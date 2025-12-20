package main.java.org.example.final_project.model;

public final class TableStorage {
    private static final int[][] itemMaterial = {{10,15,20},{15,35,50},{20,55,100}};
    private static final double[][] materialColor = {{0.05,0,0.1},{0,0.15,0.2},{0.5,0.25,0}};
    private static final int[][] idtableMaterialColor = {{0,1,2},{3,4,5},{6,7,8}}; //0-PlasticBrown,1-PlasticWhite,2-PlasticBlack,3-WoodBrown,4-WoodWhite,5-WoodBlack,6-MetalBrown,7-MetalWhite,8-MetalBlack
    private static final int[] idtableItems = {0,1,2}; //0-Chair,1-Table,2-Desk
    private static final TableStorage instance = new TableStorage();

    private TableStorage() {}
    public static TableStorage getInstance() {
        return instance;
    }

    public  int[] getIdtableItems() {
        return idtableItems;
    }

    public  int[][] getIdtableMaterialColor() {
        return idtableMaterialColor;
    }

    public  int getnumberOfItems() {
        return (idtableItems.length*(idtableMaterialColor.length*idtableMaterialColor[0].length));
    }

    public  int getMaterialPrice(int row , Materials col) {
        return itemMaterial[row][col.ordinal()];
    }
    public  double getColorPrice(int row , Colors col) {
        return materialColor[row][col.ordinal()];
    }
}
