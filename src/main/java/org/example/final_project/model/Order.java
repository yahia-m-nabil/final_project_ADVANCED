package org.example.final_project.model;

import java.time.LocalDateTime;

public class Order {
    private int orderid ;
    private OrderStatus status;
    private FurnitureItem[] items;
    private java.time.LocalDateTime date;

    Order(int orderid , FurnitureItem[] items){
        this.orderid=orderid;
        this.items = items.clone();
        this.status = OrderStatus.PENDING;
        this.date= LocalDateTime.now();
    }

    public int displayAllItemsPrice(){
        int price;
        for(FurnitureItem z : items){
            price += z.getPrice();
        }
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getOrderid() {
        return orderid;
    }

    public FurnitureItem[] getItems() {
        return items;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
