package kr.co._29cm.homework.databse;

import kr.co._29cm.homework.domain.Product;
import kr.co._29cm.homework.util.Common;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.List;

public abstract class Database {

    private ConnectInfo connectInfo;

    protected void update(String sql, int transactionLevel) {
        try(Connection connection = DriverManager.getConnection(connectInfo.getUrl(), connectInfo.getUsername(), connectInfo.getPassword())) {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(transactionLevel);
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);

            connection.commit();
        }
        catch (Exception e) {
        }
    }


    protected ResultSet select(String sql) {
        try(Connection connection = DriverManager.getConnection(connectInfo.getUrl(), connectInfo.getUsername(), connectInfo.getPassword())) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return  rs;
        }
        catch (Exception e) {
        }
        return null;
    }

    protected void setConnectInfo(String configPath) throws FileNotFoundException, ClassNotFoundException {
        Class.forName(connectInfo.getDriverClassName());
        connectInfo = Common.getObjectToYaml(configPath, ConnectInfo.class);
    }

    abstract void setUpInitData(String initFilePath, InitFileFormat fileFormat);

    abstract<T> List<T> select(Product product);
    abstract void update(Product product) throws IllegalAccessException;
    abstract void insert(Product product) throws IllegalAccessException;
}
