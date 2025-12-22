package org.example.final_project.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

class FurnitureAppTest {

    private ECommerceSystem system;

    @BeforeEach
    void setUp() {
        // Access the singleton and clear session for clean testing
        system = ECommerceSystem.getInstance();
        system.clearSession();
    }

    @Test
    @DisplayName("1. TableStorage Pricing Logic Test")
    void testPricingCalculations() {
        // Testing a Wood Chair (Material Index 1, Wood)
        // According to your TableStorage: Base 15, Wood-Brown Multiplier 0.00
        Chair woodChair = new Chair(1001, 1, Materials.WOOD, Colors.BROWN);
        assertEquals(15, woodChair.getPrice(), "Wood Brown Chair should be base price 15");

        // Testing a Metal Chair (Material Index 2, Metal)
        // Base 20, Metal-Brown Multiplier 0.50 -> 20 * 1.5 = 30
        Chair metalChair = new Chair(1002, 1, Materials.METAL, Colors.BROWN);
        assertEquals(30, metalChair.getPrice(), "Metal Brown Chair should be 30 (20 + 50% multiplier)");
    }

    @Test
    @DisplayName("2. Seller Platform Fee Test")
    void testSellerEarnings() {
        Seller seller = new Seller(50, "Test Seller", "seller@test.com");

        // According to your Seller.java, PLATFORM_FEE_PERCENTAGE = 5
        int gross = 1000;
        int expectedNet = 950; // 1000 - 5%

        assertEquals(expectedNet, seller.getNetEarnings(gross), "Seller should receive 95% of gross after platform fee");
    }

    @Test
    @DisplayName("3. User Money & Exception Test")
    void testUserFinancials() {
        User user = new User("Buyer", "buyer@test.com", 60);
        user.setMoney(100);

        // Test successful deduction
        user.deductMoney(40);
        assertEquals(60, user.getMoney());

        // Test your custom InvalidAmountException (Insufficient funds)
        assertThrows(InvalidAmountException.class, () -> {
            user.deductMoney(100);
        }, "Should throw InvalidAmountException when balance is too low");
    }

    @Test
    @DisplayName("4. Order ID Generation Test")
    void testOrderSequencing() {
        Chair item = new Chair(2001, 1, Materials.PLASTIC, Colors.WHITE);
        FurnitureItem[] items = {item};

        Order order1 = new Order(1, items);
        Order order2 = new Order(1, items);

        // According to Order.java, IDs start at 1000 and increment
        assertTrue(order2.getOrderId() > order1.getOrderId(), "Order IDs should be sequential");
    }

    @Test
    @DisplayName("5. Authentication & Session Test")
    void testSystemLogin() {
        // Use the startup admin from your ECommerceSystem constructor (ID 99, Pass admin123)
        Member admin = system.login(99, "admin123");
        assertNotNull(admin, "Login should succeed with correct ID and password");

        system.setCurrentMember(admin);
        assertEquals("Admin", system.getMemberType());
        assertTrue(system.isLoggedIn());
    }

    @Test
    @DisplayName("6. Warehouse Logic & Sorting")
    void testWarehouseSorting() {
        Warehouse wh = new Warehouse("Test Hub");
        Chair c1 = new Chair(300, 10, Materials.WOOD, Colors.WHITE);
        Chair c2 = new Chair(100, 5, Materials.WOOD, Colors.WHITE);

        wh.addItems(c1,1);
        wh.addItems(c2,1);

        // Test your sortInventoryByID (uses Comparable implementation in FurnitureItem)
        wh.sortInventoryByID();
        assertEquals(100, wh.getInventory().get(0).getItemID(), "Item 100 should be first after sort");
    }
}