package main.java.org.example.final_project.model;

public class Chair extends FurnitureItem {

    public Chair(int itemID, int quantity, Materials material, Colors color) {
        super(itemID, quantity, material, color);
        this.setPrice(calculatePrice(TableStorage.getInstance()));
    }

    @Override
    public int calculatePrice(TableStorage table) {
       int initalprice = table.getMaterialPrice(0, this.getMaterial());
       double percentage = table.getColorPrice(0, this.getColor());
         return  (int) (initalprice + (initalprice * percentage));
    }

//    @Override
//    public void displayInfo() {
//        System.out.println("Chair ID: " + this.getItemID() +
//                ", Material: " + this.getMaterial() +
//                ", Color: " + this.getColor() +
//                ", Quantity: " + this.getQuantity() +
//                ", Price: $" + this.getPrice());
//    }
}
