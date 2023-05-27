package kr.co._29cm.homework.exception;

import java.util.NoSuchElementException;

public class NoOrderException extends NoSuchElementException {
    public NoOrderException() {
        super("주문한 내역이 없습니다.");
    }
}
