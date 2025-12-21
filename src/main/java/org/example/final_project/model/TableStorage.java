package org.example.final_project.model;

public final class TableStorage {

    /* ======================== PRICING TABLES ===================== */
    
    // Base prices for each furniture type and material combination
    // Rows: Chair(0), Table(1), Desk(2)
    // Columns: PLASTIC(0), WOOD(1), METAL(2)
    private static final int[][] ITEM_MATERIAL_PRICES = {
        {10, 15, 20},   // Chair prices
        {15, 35, 50},   // Table prices
        {20, 55, 100}   // Desk prices
    };
    
    // Color multipliers for each material
    // Rows: PLASTIC(0), WOOD(1), METAL(2)
    // Columns: BROWN(0), WHITE(1), BLACK(2)
    private static final double[][] MATERIAL_COLOR_MULTIPLIERS = {
        {0.05, 0.00, 0.10},   // Plastic color multipliers
        {0.00, 0.15, 0.20},   // Wood color multipliers
        {0.50, 0.25, 0.00}    // Metal color multipliers
    };

    /* ======================== SINGLETON INSTANCE ===================== */

    private static final TableStorage instance = new TableStorage();

    private TableStorage() {}

    public static TableStorage getInstance() {
        return instance;
    }

    /* ======================== PRICE CALCULATION ===================== */

    public int getMaterialPrice(int itemType, Materials material) {
        validateItemType(itemType);
        validateMaterial(material);
        return ITEM_MATERIAL_PRICES[itemType][material.ordinal()];
    }

    public double getColorMultiplier(Materials material, Colors color) {
        validateMaterial(material);
        validateColor(color);
        return MATERIAL_COLOR_MULTIPLIERS[material.ordinal()][color.ordinal()];
    }

    public int calculateFinalPrice(int itemType, Materials material, Colors color) {
        int basePrice = getMaterialPrice(itemType, material);
        double colorMultiplier = getColorMultiplier(material, color);
        return (int) (basePrice * (1 + colorMultiplier));
    }

    /* ======================== VALIDATION ===================== */

    private void validateItemType(int itemType) {
        if (itemType < 0 || itemType > 2) {
            throw new IllegalArgumentException("Invalid item type: " + itemType + ". Must be 0 (Chair), 1 (Table), or 2 (Desk)");
        }
    }

    private void validateMaterial(Materials material) {
        if (material == null) {
            throw new IllegalArgumentException("Material cannot be null");
        }
    }

    private void validateColor(Colors color) {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
    }

    /* ======================== UTILITY METHODS ===================== */

    public int getTotalCombinations() {
        return 3 * 3 * 3; // 3 item types × 3 materials × 3 colors = 27 combinations
    }

    /* ======================== OBJECT METHODS ===================== */

    @Override
    public String toString() {
        return "TableStorage{Singleton Instance, Total Combinations=" + getTotalCombinations() + "}";
    }
}
