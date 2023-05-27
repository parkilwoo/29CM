package kr.co._29cm.homework.domain;

import java.lang.reflect.Field;

public class Order {
    private Integer productId;
    private Integer count;

    public Order() {

    }


    public enum OrderAttribute {
        PRODUCT_ID("상품번호", "productId"),
        COUNT("수량", "count");
        private final String name;
        private final String field;
        public String getName() {
            return this.name;
        }
        public String getField() {
            return this.field;
        }
        OrderAttribute(String name, String field) {
            this.name = name;
            this.field = field;
        }
    }

    public void OrderSetting(String fieldName, Object setValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(fieldName);

        field.set(this, setValue);
    }

    public String checkOrderAttribute() {
        if(this.productId == null && this.count == null) return "";
        if(this.productId == null) return OrderAttribute.PRODUCT_ID.getName();
        if(this.count == null || this.count == 0) return OrderAttribute.COUNT.getName();
        return null;
    }

    public Integer productId() {return this.productId;}
    public Integer count() {return this.count;}

    public String generateOrderMessage(String productName) {
        return productName + " - " + this.count + "개";
    }

}
