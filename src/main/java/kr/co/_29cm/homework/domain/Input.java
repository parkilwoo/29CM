package kr.co._29cm.homework.domain;

public enum Input {
    ORDER("o"),
    QUIT("q");
    private final String value;
    public String getValue() {
        return this.value;
    }
    Input(String inputValue) {
        this.value = inputValue;
    }
}
