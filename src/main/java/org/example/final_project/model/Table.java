package org.example.final_project.model;

public class Table extends FurnitureItem implements Discountable {

    /* ======================== CONSTRUCTOR ===================== */

    public Table(int itemID, int quantity, Materials material, Colors color) {
        super(itemID, quantity, material, color);
        int calculatedPrice = calculatePrice(TableStorage.getInstance());
        this.setPrice(calculatedPrice);
    }

    /* ======================== PRICE CALCULATION ===================== */

    @Override
    public int calculatePrice(TableStorage table) {
        int initialPrice = table.getMaterialPrice(1, this.getMaterial());
        double colorMultiplier = table.getColorMultiplier(this.getMaterial(), this.getColor());
        return (int) (initialPrice * (1 + colorMultiplier));
    }

    /* ======================== DISCOUNT MANAGEMENT ===================== */

    @Override
    public void AddDiscount(double discountPercentage) {
        applyDiscount(discountPercentage);
    }

    /* ======================== STRING REPRESENTATION ===================== */

    @Override
    public String toString() {
        return "Table{" +
                "ID=" + getItemID() +
                ", Material=" + getMaterial() +
                ", Color=" + getColor() +
                ", Quantity=" + getQuantity() +
                ", Price=$" + getPrice() +
                '}';
    }

    /* ======================== COPY METHOD ===================== */

    @Override
    public Table createCopy(int newQuantity) {
        return new Table(this.getItemID(), newQuantity, this.getMaterial(), this.getColor());
    }
}

