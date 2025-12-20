package main.java.org.example.final_project.model;

public class Table extends FurnitureItem implements Discountable {
    public Table(int itemID, int quantity, Materials material, Colors color) {
        super(itemID, quantity, material, color);
        this.setPrice(calculatePrice(TableStorage.getInstance()));
    }

    @Override
    public int calculatePrice(TableStorage table) {
        int malpractice = table.getMaterialPrice(1, this.getMaterial());
        double percentage = table.getColorPrice(1, this.getColor());
        return  (int) (malpractice + (malpractice * percentage));
    }

    @Override
    public void AddDiscount(double discountPercentage) {
        this.setPrice((int)((this.getPrice())+(this.getPrice()*discountPercentage)));
    }


//    @Override
//    public void displayInfo() {
//        System.out.println("Table ID: " + this.getItemID() +
//                ", Material: " + this.getMaterial() +
//                ", Color: " + this.getColor() +
//                ", Quantity: " + this.getQuantity() +
//                ", Price: $" + this.getPrice());
//    }
}

