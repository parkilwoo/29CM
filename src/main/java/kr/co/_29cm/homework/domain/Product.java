package kr.co._29cm.homework.domain;


import kr.co._29cm.homework.databse.DML;
import kr.co._29cm.homework.util.Common;
import lombok.Builder;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Builder
public class Product {
    private Integer productId;
    private String name;
    private Integer price;
    private Integer stockCount;

    private final String INSERT_QUERY = "INSERT INTO PRODUCT VALUES(%s, %s, %s, %s)";
    private final String UPDATE_QUERY = "UPDATE PRODUCT SET %s WHERE PRODUCT_ID = %s";

    public String generateQuery(DML queryType) throws IllegalAccessException {
        String query = "";
        switch (queryType) {
            case SELECT:
                break;
            case INSERT:
                query = generateInsertQuery();
                break;
            case UPDATE:
                query =  generateUpdateQuery();
                break;
        }
        return query;
    }
    private String generateInsertQuery() {
        return String.format(INSERT_QUERY, productId, name, price, stockCount);
    }

    private String generateUpdateQuery() throws IllegalAccessException {
        List<String> stringList = new LinkedList<>();
        Field[] fields =  this.getClass().getDeclaredFields();
        Object fieldValue;
        for (Field field : fields) {
            if (field.getName().equals("productId")) continue;
            field.setAccessible(true);
            fieldValue = field.get(this);
            if(fieldValue == null) continue;
            stringList.add(Common.camelCaseToSnakeCase(field.getName()) + "=" + fieldValue);
        }
        return String.format(UPDATE_QUERY, String.join(" AND ", stringList), productId);
    }
}
