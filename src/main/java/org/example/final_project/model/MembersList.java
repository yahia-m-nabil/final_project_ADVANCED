package main.java.org.example.final_project.model;

import java.util.ArrayList;

public class MembersList {
    private ArrayList<Member> members;

    public MembersList() {
        this.members = new ArrayList<>();
    }

    public ArrayList<Member> getAllMembers() {
        return members;
    }

    public void addMember(Member member) {
        this.members.add(member);
    }

    public void removeMember(int id) {
        int IndexId = -1;
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberId() == id) {
                IndexId = i;
                break;
            }
        }
        if (IndexId != -1) {
            members.remove(IndexId);
        }
    }

    public Member getMember(int id) {
        for (Member member : members) {
            if (member.getMemberId() == id) {
                return member;
            }
        }
        return null;
    }
}
