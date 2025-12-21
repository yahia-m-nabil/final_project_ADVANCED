package main.java.org.example.final_project.model;

import java.util.ArrayList;

public class MembersList {

    /* ======================== FIELDS ===================== */

    private final ArrayList<Member> members;

    /* ======================== CONSTRUCTOR ===================== */

    public MembersList() {
        this.members = new ArrayList<>();
    }

    /* ======================== GETTERS ===================== */

    public ArrayList<Member> getAllMembers() {
        return members;
    }

    public Member getMemberById(int id) {
        for (Member member : members) {
            if (member.getMemberId() == id) {
                return member;
            }
        }
        return null;
    }

    public Member getMemberByEmail(String email) {
        if (email == null) {
            return null;
        }
        for (Member member : members) {
            if (member.getEmail().equalsIgnoreCase(email)) {
                return member;
            }
        }
        return null;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        for (Member member : members) {
            if (member instanceof User) {
                users.add((User) member);
            }
        }
        return users;
    }

    public ArrayList<Seller> getAllSellers() {
        ArrayList<Seller> sellers = new ArrayList<>();
        for (Member member : members) {
            if (member instanceof Seller) {
                sellers.add((Seller) member);
            }
        }
        return sellers;
    }

    /* ======================== ADD/REMOVE OPERATIONS ===================== */

    public void addMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        if (getMemberById(member.getMemberId()) != null) {
            throw new IllegalArgumentException("Member with ID " + member.getMemberId() + " already exists");
        }
        this.members.add(member);
    }

    public boolean removeMember(int id) {
        return members.removeIf(member -> member.getMemberId() == id);
    }

    /* ======================== UTILITY METHODS ===================== */

    public boolean memberExists(int id) {
        return getMemberById(id) != null;
    }

    public boolean emailExists(String email) {
        return getMemberByEmail(email) != null;
    }

    public int getTotalMemberCount() {
        return members.size();
    }

    public int getUserCount() {
        return getAllUsers().size();
    }

    public int getSellerCount() {
        return getAllSellers().size();
    }

    public void clear() {
        members.clear();
    }

    /* ======================== OBJECT METHODS ===================== */

    @Override
    public String toString() {
        return "MembersList{" +
                "Total Members=" + members.size() +
                ", Users=" + getUserCount() +
                ", Sellers=" + getSellerCount() +
                '}';
    }
}
