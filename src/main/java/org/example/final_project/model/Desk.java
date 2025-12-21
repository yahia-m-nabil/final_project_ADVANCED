package org.example.final_project.model;

public class Desk extends FurnitureItem {

    /* ======================== CONSTRUCTOR ===================== */

    public Desk(int itemID, int quantity, Materials material, Colors color) {
        super(itemID, quantity, material, color);
        int calculatedPrice = calculatePrice(TableStorage.getInstance());
        this.setPrice(calculatedPrice);
    }

    /* ======================== PRICE CALCULATION ===================== */

    @Override
    public int calculatePrice(TableStorage table) {
        int initialPrice = table.getMaterialPrice(2, this.getMaterial());
        double colorMultiplier = table.getColorMultiplier(this.getMaterial(), this.getColor());
        return (int) (initialPrice * (1 + colorMultiplier));
    }

    /* ======================== STRING REPRESENTATION ===================== */

    @Override
    public String toString() {
        return "Desk{" +
                "ID=" + getItemID() +
                ", Material=" + getMaterial() +
                ", Color=" + getColor() +
                ", Quantity=" + getQuantity() +
                ", Price=$" + getPrice() +
                '}';
    }

    /* ======================== COPY METHOD ===================== */

    @Override
    public Desk createCopy(int newQuantity) {
        return new Desk(this.getItemID(), newQuantity, this.getMaterial(), this.getColor());
    }
}

