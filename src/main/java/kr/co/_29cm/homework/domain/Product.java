package kr.co._29cm.homework.domain;


import kr.co._29cm.homework.databse.InitData;
import kr.co._29cm.homework.databse.Orm;
import kr.co._29cm.homework.util.Common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Product extends Orm implements InitData {
    private Integer productId;
    private String name;

    private Integer price;
    private Integer stockCount;

    public Product() {

    }
    @Override
    public Product objectMapping(ResultSet resultSet) throws SQLException {
        this.productId = resultSet.getInt(1);
        this.name = resultSet.getString(2);
        this.price = resultSet.getInt(3);
        this.stockCount = resultSet.getInt(4);

        return this;
    }

    protected String toStringPromptFormat() {
        String[] dataArray = new String[] {String.valueOf(productId), name, String.valueOf(price), String.valueOf(stockCount)};

        return String.join("   ", dataArray);
    }

    @Override
    public PreparedStatement csvDataToPreparedStatement(Connection connection, String insertBaseQuery, String csvData) throws SQLException {
        String fieldsQuery = super.generateInsertFieldsQuery(this.getClass());
        String ptmtQuery = String.format(insertBaseQuery, this.getClass().getSimpleName().toUpperCase(), fieldsQuery);
        PreparedStatement ptmt = connection.prepareStatement(ptmtQuery);
        String[] csvDataSeparateArray = csvData.split(",");
        int arrayLength = csvDataSeparateArray.length;
        ptmt.setInt(1, Integer.parseInt(csvDataSeparateArray[0]));
        if (arrayLength > 4) ptmt.setString(2, Common.arrayMergeToString(csvDataSeparateArray));
        else ptmt.setString(2,csvDataSeparateArray[1]);
        ptmt.setInt(3, Integer.parseInt(csvDataSeparateArray[arrayLength-2]));
        ptmt.setInt(4, Integer.parseInt(csvDataSeparateArray[arrayLength-1]));
        return ptmt;
    }

    public Integer productId() {return this.productId;}
    public String name() {return this.name;}
    public Integer price() {return this.price;}
    public Integer stockCount() {return this.stockCount;}

    public void subtractStockCount(int num) {this.stockCount -= num;}

    public String generateUpdateStockCountQuery() {
        return String.format("UPDATE PRODUCT SET STOCK_COUNT=%s WHERE PRODUCT_ID=%s", this.stockCount, this.productId);
    }
}
