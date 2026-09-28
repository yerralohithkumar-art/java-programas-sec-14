// class,object,field,constructor,this,method.
class student{
    String name;
    int rollno;
    String branch;

    student(String name,int rollno,String branch){
        this.name=name;
        this.rollno=rollno;
        this.branch=branch;
    }

    void display(){
        System.out.println("Name: "+name);
        System.out.println("Roll No: "+rollno);
        System.out.println("Branch: "+branch);
    }
}

public class week7 {
    public static void main(String[] args) {
        student s1=new student("stsar",101,"CSE");
        student s2=new student("hell",102,"ECE");

        s1.display();
        System.out.println();
        s2.display();
    }
}