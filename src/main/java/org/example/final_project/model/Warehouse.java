package org.example.final_project.model;

import java.util.ArrayList;
import java.util.Collections;

public class Warehouse {
    private String location;
    private ArrayList<FurnitureItem> Inventory;

    Warehouse(String location){
        this.location = location;
        this.Inventory = new ArrayList<>();
    }

    public void sortInventoryByPrice(){
        int n = Inventory.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (Inventory.get(j).getPrice() > Inventory.get(j+1).getPrice()) {
                    FurnitureItem temp = Inventory.get(j);
                    Inventory.set(j, Inventory.get(j+1));
                    Inventory.set(j+1, temp);
                }
            }
        }
    }

    public void sortInventorybyID(){
        Collections.sort(Inventory);
    }

    public ArrayList<FurnitureItem> getInventory() {
        return Inventory;
    }

    public String getLocation() {
        return location;
    }

    public FurnitureItem findItemByID(int itemID) {
        for (FurnitureItem item : Inventory) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null; // Not found
    }    

    public void addItems(FurnitureItem item, int quantityToAdd) {
        FurnitureItem existing = findItemByID(item.getItemID());
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantityToAdd);
            } 
        else {
            Inventory.add(item);
            }
    }

    public void removeItems(int itemID, int quantityToRemove) {
        FurnitureItem item = findItemByID(itemID);
        if (item != null) {
            int newQuantity = item.getQuantity() - quantityToRemove;
            if (newQuantity <= 0) {
                Inventory.remove(item);
            } else {
                item.setQuantity(newQuantity);
            }
        }
    }
}
