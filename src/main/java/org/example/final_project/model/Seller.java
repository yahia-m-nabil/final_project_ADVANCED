/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.final_project.model;

import java.util.ArrayList;

public class  Seller extends Member {
    /*===========================DATA MEMBERS================================ */
    private ArrayList<FurnitureItem> sellableFurniture;
    private ArrayList<Order> salesHistory;
    /* ======================== constructor  functions ===================== */
    public Seller(int id, String name, String email) {
        super(id, name, email);
        this.sellableFurniture = new ArrayList<>();
        this.salesHistory = new ArrayList<>();
    }

    /* ======================== getter functions ===================== */
    public ArrayList<FurnitureItem> getSellableFurniture() {
        return sellableFurniture;
    }

    public ArrayList<Order> getSalesHistory() {
        return salesHistory;
    }

    public FurnitureItem findItemInInventory(int itemID) {
        for (FurnitureItem item : sellableFurniture) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null; // Not found
    }

    /* ======================== setter functions ===================== */
    
    public void setSellableFurniture(ArrayList<FurnitureItem> sellableFurniture) {
        this.sellableFurniture = sellableFurniture;
    }

    public void setSalesHistory(ArrayList<Order> salesHistory) {
        this.salesHistory = salesHistory;
    }

    public void addSale(Order sale) {
        salesHistory.add(sale);
    }

    /* ======================== item functions ===================== */


    public void createFurnitureItem(FurnitureItem newItem ) {
        newItem.setPrice(newItem.calculatePrice(TableStorage.getInstance()));    
        FurnitureItem existing = findItemInInventory(newItem.getItemID());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
        } else {
            sellableFurniture.add(newItem);
        }
    }

    /* ======================== inventory functions ===================== */
    
    public void supplyToWarehouse(Warehouse warehouse, int itemID, int quantity) {
        FurnitureItem sellerItem = findItemInInventory(itemID);
        if (sellerItem != null && sellerItem.getQuantity() >= quantity) {
            // Add to warehouse
            warehouse.addItems(sellerItem, quantity);
            
            // Calculate earnings
            int earnings = sellerItem.getPrice() * quantity;
            setMoney(getMoney() + earnings);
            
            // Remove from seller's inventory
            sellerItem.setQuantity(sellerItem.getQuantity() - quantity);
            if (sellerItem.getQuantity() == 0) {
                sellableFurniture.remove(sellerItem);
            }
            
            // Create an order for this supply transaction (record keeping)
            FurnitureItem[] itemsArray = {sellerItem};
            Order supplyOrder = new Order(Order.generateOrderId(), itemsArray);
            supplyOrder.setStatus(OrderStatus.DELIVERED);
            addSale(supplyOrder);
        }
        else {
            throw new IllegalArgumentException("Insufficient quantity in seller's inventory.");
        }
    }
    
    public void supplyAllToWarehouse(Warehouse warehouse) {
        if (sellableFurniture.isEmpty()) {
            throw new IllegalArgumentException("No items available to supply to warehouse.");
        }
        setMoney(getMoney() + CalculateMoney());
        FurnitureItem[] itemsArray = sellableFurniture.toArray(new FurnitureItem[0]);
        Order supplyOrder = new Order(Order.generateOrderId(), itemsArray);
        supplyOrder.setStatus(OrderStatus.DELIVERED);
        addSale(supplyOrder);
        for (FurnitureItem item : sellableFurniture) {
            warehouse.addItems(item, item.getQuantity());
        }
        sellableFurniture.clear();
    }

    public void removeFromInventory(int itemID, int quantity) {
        FurnitureItem item = findItemInInventory(itemID);
        if (item != null) {
            int newQuantity = item.getQuantity() - quantity;
            if (newQuantity <= 0) {
                sellableFurniture.remove(item);
            } else {
                item.setQuantity(newQuantity);
            }
        }
    }

    /* ======================== MONEY functions ===================== */
    
    public void addMoney(int amount) {
        setMoney(getMoney() + amount);
    }
    
    public void withdrawMoney(int amount) throws InvalidAmountException {
        if (amount > getMoney()) {
            throw new InvalidAmountException("Trying to withdraw more than what's available in the account");
        }
        setMoney(getMoney() - amount);
    }
    
    @Override
    public int CalculateMoney() {
        // Calculate total inventory value (doesn't change current money)
        int total = 0;
        if (sellableFurniture != null) {
            for (FurnitureItem item : sellableFurniture) {
                if (item != null) {
                    total += item.getPrice() * item.getQuantity();
                }
            }
        }
        return total;
    }
}
    
       
    
