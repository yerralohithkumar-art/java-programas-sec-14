import java.util.Scanner;

public class reverse {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n;
        System.out.print("Enter n: ");
        n = sc.nextInt();

        System.out.println("Natural numbers:");
        for (int i = n; i >= 1; i--) {
            System.out.print(i + " ");
        }

        System.out.println();
        System.out.println("Even numbers:");
        for (int i = n; i >= 1; i--) {
            if (i % 2 == 0) {
                System.out.print(i + " ");
            }
        }

        System.out.println();
        System.out.println("Odd numbers:");
        for (int i = n; i >= 1; i--) {
            if (i % 2 != 0) {
                System.out.print(i + " ");
            }
        }
    }
}