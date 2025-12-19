package org.example.final_project.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class Order {
    private int orderid ;
    private OrderStatus status;
    private ArrayList<FurnitureItem> items;
    private java.time.LocalDateTime date;

    Order(int orderid , FurnitureItem[] items){
        this.orderid=orderid;
        this.items=new ArrayList<>();
        Collections.addAll(this.items,items);
        this.status = OrderStatus.PENDING;
        this.date= LocalDateTime.now();
    }

    public static int generateOrderId(){
        return (int)(Math.random() * 100000);
    }

    public void addItem(FurnitureItem item){
        items.add(item);
    }

//    public void PrintItems(){
//        for(FurnitureItem z : items){
//            System.out.println(z);
//        }
//    }

    public int GetAllItemsPrice(){
        int price=0;
        for(FurnitureItem z : items){
            price += z.getPrice();
        }
        return price;
    }

//    public FurnitureItem RefundItem(int itemID){
//        FurnitureItem refunditem=null;
//        for(int i=0;i<items.size();i++) {
//            if(items.get(i).getItemID() == itemID){
//                refunditem = items.get(i);
//                items.remove(i);
//                break;
//
//            }
//        }
//        if (items.isEmpty()){
//            setStatus(OrderStatus.REFUNDED);
//        }
//        if (refunditem==null){
//            return null ;
//        }
//        return refunditem;
//    }

    public ArrayList<FurnitureItem> RefundItems(){
        ArrayList<FurnitureItem> refunditems = new ArrayList<>(items);
        items.clear();
        setStatus(OrderStatus.REFUNDED);
        return refunditems;
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

    public ArrayList<FurnitureItem> getItems() {
        return items;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
