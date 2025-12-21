package org.example.final_project.model;

import java.util.ArrayList;
import java.util.Collections;

public class Warehouse {

    /* ======================== FIELDS ===================== */

    private final String location;
    private final ArrayList<FurnitureItem> inventory;

    /* ======================== CONSTRUCTOR ===================== */

    public Warehouse(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        this.location = location.trim();
        this.inventory = new ArrayList<>();
    }

    /* ======================== GETTERS ===================== */

    public String getLocation() {
        return location;
    }

    public ArrayList<FurnitureItem> getInventory() {
        return new ArrayList<>(inventory);
    }

    public int getInventoryCount() {
        return inventory.size();
    }

    public int getTotalInventoryQuantity() {
        int total = 0;
        for (FurnitureItem item : inventory) {
            total += item.getQuantity();
        }
        return total;
    }

    public int getTotalInventoryValue() {
        int total = 0;
        for (FurnitureItem item : inventory) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public String getFormattedInventoryValue() {
        return "$" + getTotalInventoryValue();
    }

    /* ======================== SEARCH METHODS ===================== */

    public FurnitureItem findItemByID(int itemID) {
        for (FurnitureItem item : inventory) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null;
    }

    public boolean hasItem(int itemID) {
        return findItemByID(itemID) != null;
    }

    public boolean hasEnoughStock(int itemID, int quantity) {
        FurnitureItem item = findItemByID(itemID);
        return item != null && item.getQuantity() >= quantity;
    }

    /* ======================== INVENTORY MANAGEMENT ===================== */

    public void addItems(FurnitureItem item, int quantityToAdd) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("Quantity to add must be positive");
        }

        FurnitureItem existing = findItemByID(item.getItemID());
        if (existing != null) {
            existing.addQuantity(quantityToAdd);
        } else {
            inventory.add(item);
        }
    }

    public void removeItems(int itemID, int quantityToRemove) {
        if (quantityToRemove <= 0) {
            throw new IllegalArgumentException("Quantity to remove must be positive");
        }

        FurnitureItem item = findItemByID(itemID);
        if (item == null) {
            throw new IllegalArgumentException("Item ID " + itemID + " not found in warehouse");
        }
        if (item.getQuantity() < quantityToRemove) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + item.getQuantity() + ", Requested: " + quantityToRemove);
        }

        item.reduceQuantity(quantityToRemove);
        if (item.getQuantity() == 0) {
            inventory.remove(item);
        }
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    /* ======================== SORTING OPERATIONS ===================== */

    public void sortInventoryByPrice() {
        int n = inventory.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (inventory.get(j).getPrice() > inventory.get(j + 1).getPrice()) {
                    FurnitureItem temp = inventory.get(j);
                    inventory.set(j, inventory.get(j + 1));
                    inventory.set(j + 1, temp);
                }
            }
        }
    }

    public void sortInventoryByID() {
        Collections.sort(inventory);
    }

    public void sortInventoryByQuantity() {
        int n = inventory.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (inventory.get(j).getQuantity() < inventory.get(j + 1).getQuantity()) {
                    FurnitureItem temp = inventory.get(j);
                    inventory.set(j, inventory.get(j + 1));
                    inventory.set(j + 1, temp);
                }
            }
        }
    }

    /* ======================== OBJECT METHODS ===================== */

    @Override
    public String toString() {
        return "Warehouse{" +
                "Location='" + location + '\'' +
                ", Items=" + getInventoryCount() +
                ", Total Units=" + getTotalInventoryQuantity() +
                ", Total Value=" + getFormattedInventoryValue() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Warehouse warehouse = (Warehouse) obj;
        return location.equalsIgnoreCase(warehouse.location);
    }

    @Override
    public int hashCode() {
        return location.toLowerCase().hashCode();
    }
}
