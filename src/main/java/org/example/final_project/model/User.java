package org.example.final_project.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class User extends Member {

    private ArrayList<FurnitureItem> wishlist;
    private ArrayList<Order> orderHistory;

    /* ======================== constructor  functions ===================== */

    public User(String name, String email, int memberid ) {
        super(memberid, name, email);
        this.wishlist = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }

    /* ======================== checkout & refund functions ===================== */

    public void Checkout(Warehouse warehouse){
        int totalPrice = CalculateMoney();
        if (totalPrice > getMoney()) {
            System.out.println("Insufficient funds to complete the purchase.");
            return;
        }

        // Deduct the total price from user's money
        setMoney(getMoney()-totalPrice);

        // Create a new order
        FurnitureItem[] itemsArray = wishlist.toArray(new FurnitureItem[0]);
        Order newOrder = new Order(Order.generateOrderId(), itemsArray);
        addToOrderHistory(newOrder);

        // Remove items from warehouse based on quantities
        for (FurnitureItem item : wishlist) {
            warehouse.removeItems(item.getItemID(), item.getQuantity());
        }

        // Clear the wishlist after checkout
        //warehouse.removeitems(itemsArray); //removes the items from warehouse
        wishlist.clear();

        //will be replaced with GUI
        //System.out.println("Checkout successful! Order ID: " + newOrder.getOrderId()); =========================================================================

    }

    public void RefundOrder(int orderId, Warehouse warehouse) {
        if (orderHistory.isEmpty()){
            return;
        }
        Order ordertorefund = null;
        for(Order z : orderHistory){
            if (z.getOrderid() == orderId) {
                ordertorefund = z;
                break;
            }
        }
        if (ordertorefund == null){
            return;
        }
        if (LocalDate.now().isAfter(ordertorefund.getDate().toLocalDate().plusDays(30))) {
            //will be replaced with GUI
            //System.out.println("Refund period has expired for this order."); ==============================================================================
            return;
        }
        int refundAmount = ordertorefund.GetAllItemsPrice();
        setMoney(getMoney()+refundAmount);
        
        // Return items to warehouse
        ArrayList<FurnitureItem> refundedItems = ordertorefund.RefundItems();
        for (FurnitureItem item : refundedItems) {
            warehouse.addItems(item, item.getQuantity());
        }
        
        //will be replaced with GUI
        //System.out.println("Order refunded successfully! Refund Amount: " + refundAmount); =========================================================================
    }

    /* ======================== order functions ===================== */

    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }

    public void addToOrderHistory(Order new_order){
        orderHistory.add(new_order);
    }

    /* ======================== wishlist functions ===================== */

    public ArrayList<FurnitureItem> getWishlist() {
        return wishlist;
    }
    
    public FurnitureItem findItemInWishlist(int itemID) {
        for (FurnitureItem item : wishlist) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null;
    }

    public void addToWishlist(FurnitureItem item) {
        for (FurnitureItem f : wishlist) {
            if (f.getItemID() == item.getItemID()) {
                f.setQuantity(f.getQuantity() + item.getQuantity());
                return;
            }
        }
        wishlist.add(item);
    }

    public void removeFromWishlist(int itemid , int quantity) {
        FurnitureItem item = findItemInWishlist(itemid);
        if (item == null) {
            throw new IllegalArgumentException("Item not found in wishlist");
        }
        if (item.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient quantity in wishlist");
        }
        
        int newQuantity = item.getQuantity() - quantity;
        if (newQuantity == 0) {
            wishlist.remove(item);
        } else {
            item.setQuantity(newQuantity);
        }
    }

    /* ======================== money functions ===================== */

    public void addMoney(int amount ) {
        setMoney(getMoney()+amount);
    }

    @Override
    public int CalculateMoney() {
        int total = 0;
        int CustomerTax = 15; // 15% tax
        for (FurnitureItem item : wishlist) {
            total += item.getPrice() * item.getQuantity();
        }
        return (total + (total * CustomerTax / 100));
    }

    public void ReturnMoney(int amount) throws InvalidAmountException {
        if(amount > getMoney()){
            throw new InvalidAmountException("trying to withdraw more than whats currently available in the account");
        }
        setMoney(getMoney()-amount);
    }

}
