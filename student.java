import java.util.Scanner;

public class student {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int m1;
        int m2;
        int m3;
        int total;
        double average;

        System.out.print("Enter marks of 3 subjects: ");
        m1 = sc.nextInt();
        m2 = sc.nextInt();
        m3 = sc.nextInt();

        total = m1 + m2 + m3;
        average = total / 3.0;

        System.out.println("Total marks = " + total);
        System.out.println("Average = " + average);

        if (average >= 90) {
            System.out.println("Grade = A");
        } else if (average >= 80) {
            System.out.println("Grade = B");
        } else if (average >= 70) {
            System.out.println("Grade = C");
        } else if (average >= 60) {
            System.out.println("Grade = D");
        } else {
            System.out.println("Grade = F");
        }
    }
}