package main.java.org.example.final_project.model;

import java.util.ArrayList;

public class ECommerceSystem {
    private static ECommerceSystem instance;
    
    private final MembersList membersData;
    private final WarehouseList warehouseData;

    private ECommerceSystem() {
        this.membersData = new MembersList();
        this.warehouseData = new WarehouseList();
    }

    public static ECommerceSystem getInstance() {
        if (instance == null) {
            instance = new ECommerceSystem();
        }
        return instance;
    }

    public MembersList getMembersData() {
        return membersData;
    }

    public WarehouseList getWarehouseData() {
        return warehouseData;
    }

    public ArrayList<Member> getAllMembers() {
        return membersData.getAllMembers();
    }

    public ArrayList<Warehouse> getAllWarehouses() {
        return warehouseData.getAllWarehouses();
    }

    public Member findMemberById(int id) {
        return membersData.getMemberById(id);
    }

    public Warehouse findWarehouseByLocation(String location) {
        return warehouseData.getWarehouseByLocation(location);
    }

    public void addMember(Member member) {
        membersData.addMember(member);
    }

    public void removeMember(int id) {
        membersData.removeMember(id);
    }

    public void addWarehouse(Warehouse warehouse) {
        warehouseData.addWarehouse(warehouse);
    }

    public void removeWarehouse(String location) {
        warehouseData.removeWarehouse(location);
    }

    public User signupUser(String name, String email, String password, int id) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (findMemberById(id) != null) {
            throw new IllegalArgumentException("User with ID " + id + " already exists");
        }
        User user = new User(name, email, id);
        user.setPassword(password);
        addMember(user);
        return user;
    }

    public Seller signupSeller(String name, String email, String password, int id) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (findMemberById(id) != null) {
            throw new IllegalArgumentException("Seller with ID " + id + " already exists");
        }
        Seller seller = new Seller(id, name, email);
        seller.setPassword(password);
        addMember(seller);
        return seller;
    }

    public Member login(int id, String password) {
        if (password == null || password.trim().isEmpty()) {
            return null;
        }
        Member member = findMemberById(id);
        if (member != null && member.getPassword().equals(password)) {
            return member;
        }
        return null;
    }

    public Member findMemberByEmail(String email) {
        return membersData.getMemberByEmail(email);
    }
}
