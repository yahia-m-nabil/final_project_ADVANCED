package main.java.org.example.final_project.model;

public abstract class Person {

    /* ======================== FIELDS ===================== */

    private final int memberId;
    private final String name;
    private final String email;
    private String password;

    /* ======================== CONSTRUCTOR ===================== */

    public Person(int id, String name, String email) {
        if (id <= 0) {
            throw new IllegalArgumentException("Member ID must be positive");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        this.memberId = id;
        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.password = "";
    }

    /* ======================== GETTERS ===================== */

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    /* ======================== SETTERS ===================== */

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        this.password = password;
    }

    /* ======================== VALIDATION ===================== */

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    /* ======================== OBJECT METHODS ===================== */

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "ID=" + memberId +
                ", Name='" + name + '\'' +
                ", Email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return memberId == person.memberId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(memberId);
    }
}
