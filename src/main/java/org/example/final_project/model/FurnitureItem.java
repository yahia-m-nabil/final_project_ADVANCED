package main.java.org.example.final_project.model;

public abstract class FurnitureItem implements Comparable<FurnitureItem> {

    /* ======================== FIELDS ===================== */

    private final int itemID;
    private int quantity;
    private int price;
    private final Materials material;
    private final Colors color;

    /* ======================== CONSTRUCTOR ===================== */

    public FurnitureItem(int itemID, int quantity, Materials material, Colors color) {
        if (itemID <= 0) {
            throw new IllegalArgumentException("Item ID must be positive");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        this.itemID = itemID;
        this.quantity = quantity;
        this.material = material;
        this.color = color;
        this.price = 0;
    }

    /* ======================== ABSTRACT METHODS ===================== */

    public abstract int calculatePrice(TableStorage table);

    public abstract FurnitureItem createCopy(int newQuantity);

    /* ======================== DISCOUNT MANAGEMENT ===================== */

    protected void applyDiscount(double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 1) {
            throw new IllegalArgumentException("Discount must be between 0 and 1");
        }
        int discountedPrice = (int) (this.price * (1 - discountPercentage));
        this.setPrice(discountedPrice);
    }

    /* ======================== GETTERS ===================== */

    public int getItemID() {
        return itemID;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public Materials getMaterial() {
        return material;
    }

    public Colors getColor() {
        return color;
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }

    public int getTotalPrice() {
        return price * quantity;
    }

    public String getFormattedPrice() {
        return "$" + price;
    }

    public String getFormattedTotalPrice() {
        return "$" + getTotalPrice();
    }

    /* ======================== SETTERS ===================== */

    public void setPrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    /* ======================== QUANTITY MANAGEMENT ===================== */

    public void addQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to add must be positive");
        }
        this.quantity += amount;
    }

    public void reduceQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to reduce must be positive");
        }
        if (this.quantity < amount) {
            throw new IllegalArgumentException("Not enough quantity available");
        }
        this.quantity -= amount;
    }

    /* ======================== STOCK CHECKING ===================== */

    public boolean isInStock() {
        return quantity > 0;
    }

    public boolean hasEnoughStock(int requestedQuantity) {
        return quantity >= requestedQuantity;
    }

    public String getStockStatus() {
        if (quantity == 0) {
            return "Out of Stock";
        } else {
            return "In Stock (" + quantity + " available)";
        }
    }

    /* ======================== COMPARABLE IMPLEMENTATION ===================== */

    @Override
    public int compareTo(FurnitureItem other) {
        return Integer.compare(this.itemID, other.itemID);
    }

    /* ======================== OBJECT METHODS ===================== */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FurnitureItem that = (FurnitureItem) obj;
        return itemID == that.itemID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(itemID);
    }

    @Override
    public String toString() {
        return getType() + "{" +
                "ID=" + itemID +
                ", Material=" + material +
                ", Color=" + color +
                ", Quantity=" + quantity +
                ", Price=$" + price +
                ", Total=$" + getTotalPrice() +
                '}';
    }
}

