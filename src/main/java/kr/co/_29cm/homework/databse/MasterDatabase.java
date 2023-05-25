package kr.co._29cm.homework.databse;

import kr.co._29cm.homework.domain.Product;
import kr.co._29cm.homework.util.Common;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MasterDatabase extends Database{

    private final int ISOLATION_LEVEL = Connection.TRANSACTION_SERIALIZABLE;

    private MasterDatabase(){}

    private static class SingletonHelper{
        private static final MasterDatabase INSTANCE = new MasterDatabase();
    }

    public static MasterDatabase getInstance(){
        return SingletonHelper.INSTANCE;
    }


    public void setConnectInfo(String configPath) throws FileNotFoundException, ClassNotFoundException {
        super.setConnectInfo(configPath);
    }
//    public void MasterDatabase(String configPath) throws Exception {
//        super.setConnectInfo(configPath);
//    }

    @Override
    void setUpInitData(String initFilePath, InitFileFormat fileFormat) {

    }

    private void execute(Product product, DML queryType) throws IllegalAccessException {
        String query = product.generateQuery(queryType);
        super.update(query, ISOLATION_LEVEL);
    }

    @Override
    <T> List<T> select(Product product) {
        return null;
    }
    @Override
    public void update(Product product) throws IllegalAccessException {
        execute(product, DML.UPDATE);
    }
    @Override
    public void insert(Product product) throws IllegalAccessException {
        execute(product, DML.INSERT);
    }
}
