package kr.co._29cm.homework.domain;

import java.util.LinkedList;
import java.util.List;

public class Orders {
    private final List<Order> orders;

    public Orders() {
        this.orders = new LinkedList<>();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {return this.orders;}

    public void clear() { this.orders.clear(); }

    public boolean isEmpty() { return this.orders.isEmpty();}
}
