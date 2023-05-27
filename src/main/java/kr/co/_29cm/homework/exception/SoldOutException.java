package kr.co._29cm.homework.exception;

public class SoldOutException extends IndexOutOfBoundsException{
    public SoldOutException(String productName) {
        super(String.format("SoldOutException 발생. 주문한 %s 상품량이 재고량보다 많습니다.", productName));
    }
}
