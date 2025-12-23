package org.example.final_project.model;

import java.util.ArrayList;

public class ECommerceSystem {
    private static ECommerceSystem instance;
    
    private MembersList membersData;
    private WarehouseList warehouseData;
    private Member currentMember;  // Session: currently logged-in member
    private int discountPercentage;

    private ECommerceSystem() {
        this.membersData = new MembersList();
        this.warehouseData = new WarehouseList();
        this.currentMember = null;
        this.discountPercentage = 0;

        // --- 1. STARTUP MEMBERS ---
        Admin admin = new Admin(99, "System Admin", "admin@system.com");
        admin.setPassword("admin123");
        addMember(admin);

        User amr = addUser(1, "Amr Emad", "amr@gmail.com");
        amr.setPassword("123456");
        amr.setMoney(100000);

        Seller abdo = addSeller(2, "Abdo", "abdo@store.com");
        abdo.setPassword("123456");

        User karem = addUser(3, "El 7ag Karem 3awad", "karem@3awad.com");
        karem.setPassword("123456");

        // --- 2. STARTUP WAREHOUSES ---
        Warehouse cairoWH = new Warehouse("Cairo");
        warehouseData.addWarehouse(cairoWH);
        warehouseData.addWarehouse(new Warehouse("Alexandria"));

        // --- 3. STARTUP PRODUCTS (Admin Initializing Stock) ---
        // We create every combination of Chair and Table
        Materials[] materials = {Materials.WOOD, Materials.METAL, Materials.PLASTIC};
        Colors[] colors = {Colors.BROWN, Colors.WHITE, Colors.BLACK};

        int idCounter = 1000; // Starting ID for products

        for (Materials m : materials) {
            for (Colors c : colors) {
                // Create a Chair with this combination
                // Params: ID, Quantity, Material, Color
                Chair chair = new Chair(idCounter++, 10, m, c);
                cairoWH.addItems(chair,chair.getQuantity());

                // Create a Table with this combination
                Table table = new Table(idCounter++, 5, m, c);
                cairoWH.addItems(table,table.getQuantity());
            }
        }

        System.out.println("System initialized: Members added and 18 startup products stocked in Cairo.");
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

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
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

    // ======================== SESSION MANAGEMENT =====================

    public void setCurrentMember(Member member) {
        this.currentMember = member;
    }

    public Member getCurrentMember() {
        return currentMember;
    }

    public User getCurrentUser() {
        if (currentMember instanceof User) {
            return (User) currentMember;
        }
        return null;
    }


    public Seller getCurrentSeller() {
        if (currentMember instanceof Seller) {
            return (Seller) currentMember;
        }
        return null;
    }

    public Admin getCurrentAdmin() {
        if (currentMember instanceof Admin) {
            return (Admin) currentMember;
        }
        return null;
    }


    public boolean isLoggedIn() {
        return currentMember != null;
    }


    public void clearSession() {
        this.currentMember = null;
    }


    public String getCurrentMemberName() {
        return currentMember != null ? currentMember.getName() : "Guest";
    }

    public String getMemberType() {
        if (currentMember == null) return "Guest";
        if (currentMember instanceof Admin) return "Admin";
        if (currentMember instanceof Seller) return "Seller";
        if (currentMember instanceof User) return "User";
        return "Unknown";
    }

    // ======================== ITEM MANAGEMENT (FACADE) =====================

    /**
     * Finds a furniture item across all warehouses
     * Controllers should use this instead of accessing WarehouseList directly
     */
    public FurnitureItem findItemById(int itemID) {
        return warehouseData.findItemAcrossWarehouses(itemID);
    }

    /**
     * Finds which warehouse contains a specific item
     */
    public Warehouse findWarehouseWithItem(int itemID) {
        return warehouseData.findWarehouseWithItem(itemID);
    }

    public ArrayList<FurnitureItem> getAllItems() {
        ArrayList<FurnitureItem> allItems = new ArrayList<>();
        for (Warehouse warehouse : warehouseData.getAllWarehouses()) {
            allItems.addAll(warehouse.getInventory());
        }
        return allItems;
    }

}

