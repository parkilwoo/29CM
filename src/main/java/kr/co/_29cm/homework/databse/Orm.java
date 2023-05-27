package kr.co._29cm.homework.databse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public abstract class Orm {
    public abstract<T> T objectMapping(ResultSet resultSet) throws SQLException;

    protected<T> String generateInsertFieldsQuery(Class<T> targetClass) {
        int fieldCount = targetClass.getDeclaredFields().length;
        String[] fieldArray = new String[fieldCount];
        Arrays.fill(fieldArray, "?");
        String fieldInputString = String.join(", ", fieldArray);
        return  "(" + fieldInputString + ")";
    }
}
