package kr.co._29cm.homework.databse;

import kr.co._29cm.homework.Main;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class SlaveDatabase extends Database{

//    private final int ISOLATION_LEVEL = Connection.TRANSACTION_SERIALIZABLE;
    private final URL slaveYamlUrl = Main.class.getClassLoader().getResource("config/slave_db.yaml");
    private final String slaveYamlPath = Objects.requireNonNull(slaveYamlUrl).getPath();
    private SlaveDatabase() throws FileNotFoundException, ClassNotFoundException {
        super.setupDatabase(slaveYamlPath);
    }

    private static class SingletonHelper{
        private static final SlaveDatabase INSTANCE;

        static {
            try {
                INSTANCE = new SlaveDatabase();
            } catch (FileNotFoundException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static SlaveDatabase getInstance(){
        return SlaveDatabase.SingletonHelper.INSTANCE;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return super.connection();
    }

    @Override
    String generateWhereQueryForFindById(String idColumName, String value) {
        return idColumName + "=" + value + ";";
    }

    @Override
    public void update(Statement statement, String sql) throws SQLException {
        super.updateUseStatement(statement, sql);
    }

    public<T> List<T> findAll(Class<? extends Orm> domainClass, Statement stmt) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        return super.findAll(domainClass, stmt);
    }

    public<T> T findById(Class<? extends Orm> domainClass, Statement stmt, String idColumName, String value ) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        String whereQuery = generateWhereQueryForFindById(idColumName, value);
        return super.findById(domainClass, stmt, whereQuery);
    }

}
