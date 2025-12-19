package org.example.final_project.model;

import java.util.ArrayList;

public class Admin extends Person {
    private MembersList MembersData;
    private WarehouseList WarehouseData;
    //a7abala7mar

    public Admin(int id, String name, String email, MembersList membersData, WarehouseList warehousedata) {
        super(id, name, email);
        this.MembersData = membersData;
        this.WarehouseData = warehousedata;
    }

//    public void DisplayAllUsers(){
//        ArrayList<Member> allMembers = MembersData.getAllMembers();
//        for (Member member : allMembers) {
//            member.displayInfo();
//        }
//    }

//    public void DisplayAllSellers(){
//        ArrayList<Member> allMembers = MembersData.getAllMembers();
//        for (Member member : allMembers) {
//            if (member instanceof Seller){
//                member.displayInfo();
//            }
//        }
//    }

    public void FindMemberById(int id) {
        Member member = MembersData.getMember(id);
        if (member != null) {
            //member.displayInfo();
        }
    }

    public void DisplayAllItems(){

    }
}

