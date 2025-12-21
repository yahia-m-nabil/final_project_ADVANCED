package org.example.final_project.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class Order {

    /* ======================== FIELDS ===================== */

    private static int nextOrderId = 1000;
    
    private final int orderId;
    private final int customerId;
    private OrderStatus status;
    private final ArrayList<FurnitureItem> items;
    private final LocalDateTime orderDate;

    /* ======================== CONSTRUCTOR ===================== */

    public Order(int customerId, FurnitureItem[] items) {
        if (items == null || items.length == 0) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        if (customerId <= 0) {
            throw new IllegalArgumentException("Customer ID must be positive");
        }
        
        this.orderId = generateOrderId();
        this.customerId = customerId;
        this.items = new ArrayList<>();
        Collections.addAll(this.items, items);
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
    }

    /* ======================== ID GENERATION ===================== */

    public static synchronized int generateOrderId() {
        return nextOrderId++;
    }

    /* ======================== GETTERS ===================== */

    public int getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public ArrayList<FurnitureItem> getItems() {
        return new ArrayList<>(items);
    }

    public int getItemCount() {
        return items.size();
    }

    public int getTotalQuantity() {
        int total = 0;
        for (FurnitureItem item : items) {
            total += item.getQuantity();
        }
        return total;
    }

    /* ======================== PRICE CALCULATION ===================== */

    public int getTotalPrice() {
        int price = 0;
        for (FurnitureItem item : items) {
            price += item.getTotalPrice();
        }
        return price;
    }

    public String getFormattedTotalPrice() {
        return "$" + getTotalPrice();
    }

    /* ======================== ORDER STATUS MANAGEMENT ===================== */

    public void setStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Order status cannot be null");
        }
        this.status = status;
    }

    public void markAsShipped() {
        if (status == OrderStatus.REFUNDED) {
            throw new IllegalStateException("Cannot ship a refunded order");
        }
        this.status = OrderStatus.SHIPPED;
    }

    public void markAsDelivered() {
        if (status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Order must be shipped before delivery");
        }
        this.status = OrderStatus.DELIVERED;
    }

    /* ======================== REFUND OPERATIONS ===================== */

    public ArrayList<FurnitureItem> refundOrder() {
        if (status == OrderStatus.REFUNDED) {
            throw new IllegalStateException("Order already refunded");
        }
        
        ArrayList<FurnitureItem> refundedItems = new ArrayList<>(items);
        items.clear();
        this.status = OrderStatus.REFUNDED;
        return refundedItems;
    }

    /* ======================== ITEM QUERIES ===================== */

    public FurnitureItem findItemById(int itemId) {
        for (FurnitureItem item : items) {
            if (item.getItemID() == itemId) {
                return item;
            }
        }
        return null;
    }

    public boolean containsItem(int itemId) {
        return findItemById(itemId) != null;
    }

    /* ======================== OBJECT METHODS ===================== */

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Order{" +
                "ID=" + orderId +
                ", Customer=" + customerId +
                ", Status=" + status +
                ", Items=" + getItemCount() +
                ", Total=" + getFormattedTotalPrice() +
                ", Date=" + orderDate.format(formatter) +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return orderId == order.orderId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(orderId);
    }
}
