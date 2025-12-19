package org.example.final_project.model;

import java.util.ArrayList;

public class Seller extends Member {

    private ArrayList<FurnitureItem> itemsForSale ;


    /* ======================== constructor  functions ===================== */
    public Seller(String name, String email , int id) {
        super(id ,name, email);
        this.itemsForSale = new ArrayList<>();
    }
    /* ======================== setters and getter  functions ===================== */
    public void addItemForSale(FurnitureItem item) {
        itemsForSale.add(item);
    }

    public ArrayList<FurnitureItem> getItemsForSale() {
        return itemsForSale;
    }
    /* ======================== money  functions ===================== */
    @Override
    public int CalculateMoney() {
        int total = 0;
        for (FurnitureItem item : itemsForSale) {
            total += item.getPrice();
        }
        return total;
    }
    /* ======================== warehouse functions ===================== */

}

