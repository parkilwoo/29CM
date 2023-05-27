package kr.co._29cm.homework.databse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface InitData {
    PreparedStatement csvDataToPreparedStatement(Connection connection, String insertBaseQuery, String csvData) throws SQLException;
}
