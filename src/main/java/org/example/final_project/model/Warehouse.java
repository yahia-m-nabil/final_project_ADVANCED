package org.example.final_project.model;

import java.util.ArrayList;
import java.util.Collections;

public class Warehouse {
    private String location;
    private ArrayList<FurnitureItem> Inventory;
    //private final int[][] id_inventory = {{0,0},{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7},{8,8},{9,10},{10,11},{11,12},{12,13},{13,14},{14,15},{15,16},{16,17},{17,18},{18,20},{19,21},{20,22},{21,23},{22,24},{23,25},{24,26},{25,27},{26,28}};
    private final int MAX_CAPACITY = 27;

    Warehouse(String location){
        this.location = location;
        this.Inventory = new ArrayList<FurnitureItem>(MAX_CAPACITY);
    }

    public String getLocation() {
        return location;
    }

    public void additems(ArrayList<FurnitureItem> item){
        Collections.sort(item);
        Collections.sort(Inventory);
        for(int i = 0 ; i < item.size() ; i++){
            int id_added = item.get(i).getItemID();

        }
    }

    public void removeitems(ArrayList<FurnitureItem> item){

    }
}
