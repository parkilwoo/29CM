package kr.co._29cm.homework.exception;

import java.util.NoSuchElementException;

public class NoSuchProductException extends NoSuchElementException {
    public NoSuchProductException(int productId) {
        super(productId + "은(는) 없는 상품번호 입니다.");
    }
}
