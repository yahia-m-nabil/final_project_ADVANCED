package org.example.final_project.model;

import java.util.ArrayList;

public class WarehouseList {

    /* ======================== FIELDS ===================== */

    private final ArrayList<Warehouse> warehouses;

    /* ======================== CONSTRUCTOR ===================== */

    public WarehouseList() {
        this.warehouses = new ArrayList<>();
    }

    /* ======================== GETTERS ===================== */

    public ArrayList<Warehouse> getAllWarehouses() {
        return new ArrayList<>(warehouses);
    }

    public int getWarehouseCount() {
        return warehouses.size();
    }

    public int getTotalInventoryValue() {
        int total = 0;
        for (Warehouse warehouse : warehouses) {
            total += warehouse.getTotalInventoryValue();
        }
        return total;
    }

    public int getTotalInventoryQuantity() {
        int total = 0;
        for (Warehouse warehouse : warehouses) {
            total += warehouse.getTotalInventoryQuantity();
        }
        return total;
    }

    public String getFormattedTotalValue() {
        return "$" + getTotalInventoryValue();
    }

    /* ======================== SEARCH METHODS ===================== */

    public Warehouse getWarehouseByLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return null;
        }
        String normalizedLocation = location.trim();
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getLocation().equalsIgnoreCase(normalizedLocation)) {
                return warehouse;
            }
        }
        return null;
    }

    public boolean locationExists(String location) {
        return getWarehouseByLocation(location) != null;
    }

    /* ======================== WAREHOUSE MANAGEMENT ===================== */

    public void addWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse cannot be null");
        }
        if (locationExists(warehouse.getLocation())) {
            throw new IllegalArgumentException("Warehouse with location '" + warehouse.getLocation() + "' already exists");
        }
        warehouses.add(warehouse);
    }

    public boolean removeWarehouse(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        Warehouse warehouse = getWarehouseByLocation(location);
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse with location '" + location + "' not found");
        }
        return warehouses.remove(warehouse);
    }

    /* ======================== UTILITY METHODS ===================== */

    public boolean isEmpty() {
        return warehouses.isEmpty();
    }

    public FurnitureItem findItemAcrossWarehouses(int itemID) {
        for (Warehouse warehouse : warehouses) {
            FurnitureItem item = warehouse.findItemByID(itemID);
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    public Warehouse findWarehouseWithItem(int itemID) {
        for (Warehouse warehouse : warehouses) {
            if (warehouse.hasItem(itemID)) {
                return warehouse;
            }
        }
        return null;
    }

    public int getTotalStockForItem(int itemID) {
        int total = 0;
        for (Warehouse warehouse : warehouses) {
            FurnitureItem item = warehouse.findItemByID(itemID);
            if (item != null) {
                total += item.getQuantity();
            }
        }
        return total;
    }

    /* ======================== OBJECT METHODS ===================== */

    @Override
    public String toString() {
        return "WarehouseList{" +
                "Warehouses=" + getWarehouseCount() +
                ", Total Value=" + getFormattedTotalValue() +
                ", Total Units=" + getTotalInventoryQuantity() +
                '}';
    }
}

