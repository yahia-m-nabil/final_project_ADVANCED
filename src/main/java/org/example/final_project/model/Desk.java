package org.example.final_project.model;

public class Desk extends FurnitureItem {
    public Desk(int itemID, int quantity, Materials material, Colors color) {
        super(itemID, quantity, material, color);
        this.setPrice(calculatePrice(TableStorage.getInstance()));
    }

    @Override
    public int calculatePrice(TableStorage table) {
        int initalprice = table.getMaterialPrice(2, this.getMaterial());
        double percentage = table.getColorPrice(2, this.getColor());

        return  (int) (initalprice + (initalprice * percentage));
    }

//    @Override
//    public void displayInfo() {
//        System.out.println("Desk ID: " + this.getItemID() +
//                ", Material: " + this.getMaterial() +
//                ", Color: " + this.getColor() +
//                ", Quantity: " + this.getQuantity() +
//                ", Price: $" + this.getPrice());
//    }
}

