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
        ordertorefund.setStatus(OrderStatus.REFUNDED);
       // warehouse.additems((ordertorefund.getItems()).toArray(new FurnitureItem[0]));  //returns the items to warehouse
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
        FurnitureItem item = null;
        for (FurnitureItem f : wishlist) {
            if (f.getItemID() == itemid) {
                item = f;
                break;
            }
        }
        if(item.getQuantity()==0 || item.getQuantity()<quantity){
            wishlist.remove(item);
        }
        else{
            item.setQuantity(item.getQuantity()-quantity);
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
            total += item.getPrice();
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
