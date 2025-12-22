package org.example.final_project.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.final_project.model.Colors;
import org.example.final_project.model.Materials;

public class AddProductDialogController {

    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<Materials> materialComboBox;
    @FXML private ComboBox<Colors> colorComboBox;
    @FXML private TextField quantityField;

    @FXML
    public void initialize() {
        typeComboBox.getItems().addAll("Chair", "Table", "Desk");
        materialComboBox.getItems().addAll(Materials.values());
        colorComboBox.getItems().addAll(Colors.values());
    }

    public int generateItemId() {
        // Generate ID based on type, material, color combination
        // SELLER ID RANGE: 2000-2026 (to avoid conflict with warehouse items 1000-1017)
        // Format: Base 2000 + (type*9 + material*3 + color)
        int typeIndex = typeComboBox.getSelectionModel().getSelectedIndex();
        int materialIndex = getMaterial().ordinal();
        int colorIndex = getColor().ordinal();

        // Calculate unique ID: base 2000 + (type*9 + material*3 + color)
        // This ensures seller items have different IDs than warehouse items
        return 2000 + (typeIndex * 9 + materialIndex * 3 + colorIndex);
    }

    public String getType() {
        return typeComboBox.getValue();
    }

    public Materials getMaterial() {
        return materialComboBox.getValue();
    }

    public Colors getColor() {
        return colorComboBox.getValue();
    }

    public int getQuantity() throws NumberFormatException {
        return Integer.parseInt(quantityField.getText().trim());
    }

    public boolean isValid() {
        return getType() != null && getMaterial() != null && getColor() != null;
    }
}

