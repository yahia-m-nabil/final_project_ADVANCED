package main.java.org.example.final_project.model;

import java.util.ArrayList;

public class WarehouseList {
    private ArrayList<Warehouse> warehouses;

    public WarehouseList() {
        this.warehouses = new ArrayList<>();
    }

    public ArrayList<Warehouse> getAllWarehouses() {
        return warehouses;
    }

    public void addWarehouse(Warehouse warehouse) {
        this.warehouses.add(warehouse);
    }

    public void removeWarehouse(String location) {
        int IndexId = -1;
        for (int i = 0; i < warehouses.size(); i++) {
            if (warehouses.get(i).getLocation().equals(location)) {
                IndexId = i;
                break;
            }
        }
        if (IndexId != -1) {
            warehouses.remove(IndexId);
        }
    }

    public Warehouse getWarehouse(String location) {
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getLocation().equals(location)) {
                return warehouse;
            }
        }
        return null;
    }

}
