/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.org.example.final_project.model;

public class  Seller extends Member {

    private FurnitureItem[] sellableFurniture;
// Constructor
    public Seller(int id, String name, String email, FurnitureItem[] sellableFurniture) {
        super(id, name, email);
        this.sellableFurniture = sellableFurniture;
    }

    // Getter
    public FurnitureItem[] getSellableFurniture() {
        return sellableFurniture;
    }

    // Setter
    public void setSellableFurniture(FurnitureItem[] sellableFurniture) {
        this.sellableFurniture = sellableFurniture;
    }

  
    @Override
    public int CalculateMoney() {
        int total = 0;

        if (sellableFurniture != null) {
            for (FurnitureItem item : sellableFurniture) {
                if (item != null) {
                    total += item.getPrice();
                }
            }
        }
setMoney(total);
        return total;
    }}
    
       
    
