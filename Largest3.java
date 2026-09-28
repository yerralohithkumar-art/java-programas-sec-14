import java.util.Scanner;

public class Largest3 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int a;
        int b;
        int c;

        System.out.print("Enter three numbers: ");
        a = sc.nextInt();
        b = sc.nextInt();
        c = sc.nextInt();

        if (a > b && a > c) {
            System.out.println("Largest = " + a);
        } else if (b > a && b > c) {
            System.out.println("Largest = " + b);
        } else {
            System.out.println("Largest = " + c);
        }
    }
}