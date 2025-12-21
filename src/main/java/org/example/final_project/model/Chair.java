package org.example.final_project.model;

public class Chair extends FurnitureItem implements Discountable {

    /* ======================== CONSTRUCTOR ===================== */

    public Chair(int itemID, int quantity, Materials material, Colors color) {
        super(itemID, quantity, material, color);
        int calculatedPrice = calculatePrice(TableStorage.getInstance());
        this.setPrice(calculatedPrice);
    }

    /* ======================== PRICE CALCULATION ===================== */

    @Override
    public int calculatePrice(TableStorage table) {
        int initialPrice = table.getMaterialPrice(0, this.getMaterial());
        double colorMultiplier = table.getColorMultiplier(this.getMaterial(), this.getColor());
        return (int) (initialPrice * (1 + colorMultiplier));
    }

    /* ======================== DISCOUNT MANAGEMENT ===================== */

    @Override
    public void AddDiscount(double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 1) {
            throw new IllegalArgumentException("Discount must be between 0 and 1");
        }
        int discountedPrice = (int) (this.getPrice() * (1 - discountPercentage));
        this.setPrice(discountedPrice);
    }

    /* ======================== STRING REPRESENTATION ===================== */

    @Override
    public String toString() {
        return "Chair{" +
                "ID=" + getItemID() +
                ", Material=" + getMaterial() +
                ", Color=" + getColor() +
                ", Quantity=" + getQuantity() +
                ", Price=$" + getPrice() +
                '}';
    }

    /* ======================== COPY METHOD ===================== */

    @Override
    public Chair createCopy(int newQuantity) {
        return new Chair(this.getItemID(), newQuantity, this.getMaterial(), this.getColor());
    }
}
