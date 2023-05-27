package kr.co._29cm.homework.service;

import kr.co._29cm.homework.databse.MasterDatabase;
import kr.co._29cm.homework.databse.SlaveDatabase;
import kr.co._29cm.homework.domain.Order;
import kr.co._29cm.homework.domain.Orders;
import kr.co._29cm.homework.domain.Product;
import kr.co._29cm.homework.domain.Products;
import kr.co._29cm.homework.exception.NoOrderException;
import kr.co._29cm.homework.exception.NoSuchProductException;
import kr.co._29cm.homework.exception.SoldOutException;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

public class OrderService {
    private final MasterDatabase masterDatabase = MasterDatabase.getInstance();
    private final SlaveDatabase slaveDatabase = SlaveDatabase.getInstance();

    public OrderService() {
    }

    public Products getAllProducts() {
        try(Connection connection = slaveDatabase.getConnection()) {
            Statement stmt = connection.createStatement();
            List<Product> productList = slaveDatabase.findAll(Product.class, stmt);
            return new Products(productList);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("상품목록 조회중 에러발생");
        }
    }

    public Map<String, Integer> doPayment(Orders orders) throws SoldOutException, NoSuchProductException, NoOrderException{
        if (orders.isEmpty()) throw new NoOrderException();
        try(Connection masterDatabaseConnection = masterDatabase.getConnection();
            Connection slaveDatabaseConnection = slaveDatabase.getConnection()) {
            masterDatabaseConnection.setAutoCommit(false);
            slaveDatabaseConnection.setAutoCommit(false);
            Statement masterDatabaseConnectionStatement = masterDatabaseConnection.createStatement();
            Statement slaveDatabaseConnectionStatement = slaveDatabaseConnection.createStatement();
            Map<String, Integer> paymentResult = new LinkedHashMap<>();
            for (Order order : orders.getOrders()) {
                // MasterDB에서 select for update
                int productId = order.productId();
                Product findProduct = masterDatabase.findById(Product.class, masterDatabaseConnectionStatement, "PRODUCT_ID", String.valueOf(productId));
                if (findProduct == null) throw new NoSuchProductException(productId);
                // 재고없으면 Exception
                findProduct.subtractStockCount(order.count());
                if (findProduct.stockCount() < 0) throw new SoldOutException(findProduct.name());
                String updateQuery = findProduct.generateUpdateStockCountQuery();
                masterDatabase.update(masterDatabaseConnectionStatement, updateQuery);
                // masterDB 업데이트 했으면 slaveDB 업데이트
                slaveDatabase.update(slaveDatabaseConnectionStatement, updateQuery);
                paymentResult.put(order.generateOrderMessage(findProduct.name()), findProduct.price() * order.count());
            }
            masterDatabaseConnection.commit();
            slaveDatabaseConnection.commit();

            return paymentResult;
        }
        catch (NoSuchProductException | SoldOutException customException) {
            throw customException;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("결제 처리중 에러발생");
        }
    }

}
