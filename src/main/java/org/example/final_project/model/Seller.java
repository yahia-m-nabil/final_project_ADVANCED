package org.example.final_project.model;

import java.util.ArrayList;

public class Seller extends Member {

    /* ======================== FIELDS ===================== */

    private final ArrayList<FurnitureItem> sellableFurniture;
    private final ArrayList<Order> salesHistory;
    private static final int PLATFORM_FEE_PERCENTAGE = 5;

    /* ======================== CONSTRUCTOR ===================== */

    public Seller(int id, String name, String email) {
        super(id, name, email);
        this.sellableFurniture = new ArrayList<>();
        this.salesHistory = new ArrayList<>();
    }

    /* ======================== GETTERS ===================== */

    public ArrayList<FurnitureItem> getSellableFurniture() {
        return new ArrayList<>(sellableFurniture);
    }

    public ArrayList<Order> getSalesHistory() {
        return new ArrayList<>(salesHistory);
    }

    public int getInventoryCount() {
        return sellableFurniture.size();
    }

    public int getTotalInventoryQuantity() {
        int total = 0;
        for (FurnitureItem item : sellableFurniture) {
            total += item.getQuantity();
        }
        return total;
    }

    public int getTotalSalesCount() {
        return salesHistory.size();
    }

    public String getFormattedInventoryValue() {
        return "$" + calculateMoney();
    }

    public String getFormattedTotalEarnings() {
        return "$" + getTotalEarnings();
    }

    /* ======================== SEARCH METHODS ===================== */

    public FurnitureItem findItemInInventory(int itemID) {
        for (FurnitureItem item : sellableFurniture) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null;
    }

    public boolean hasItemInInventory(int itemID) {
        return findItemInInventory(itemID) != null;
    }

    public Order findSaleById(int orderId) {
        for (Order sale : salesHistory) {
            if (sale.getOrderId() == orderId) {
                return sale;
            }
        }
        return null;
    }

    /* ======================== INVENTORY MANAGEMENT ===================== */

    public void createFurnitureItem(int itemId, int quantity, Materials material, Colors color, String type) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be empty");
        }
        
        FurnitureItem newItem;
        String itemType = type.trim().toLowerCase();
        
        switch (itemType) {
            case "chair":
                newItem = new Chair(itemId, quantity, material, color);
                break;
            case "table":
                newItem = new Table(itemId, quantity, material, color);
                break;
            case "desk":
                newItem = new Desk(itemId, quantity, material, color);
                break;
            default:
                throw new IllegalArgumentException("Invalid furniture type: " + type + ". Must be 'chair', 'table', or 'desk'");
        }
        
        newItem.setPrice(newItem.calculatePrice(TableStorage.getInstance()));
        FurnitureItem existing = findItemInInventory(itemId);
        
        if (existing != null) {
            existing.addQuantity(quantity);
        } else {
            sellableFurniture.add(newItem);
        }
    }

    public void removeFromInventory(int itemID, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to remove must be positive");
        }
        
        FurnitureItem item = findItemInInventory(itemID);
        if (item == null) {
            throw new IllegalArgumentException("Item ID " + itemID + " not found in inventory");
        }
        if (item.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient quantity. Available: " + item.getQuantity() + ", Requested: " + quantity);
        }
        
        item.reduceQuantity(quantity);
        if (item.getQuantity() == 0) {
            sellableFurniture.remove(item);
        }
    }

    public boolean hasInventory() {
        return !sellableFurniture.isEmpty();
    }

    public boolean canSupplyToWarehouse(int itemID, int quantity) {
        FurnitureItem item = findItemInInventory(itemID);
        return item != null && item.getQuantity() >= quantity;
    }

    /* ======================== WAREHOUSE SUPPLY OPERATIONS ===================== */

    public void supplyToWarehouse(Warehouse warehouse, int itemID, int quantity) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        FurnitureItem sellerItem = findItemInInventory(itemID);
        if (sellerItem == null) {
            throw new IllegalArgumentException("Item ID " + itemID + " not found in inventory");
        }
        if (sellerItem.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient quantity in inventory. Available: " + sellerItem.getQuantity());
        }
        
        warehouse.addItems(sellerItem, quantity);
        
        int grossEarnings = sellerItem.getPrice() * quantity;
        int platformFee = calculatePlatformFee(grossEarnings);
        int netEarnings = grossEarnings - platformFee;
        addMoney(netEarnings);
        
        sellerItem.reduceQuantity(quantity);
        if (sellerItem.getQuantity() == 0) {
            sellableFurniture.remove(sellerItem);
        }
        
        FurnitureItem suppliedCopy = sellerItem.createCopy(quantity);
        FurnitureItem[] itemsArray = {suppliedCopy};
        Order supplyOrder = new Order(getMemberId(), itemsArray);
        supplyOrder.setStatus(OrderStatus.DELIVERED);
        salesHistory.add(supplyOrder);
    }

    public void supplyAllToWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse cannot be null");
        }
        if (sellableFurniture.isEmpty()) {
            throw new IllegalArgumentException("No items available to supply to warehouse");
        }
        
        int grossEarnings = calculateMoney();
        int platformFee = calculatePlatformFee(grossEarnings);
        int netEarnings = grossEarnings - platformFee;
        
        ArrayList<FurnitureItem> itemsCopy = new ArrayList<>();
        for (FurnitureItem item : sellableFurniture) {
            itemsCopy.add(item.createCopy(item.getQuantity()));
        }
        FurnitureItem[] itemsArray = itemsCopy.toArray(new FurnitureItem[0]);
        
        for (FurnitureItem item : sellableFurniture) {
            warehouse.addItems(item, item.getQuantity());
        }
        
        addMoney(netEarnings);
        
        Order supplyOrder = new Order(getMemberId(), itemsArray);
        supplyOrder.setStatus(OrderStatus.DELIVERED);
        salesHistory.add(supplyOrder);
        
        sellableFurniture.clear();
    }

    /* ======================== FINANCIAL METHODS ===================== */

    @Override
    public int calculateMoney() {
        int total = 0;
        for (FurnitureItem item : sellableFurniture) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public int getTotalEarnings() {
        int total = 0;
        for (Order sale : salesHistory) {
            total += sale.getTotalPrice();
        }
        return total;
    }

    private int calculatePlatformFee(int amount) {
        return (amount * PLATFORM_FEE_PERCENTAGE) / 100;
    }

    public int getNetEarnings(int grossAmount) {
        return grossAmount - calculatePlatformFee(grossAmount);
    }

    /* ======================== OBJECT METHODS ===================== */

    @Override
    public String toString() {
        return "Seller{" +
                "ID=" + getMemberId() +
                ", Name='" + getName() + '\'' +
                ", Email='" + getEmail() + '\'' +
                ", Money=$" + getMoney() +
                ", Inventory=" + getInventoryCount() + " items" +
                ", Total Sales=" + getTotalSalesCount() +
                '}';
    }
}
    
       
    
