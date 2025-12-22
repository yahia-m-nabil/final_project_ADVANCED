package org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.final_project.model.*;

public class ProductCardController {

    @FXML private ImageView itemImage;
    @FXML private Label itemName;
    @FXML private Label itemMaterial;
    @FXML private Label itemColor;
    @FXML private Label itemPrice;
    @FXML private Label discountLabel;
    @FXML private Label stockLabel;

    // Quantity Controls
    @FXML private Button btnMinus;
    @FXML private Button btnPlus;
    @FXML private Label quantityLabel;

    // Action Button
    @FXML private Button btnAddToCart;

    private int currentQuantity = 1;
    private int itemID;
    private boolean isProcessing = false; // Prevent double-click

    @FXML
    public void initialize() {
        // Increment Quantity (with stock limit check)
        btnPlus.setOnAction(e -> {
            FurnitureItem item = ECommerceSystem.getInstance().findItemById(itemID);
            if (item != null && currentQuantity < item.getQuantity()) {
                currentQuantity++;
                updateQuantityDisplay();
            } else if (item != null) {
                showAlert("Stock Limit", "Only " + item.getQuantity() + " items available in stock.");
            }
        });

        // Decrement Quantity (Minimum of 1)
        btnMinus.setOnAction(e -> {
            if (currentQuantity > 1) {
                currentQuantity--;
                updateQuantityDisplay();
            }
        });
    }

    private void updateQuantityDisplay() {
        quantityLabel.setText(String.valueOf(currentQuantity));
    }

    @FXML
    private void handleAddToCart() {
        // Prevent double-clicking
        if (isProcessing) {
            return;
        }
        isProcessing = true;

        try {
            ECommerceSystem system = ECommerceSystem.getInstance();
            User currentUser = system.getCurrentUser();

            if (currentUser == null) {
                showAlert("Login Required", "Please log in to add items to cart.");
                return;
            }

            FurnitureItem item = system.findItemById(itemID);
            if (item == null) {
                showAlert("Error", "Item not found in inventory.");
                return;
            }

            // Validate stock availability
            if (currentQuantity > item.getQuantity()) {
                showAlert("Insufficient Stock",
                    "Only " + item.getQuantity() + " items available. You requested " + currentQuantity + ".");
                return;
            }

            // Check if user already has this item in wishlist (cart)
            FurnitureItem existingInCart = currentUser.findItemInWishlist(itemID);
            int existingQuantity = (existingInCart != null) ? existingInCart.getQuantity() : 0;
            int totalQuantity = existingQuantity + currentQuantity;

            // Validate total quantity doesn't exceed stock
            if (totalQuantity > item.getQuantity()) {
                if (existingQuantity > 0) {
                    showAlert("Insufficient Stock",
                        "You already have " + existingQuantity + " in cart. " +
                        "Only " + item.getQuantity() + " available in total. " +
                        "Cannot add " + currentQuantity + " more.");
                } else {
                    showAlert("Insufficient Stock",
                        "Only " + item.getQuantity() + " items available. You requested " + currentQuantity + ".");
                }
                return;
            }

            // Create a copy and add to cart
            FurnitureItem itemToAdd = item.createCopy(currentQuantity);
            currentUser.addToWishlist(itemToAdd);

            // Reset quantity to 1 after successful add
            currentQuantity = 1;
            updateQuantityDisplay();

            showAlert("Success", "Added " + itemToAdd.getQuantity() + "x " + itemToAdd.getName() + " to cart!");
            System.out.println("Added to cart: " + itemToAdd.getName() + " x" + itemToAdd.getQuantity());

        } catch (Exception e) {
            showAlert("Error", "Failed to add item to cart: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Re-enable button after processing
            isProcessing = false;
        }
    }

    public void setData(FurnitureItem furnitureItem) {
        this.itemID = furnitureItem.getItemID();

        itemName.setText(furnitureItem.getName());
        itemMaterial.setText(furnitureItem.getMaterial().toString());
        itemColor.setText(furnitureItem.getColor().toString());
        itemPrice.setText(String.format("$%d", furnitureItem.getPrice()));

        // Display stock availability
        int stock = furnitureItem.getQuantity();
        stockLabel.setText(stock + " in stock");
        if (stock == 0) {
            stockLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: red; -fx-font-weight: bold;");
            stockLabel.setText("Out of Stock");
        } else if (stock < 5) {
            stockLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: orange; -fx-font-weight: bold;");
        } else {
            stockLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");
        }

        // Show discount label with actual percentage if item has discount
        if (furnitureItem instanceof Discountable && furnitureItem.hasDiscount()) {
            int discountPercentage = furnitureItem.getDiscountPercentage();
            discountLabel.setText(discountPercentage + "% OFF");
            discountLabel.setVisible(true);
        } else {
            discountLabel.setVisible(false);
        }

        loadImage(furnitureItem);
    }

    private void loadImage(FurnitureItem item) {
        try {
            String color = item.getColor().toString();
            String material = item.getMaterial().toString();
            String type = item.getType();

            String colorFormatted = color.charAt(0) + color.substring(1).toLowerCase();
            String materialFormatted = material.charAt(0) + material.substring(1).toLowerCase();

            String extension = ".jpg";
            if (material.equals("PLASTIC") && type.equals("Chair")) {
                extension = ".jpeg";
            }

            String imageName = colorFormatted + materialFormatted + type + extension;
            String imagePath = "/org/example/final_project/images/" + imageName;

            Image image = new Image(getClass().getResourceAsStream(imagePath));
            itemImage.setImage(image);
        } catch (Exception e) {
            System.err.println("Failed to load image for item: " + item.getName() + " - " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

