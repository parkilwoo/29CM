package kr.co._29cm.homework.databse;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public interface CommonDatabase {
    Map<String, String> getConnectInfo(String configPath) throws FileNotFoundException;

    Connection connect(String url, String user, String password) throws ClassNotFoundException, SQLException;

    void execute(String sql, String[] inputParameter);
}
