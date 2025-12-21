package org.example.final_project.model;

import java.util.ArrayList;

public class Admin extends Member {

    public Admin(int id, String name, String email) {
        super(id, name, email);
    }

    /* ======================== QUERY METHODS - GET ALL ===================== */

    public ArrayList<Member> getAllMembers() {
        return ECommerceSystem.getInstance().getAllMembers();
    }

    public ArrayList<User> getAllUsers() {
        return ECommerceSystem.getInstance().getMembersData().getAllUsers();
    }

    public ArrayList<Seller> getAllSellers() {
        return ECommerceSystem.getInstance().getMembersData().getAllSellers();
    }

    public ArrayList<Warehouse> getAllWarehouses() {
        return ECommerceSystem.getInstance().getAllWarehouses();
    }

    /* ======================== SEARCH METHODS - FIND BY ID/LOCATION ===================== */

    public Member findMemberById(int id) {
        return ECommerceSystem.getInstance().findMemberById(id);
    }

    public User getUserById(int id) {
        Member member = ECommerceSystem.getInstance().findMemberById(id);
        if (member instanceof User) {
            return (User) member;
        }
        return null;
    }

    public Seller getSellerById(int id) {
        Member member = ECommerceSystem.getInstance().findMemberById(id);
        if (member instanceof Seller) {
            return (Seller) member;
        }
        return null;
    }

    public Warehouse findWarehouseByLocation(String location) {
        return ECommerceSystem.getInstance().findWarehouseByLocation(location);
    }

    /* ======================== USER MANAGEMENT ===================== */

    public User addUser(int id, String name, String email) {
        if (ECommerceSystem.getInstance().findMemberById(id) != null) {
            throw new IllegalArgumentException("Member with ID " + id + " already exists");
        }
        User user = new User(name, email, id);
        ECommerceSystem.getInstance().addMember(user);
        return user;
    }

    public ArrayList<Order> getUserOrderHistory(int userId) {
        User user = getUserById(userId);
        if (user != null) {
            return user.getOrderHistory();
        }
        return new ArrayList<>();
    }

    /* ======================== SELLER MANAGEMENT ===================== */

    public Seller addSeller(int id, String name, String email) {
        if (ECommerceSystem.getInstance().findMemberById(id) != null) {
            throw new IllegalArgumentException("Member with ID " + id + " already exists");
        }
        Seller seller = new Seller(id, name, email);
        ECommerceSystem.getInstance().addMember(seller);
        return seller;
    }

    public ArrayList<Order> getSellerSalesHistory(int sellerId) {
        Seller seller = getSellerById(sellerId);
        if (seller != null) {
            return seller.getSalesHistory();
        }
        return new ArrayList<>();
    }

    /* ======================== MEMBER REMOVAL ===================== */

    public boolean removeMember(int id) {
        Member member = ECommerceSystem.getInstance().findMemberById(id);
        if (member == null) {
            return false;
        }
        Member.decrementMemberCount();
        ECommerceSystem.getInstance().removeMember(id);
        return true;
    }

    /* ======================== WAREHOUSE MANAGEMENT ===================== */

    public Warehouse addWarehouse(String location) {
        if (ECommerceSystem.getInstance().findWarehouseByLocation(location) != null) {
            throw new IllegalArgumentException("Warehouse with location '" + location + "' already exists");
        }
        Warehouse warehouse = new Warehouse(location);
        ECommerceSystem.getInstance().addWarehouse(warehouse);
        return warehouse;
    }

    public boolean removeWarehouse(String location) {
        Warehouse warehouse = ECommerceSystem.getInstance().findWarehouseByLocation(location);
        if (warehouse == null) {
            return false;
        }
        ECommerceSystem.getInstance().removeWarehouse(location);
        return true;
    }

    /* ======================== DISCOUNT MANAGEMENT ===================== */

    public int applyDiscount(String location, double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        
        Warehouse warehouse = ECommerceSystem.getInstance().findWarehouseByLocation(location);
        if (warehouse == null) {
            return -1;
        }
        
        int itemsDiscounted = 0;
        ArrayList<FurnitureItem> items = warehouse.getInventory();
        for (FurnitureItem item : items) {
            if (item instanceof Discountable discountable) {
                discountable.AddDiscount(discountPercentage);
                itemsDiscounted++;
            }
        }
        return itemsDiscounted;
    }

    public int applyGlobalDiscount(double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        
        int totalDiscounted = 0;
        for (Warehouse warehouse : ECommerceSystem.getInstance().getAllWarehouses()) {
            ArrayList<FurnitureItem> items = warehouse.getInventory();
            for (FurnitureItem item : items) {
                if (item instanceof Discountable discountable) {
                    discountable.AddDiscount(discountPercentage);
                    totalDiscounted++;
                }
            }
        }
        return totalDiscounted;
    }

    /* ======================== INVENTORY MANAGEMENT ===================== */

    public ArrayList<FurnitureItem> getAllItemsInWarehouse(String location) {
        Warehouse warehouse = ECommerceSystem.getInstance().findWarehouseByLocation(location);
        if (warehouse != null) {
            return warehouse.getInventory();
        }
        return new ArrayList<>();
    }

    public ArrayList<FurnitureItem> getAllItemsAcrossWarehouses() {
        ArrayList<FurnitureItem> allItems = new ArrayList<>();
        for (Warehouse warehouse : ECommerceSystem.getInstance().getAllWarehouses()) {
            allItems.addAll(warehouse.getInventory());
        }
        return allItems;
    }

    public FurnitureItem findItemInWarehouse(String location, int itemID) {
        Warehouse warehouse = ECommerceSystem.getInstance().findWarehouseByLocation(location);
        if (warehouse != null) {
            return warehouse.findItemByID(itemID);
        }
        return null;
    }

    /* ======================== STATISTICS & ANALYTICS ===================== */

    public int getTotalUsersCount() {
        return ECommerceSystem.getInstance().getMembersData().getUserCount();
    }

    public int getTotalSellersCount() {
        return ECommerceSystem.getInstance().getMembersData().getSellerCount();
    }

    public int getTotalWarehousesCount() {
        return ECommerceSystem.getInstance().getWarehouseData().getWarehouseCount();
    }

    public int getTotalItemsCount() {
        return ECommerceSystem.getInstance().getWarehouseData().getTotalInventoryQuantity();
    }

    public int getTotalInventoryValue() {
        return ECommerceSystem.getInstance().getWarehouseData().getTotalInventoryValue();
    }

    public int calculateMoney() {
        return 0; // Admin does not have personal earnings
    }
}