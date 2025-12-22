package org.example.final_project.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class User extends Member {

    /* ======================== FIELDS ===================== */

    private final ArrayList<FurnitureItem> wishlist;
    private final ArrayList<Order> orderHistory;
    private Warehouse selectedWarehouse;
    private static final int CUSTOMER_TAX_PERCENTAGE = 15;
    private static final int REFUND_PERIOD_DAYS = 30;

    /* ======================== CONSTRUCTOR ===================== */

    public User(String name, String email, int memberId) {
        super(memberId, name, email);
        this.wishlist = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }

    /* ======================== GETTERS ===================== */

    public ArrayList<FurnitureItem> getWishlist() {
        return new ArrayList<>(wishlist);
    }

    public ArrayList<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory);
    }

    public int getWishlistCount() {
        return wishlist.size();
    }

    public int getTotalOrderCount() {
        return orderHistory.size();
    }

    public int getWishlistItemQuantity() {
        int total = 0;
        for (FurnitureItem item : wishlist) {
            total += item.getQuantity();
        }
        return total;
    }

    public String getFormattedWishlistTotal() {
        return "$" + calculateMoney();
    }

    public String getFormattedTotalSpent() {
        return "$" + getTotalSpent();
    }

    public Warehouse getSelectedWarehouse() {
        return selectedWarehouse;
    }

    public void setSelectedWarehouse(Warehouse warehouse) {
        this.selectedWarehouse = warehouse;
    }

    /* ======================== SEARCH METHODS ===================== */

    public FurnitureItem findItemInWishlist(int itemID) {
        for (FurnitureItem item : wishlist) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null;
    }

    public boolean hasItemInWishlist(int itemID) {
        return findItemInWishlist(itemID) != null;
    }

    public Order findOrderById(int orderId) {
        for (Order order : orderHistory) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }

    /* ======================== WISHLIST MANAGEMENT ===================== */

    public void addToWishlist(FurnitureItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Item quantity must be positive");
        }

        FurnitureItem existing = findItemInWishlist(item.getItemID());
        if (existing != null) {
            existing.addQuantity(item.getQuantity());
        } else {
            wishlist.add(item);
        }
    }

    public void removeFromWishlist(int itemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to remove must be positive");
        }

        FurnitureItem item = findItemInWishlist(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Item ID " + itemId + " not found in wishlist");
        }
        if (item.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient quantity in wishlist. Available: " + item.getQuantity());
        }

        item.reduceQuantity(quantity);
        if (item.getQuantity() == 0) {
            wishlist.remove(item);
        }
    }

    public void clearWishlist() {
        wishlist.clear();
    }

    public boolean hasWishlistItems() {
        return !wishlist.isEmpty();
    }

    /* ======================== CHECKOUT OPERATIONS ===================== */

    public void checkout(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse cannot be null");
        }
        if (wishlist.isEmpty()) {
            throw new IllegalArgumentException("Wishlist is empty. Add items before checkout");
        }

        int totalPrice = calculateMoney();
        if (totalPrice > getMoney()) {
            throw new IllegalArgumentException("Insufficient funds. Required: $" + totalPrice + ", Available: $" + getMoney());
        }

        deductMoney(totalPrice);

        FurnitureItem[] itemsArray = wishlist.toArray(new FurnitureItem[0]);
        Order newOrder = new Order(getMemberId(), itemsArray);
        orderHistory.add(newOrder);

        for (FurnitureItem item : wishlist) {
            warehouse.removeItems(item.getItemID(), item.getQuantity());
        }

        wishlist.clear();
    }

    public boolean canCheckout() {
        return !wishlist.isEmpty() && getMoney() >= calculateMoney();
    }


    /* ======================== REFUND OPERATIONS ===================== */

    public void refundOrder(int orderId, Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse cannot be null");
        }
        if (orderHistory.isEmpty()) {
            throw new IllegalArgumentException("No orders to refund");
        }

        Order orderToRefund = findOrderById(orderId);
        if (orderToRefund == null) {
            throw new IllegalArgumentException("Order ID " + orderId + " not found");
        }

        if (orderToRefund.getStatus() == OrderStatus.REFUNDED) {
            throw new IllegalArgumentException("Order already refunded");
        }

        LocalDate refundDeadline = orderToRefund.getOrderDate().toLocalDate().plusDays(REFUND_PERIOD_DAYS);
        if (LocalDate.now().isAfter(refundDeadline)) {
            throw new IllegalArgumentException("Refund period expired. Must refund within " + REFUND_PERIOD_DAYS + " days");
        }

        int refundAmount = orderToRefund.getTotalPrice();
        addMoney(refundAmount);

        ArrayList<FurnitureItem> refundedItems = orderToRefund.refundOrder();
        for (FurnitureItem item : refundedItems) {
            warehouse.addItems(item, item.getQuantity());
        }
    }

    public boolean canRefundOrder(int orderId) {
        Order order = findOrderById(orderId);
        if (order == null || order.getStatus() == OrderStatus.REFUNDED) {
            return false;
        }
        LocalDate refundDeadline = order.getOrderDate().toLocalDate().plusDays(REFUND_PERIOD_DAYS);
        return !LocalDate.now().isAfter(refundDeadline);
    }

    /* ======================== FINANCIAL METHODS ===================== */

    @Override
    public int calculateMoney() {
        return getWishlistSubtotal() + getWishlistTax();
    }

    public int getWishlistSubtotal() {
        int total = 0;
        for (FurnitureItem item : wishlist) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getWishlistTax() {
        return (getWishlistSubtotal() * CUSTOMER_TAX_PERCENTAGE) / 100;
    }

    public int getTotalSpent() {
        int total = 0;
        for (Order order : orderHistory) {
            if (order.getStatus() != OrderStatus.REFUNDED) {
                total += order.getTotalPrice();
            }
        }
        return total;
    }

    /* ======================== OBJECT METHODS ===================== */

    @Override
    public String toString() {
        return "User{" +
                "ID=" + getMemberId() +
                ", Name='" + getName() + '\'' +
                ", Email='" + getEmail() + '\'' +
                ", Money=$" + getMoney() +
                ", Wishlist=" + getWishlistCount() + " items" +
                ", Orders=" + getTotalOrderCount() +
                '}';
    }
}

