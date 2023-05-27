package kr.co._29cm.homework.databse;

import kr.co._29cm.homework.Main;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;

public class MasterDatabase extends Database{

    private final int ISOLATION_LEVEL = Connection.TRANSACTION_SERIALIZABLE;
    private final URL masterYamlUrl = Main.class.getClassLoader().getResource("config/master_db.yaml");
    private final String masterYamlPath = Objects.requireNonNull(masterYamlUrl).getPath();
    private final String FOR_UPDATE = " FOR UPDATE;";

    private MasterDatabase() throws FileNotFoundException, ClassNotFoundException {
        super.setupDatabase(masterYamlPath);
    }


    private static class SingletonHelper{
        private static final MasterDatabase INSTANCE;

        static {
            try {
                INSTANCE = new MasterDatabase();
            } catch (FileNotFoundException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static MasterDatabase getInstance(){
        return SingletonHelper.INSTANCE;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return super.connection();
    }

    @Override
    String generateWhereQueryForFindById(String idColumName, String value) {
        return idColumName + "=" + value + FOR_UPDATE;
    }

    @Override
    public void update(Statement statement, String sql) throws SQLException {
        super.updateUseStatement(statement, sql);
    }

    public<T> T findById(Class<? extends Orm> domainClass, Statement stmt, String idColumName, String value) throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        String whereQuery = generateWhereQueryForFindById(idColumName, value);
        return super.findById(domainClass, stmt, whereQuery);
    }

}
