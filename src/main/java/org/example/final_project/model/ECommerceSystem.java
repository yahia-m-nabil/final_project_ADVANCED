package org.example.final_project.model;

import java.util.ArrayList;

public class ECommerceSystem {
    private static ECommerceSystem instance;
    
    private  MembersList membersData;
    private  WarehouseList warehouseData;

    private ECommerceSystem() {
        this.membersData = new MembersList();
        this.warehouseData = new WarehouseList();


        // --- STARTUP DATA INITIALIZATION ---

        // 1. Add Admin (Using the ID, Name, Email)
        Admin admin = new Admin(99, "System Admin", "admin@system.com");
        admin.setPassword("admin123");
        addMember(admin);

        // 2. Add Users (Customers)
        // Note: Your User constructor takes (Name, Email, ID)
        User amr = addUser(1, "Amr Emad", "amr@gmail.com");
        amr.setPassword("123456");

        // 3. Add Sellers
        // Note: Your Seller constructor takes (ID, Name, Email)
        Seller abdo = addSeller(2, "Abdo", "a");
        abdo.setPassword("123456");

        Seller karem = addSeller(3, "El 7ag Karem 3awad", "karem@3awad.com");
        karem.setPassword("123456");

        // 4. Add Startup Warehouses
        warehouseData.addWarehouse(new Warehouse("Cairo"));
        warehouseData.addWarehouse(new Warehouse("Alexandria"));

        // ------------------------------------

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

    public User addUser(int id, String name, String email) {

        if (this.findMemberById(id) != null) {
            throw new IllegalArgumentException("Member with ID " + id + " already exists");
        }
        User user = new User(name, email, id);
        this.addMember(user); // Use the local instance
        return user;
    }

    public Seller addSeller(int id, String name, String email) {

        if (this.findMemberById(id) != null) {
            throw new IllegalArgumentException("Seller with ID " + id + " already exists");
        }

        Seller seller = new Seller(id, name, email);

        this.addMember(seller);

        return seller;
    }

}

