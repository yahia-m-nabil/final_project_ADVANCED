package org.example.final_project.model;

public abstract class Member extends Person {

    /* ======================== FIELDS ===================== */

    private static int totalIdCount = 0;
    private int money;

    /* ======================== CONSTRUCTOR ===================== */

    public Member(int id, String name, String email) {
        super(id, name, email);
        this.money = 0;
        totalIdCount++;
    }

    /* ======================== GETTERS ===================== */

    public int getMoney() {
        return money;
    }

    public static int getTotalIdCount() {
        return totalIdCount;
    }

    /* ======================== SETTERS ===================== */

    public void setMoney(int money) {
        if (money < 0) {
            throw new IllegalArgumentException("Money cannot be negative");
        }
        this.money = money;
    }

    /* ======================== MONEY MANAGEMENT ===================== */

    public void addMoney(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to add must be positive");
        }
        this.money += amount;
    }

    public void deductMoney(int amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to deduct must be positive");
        }
        if (this.money < amount) {
            throw new InvalidAmountException("Insufficient funds. Available: $" + this.money + ", Required: $" + amount);
        }
        this.money -= amount;
    }

    /* ======================== MEMBER COUNT MANAGEMENT ===================== */

    public static void decrementMemberCount() {
        if (totalIdCount > 0) {
            totalIdCount--;
        }
    }

    /* ======================== ABSTRACT METHODS ===================== */

    public abstract int calculateMoney();

    /* ======================== OBJECT METHODS ===================== */

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "ID=" + getMemberId() +
                ", Name='" + getName() + '\'' +
                ", Email='" + getEmail() + '\'' +
                ", Money=$" + money +
                '}';
    }
}

