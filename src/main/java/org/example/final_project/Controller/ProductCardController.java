package org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ProductCardController {

    @FXML private ImageView itemImage;
    @FXML private Label itemName;
    @FXML private Label itemMaterial;
    @FXML private Label itemColor;
    @FXML private Label itemPrice;
    @FXML private Label discountLabel;

    // Quantity Controls
    @FXML private Button btnMinus;
    @FXML private Button btnPlus;
    @FXML private Label quantityLabel;

    private int currentQuantity = 1;
    // TODO: Reference to the actual FurnitureItem object this card represents
    // private FurnitureItem item;

    /**
     * Initializes the card and sets up the plus/minus button logic.
     */
    @FXML
    public void initialize() {
        // Increment Quantity
        btnPlus.setOnAction(event -> {
            currentQuantity++;
            updateQuantityDisplay();
        });

        // Decrement Quantity (Minimum of 1)
        btnMinus.setOnAction(event -> {
            if (currentQuantity > 1) {
                currentQuantity--;
                updateQuantityDisplay();
            }
        });
    }

    /**
     * Updates the label in the GUI
     */
    private void updateQuantityDisplay() {
        quantityLabel.setText(String.valueOf(currentQuantity));
    }

    /**
     * Logic for the "Add to Cart" button
     */
    @FXML
    private void handleAddToCart() {
        System.out.println("Adding " + currentQuantity + " of " + itemName.getText() + " to cart.");
        // TODO: Call currentCustomer.getWishlist().add(item);
        // Note: In your logic, Cart and Wishlist share similar list behaviors.
    }

    /**
     * Logic for the "Add to Wishlist" button
     */
    @FXML
    private void handleAddToWishlist() {
        System.out.println("Item saved to wishlist: " + itemName.getText());
        // TODO: Persist this item to the database/MembersList for the user
    }

    /**
     * Method to be called by the StoreController to set up the card's data
     */
    public void setData(String name, String material, String color, double price) {
        itemName.setText(name);
        itemMaterial.setText(material);
        itemColor.setText(color);
        itemPrice.setText(String.format("$%.2f", price));
        // TODO: load image using itemImage.setImage(new Image(path));
    }
}
