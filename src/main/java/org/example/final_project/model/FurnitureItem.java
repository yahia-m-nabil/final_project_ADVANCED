package org.example.final_project.model;

public abstract class FurnitureItem {

    private int itemID;
    private int quantity;
    private int price;
    private Materials material;
    private Colors color;

    public FurnitureItem(int itemID, int quantity, Materials material, Colors color) {
        this.color = color;
        this.material = material;
        this.itemID = itemID;
        this.quantity = quantity;
    }

    public abstract void displayInfo();

    public abstract int calculatePrice(PriceTable table);

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Colors getColor() {
        return color;
    }

    public int getItemID() {
        return itemID;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Materials getMaterial() {
        return material;
    }
}

