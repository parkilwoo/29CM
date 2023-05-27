package kr.co._29cm.homework.exception;

public class PromptException extends IllegalArgumentException{
    public PromptException() {
        super("잘못된 입력 발생");
    }
}
