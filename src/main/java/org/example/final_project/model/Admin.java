package main.java.org.example.final_project.model;

import java.util.ArrayList;

public class Admin extends Person {
    private MembersList MembersData;
    private WarehouseList WarehouseData;


    public Admin(int id, String name, String email, MembersList membersData, WarehouseList warehousedata) {
        super(id, name, email);
        this.MembersData = membersData;
        this.WarehouseData = warehousedata;
    }

    public void DisplayAllUsers(){
        ArrayList<Member> allMembers = MembersData.getAllMembers();
        for (Member member : allMembers) {
          //  member.displayInfo();
        }
    }

    public void DisplayAllSellers(){
        ArrayList<Member> allMembers = MembersData.getAllMembers();
        for (Member member : allMembers) {
            if (member instanceof Seller){
              //  member.displayInfo();
            }
        }
    }

    public void FindMemberById(int id) {
        Member member = MembersData.getMember(id);
        if (member != null) {
            //member.displayInfo();
        }
    }

    public void DisplayAllItems() {
//        for (FurnitureItem item : WarehouseData.getAllItems()) {
//            //item.displayInfo();
//        }
    }

    public void removeMember(int id){
        MembersData.removeMember(id);
    }

    public void removeWarehouse(String location){
        WarehouseData.removeWarehouse(location);
    }

    public void addMember(Member member){
        MembersData.addMember(member);
}
    public void addWarehouse(Warehouse warehouse){
        WarehouseData.addWarehouse(warehouse);
    }

    public void applyDiscount(String location, double discountPercentage){
//        Warehouse warehouse = WarehouseData.getWarehouse(location);
//        if (warehouse != null) {
//            ArrayList<FurnitureItem> items = warehouse.getAllItems();
//            for (FurnitureItem item : items) {
//                double originalPrice = item.getPrice();
//                double discountedPrice = originalPrice * (1 - discountPercentage / 100);
//                item.setPrice(discountedPrice);
//            }
//        }
    }
}

