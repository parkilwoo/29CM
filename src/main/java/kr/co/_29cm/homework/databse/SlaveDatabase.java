package kr.co._29cm.homework.databse;

import kr.co._29cm.homework.domain.Product;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.List;

public class SlaveDatabase extends Database{

//    private final int ISOLATION_LEVEL = Connection.TRANSACTION_SERIALIZABLE;

    private SlaveDatabase(){}

    private static class SingletonHelper{
        private static final SlaveDatabase INSTANCE = new SlaveDatabase();
    }

    public static SlaveDatabase getInstance(){
        return SlaveDatabase.SingletonHelper.INSTANCE;
    }


    public void setConnectInfo(String configPath) throws FileNotFoundException, ClassNotFoundException {
        super.setConnectInfo(configPath);
    }
    @Override
    void setUpInitData(String initFilePath, InitFileFormat fileFormat) {

    }

    private void execute(Product product, DML queryType) throws IllegalAccessException {
        String query = product.generateQuery(queryType);
        super.update(query, Connection.TRANSACTION_READ_COMMITTED);
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
