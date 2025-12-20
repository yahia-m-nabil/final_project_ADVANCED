package main.java.org.example.final_project.model;

public abstract class Member extends  Person {
private static int totalIdCount = 0;
private int money;

    public Member(int id , String name , String email ){
      super(id, name , email);
      this.money=0;
      totalIdCount++;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public static int getTotalIdCount() {
        return totalIdCount;
    }

    public static void removedMember() {
        if (totalIdCount==0){
            System.out.println("THERE ARE NO USERS OR SELLERS TO DELETE");
        }
        else {
            totalIdCount--;
        }
    }

    abstract public int CalculateMoney();
}
