package org.example.final_project.model;

public final class PriceTable {
    private static final int[][] itemMaterial = {{10,15,20},{15,35,50},{20,55,100}};
    private static final double[][] materialColor = {{0.05,0,0.1},{0,0.15,0.2},{0.5,0.25,0}};

    public static int getMaterialPrice(int row , Materials col) {
        return itemMaterial[row][col.ordinal()];
    }
    public static double getColor(int row , Colors col) {
        return materialColor[row][col.ordinal()];
    }
}
