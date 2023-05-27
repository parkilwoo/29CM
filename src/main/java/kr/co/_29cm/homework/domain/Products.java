package kr.co._29cm.homework.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Products {
    private final List<Product> products;

    public Products(List<Product> products) {
        this.products = products;
    }

    public String productToStringPromptFormat() {
        return products.stream()
                .map(Product::toStringPromptFormat)
                .collect(Collectors.joining("\n"));
    }
}
