package kr.co._29cm.homework.databse;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class H2Database implements DatabaseConnector{

    private Connection connection;

    private String JDBC_DRIVER;
    private String DB_URL;
    private String USER;
    private String PASSWORD;

    @Override
    public Map<String, String> getConnectInfo(String configPath) throws FileNotFoundException {

    }

    @Override
    public Connection connect(String url, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        this.connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        return this.connection;
    }
}
