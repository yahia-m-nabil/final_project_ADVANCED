package org.example.final_project.model;

abstract class Person {
    private int memberId;
    private String name;
    private String email;

    public Person(int id , String name , String email){
        this.memberId=id;
        this.name = name;
        this.email = email;
    }

//    public int GenerateIdusers(){
//
//    }
//    public int GenerateIdsellers(){
//
//    }

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

//    public void  displayInfo(){
//        System.out.println("Name is " + name + "\nEmail is " + email + "\nid is " + memberId );
//    }
}
