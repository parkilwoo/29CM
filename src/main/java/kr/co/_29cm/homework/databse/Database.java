package kr.co._29cm.homework.databse;

import kr.co._29cm.homework.Main;
import kr.co._29cm.homework.domain.Product;
import kr.co._29cm.homework.util.Common;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public abstract class Database {

    private final URL schemaSqlUrl = Main.class.getClassLoader().getResource("database/schema.sql");
    private final URL initDataUrl = Main.class.getClassLoader().getResource("database/items.csv");

    private ConnectInfo connectInfo;

    private final String INSERT_QUERY = "INSERT INTO %s VALUES %s";
    private final String FIND_ALL_QUERY = "SELECT * FROM %s";
    private final String FIND_BY_ID_QUERY = "SELECT * FROM %s WHERE %s";

    private void updateUsePreparedStatement(PreparedStatement ptmt) throws SQLException {
        ptmt.executeUpdate();
    }

    private ResultSet select(Statement stmt, String sql) throws SQLException {
        return stmt.executeQuery(sql);
    }

    protected void updateUseStatement(Statement statement, String query) throws SQLException {
        statement.execute(query);
    }


    private void executeSqlFile(String sqlFilePath) {
        try(Connection connection = DriverManager.getConnection(connectInfo.getUrl(), connectInfo.getUsername(), connectInfo.getPassword())) {
            Statement statement = connection.createStatement();
            String fileToSql = String.join("", Common.readFileConvertStringList(sqlFilePath, false));
            statement.execute(fileToSql);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(sqlFilePath + "실행중에 에러 발생");
        }
    }

    private void initInsertData(String insertDataFilePath, Class<? extends InitData> tableClass) {
        try (Connection connection = connection()){

            Class<?>[] parameterTypes = {Connection.class, String.class, String.class};
            Method csvDataToPreparedStatement = tableClass.getMethod("csvDataToPreparedStatement", parameterTypes);

            List<String> dataStringList = Common.readFileConvertStringList(insertDataFilePath, true);
            for (String readLine: dataStringList) {
                Object ob = csvDataToPreparedStatement.invoke(tableClass.getConstructor().newInstance(), connection, INSERT_QUERY, readLine);
                updateUsePreparedStatement((PreparedStatement) ob);
            }
            connection.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(insertDataFilePath + "데이터 Insert중에 에러발생");
        }
    }

    private void setConnectInfo(String configPath) throws FileNotFoundException, ClassNotFoundException {
        connectInfo = Common.getObjectToYaml(configPath, ConnectInfo.class);
        Class.forName(connectInfo.getDriverClassName());
    }

    protected void setupDatabase(String connectInfoFilePath) throws FileNotFoundException, ClassNotFoundException {
        setConnectInfo(connectInfoFilePath);

        String schemaSqPath = Objects.requireNonNull(schemaSqlUrl).getFile();
        executeSqlFile(schemaSqPath);

        String initDataPath = Objects.requireNonNull(initDataUrl).getFile();
        initInsertData(initDataPath, Product.class);
    }
    protected <T> List<T> findAll(Class<? extends Orm> domainClass, Statement stmt) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        String upperSnakeClassName = Common.camelCaseToSnakeCase(domainClass.getSimpleName());
        String findAllQuery = String.format(FIND_ALL_QUERY, upperSnakeClassName);
        Method objectMapping = domainClass.getMethod("objectMapping", ResultSet.class);

        ResultSet rs = select(stmt, findAllQuery);
        List<T> resultList = new LinkedList<>();
        while (true) {
            assert rs != null;
            if (!rs.next()) break;
            Object obj = objectMapping.invoke(domainClass.getConstructor().newInstance(),rs);
            resultList.add((T) obj);
        }
        return resultList;
    }

    protected<T> T findById(Class<? extends Orm> domainClass, Statement stmt, String whereQuery) throws NoSuchMethodException, SQLException, InvocationTargetException, IllegalAccessException, InstantiationException {
        String upperSnakeClassName = Common.camelCaseToSnakeCase(domainClass.getSimpleName());
        String findByIdQuery = String.format(FIND_BY_ID_QUERY, upperSnakeClassName, whereQuery);
        Method objectMapping = domainClass.getMethod("objectMapping", ResultSet.class);

        ResultSet rs = select(stmt, findByIdQuery);
        if(!rs.next()) return null;
        return (T) objectMapping.invoke(domainClass.getConstructor().newInstance(), rs);
    }


    protected Connection connection() throws SQLException {
        return DriverManager.getConnection(connectInfo.getUrl(), connectInfo.getUsername(), connectInfo.getPassword());
    }

    public abstract Connection getConnection() throws SQLException;

    abstract String generateWhereQueryForFindById(String idColumName, String value);
    public abstract void update(Statement statement, String sql) throws SQLException;
}
