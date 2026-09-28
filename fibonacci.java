import java.util.Scanner;

public class fibonacci {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n;
        int a = 0;
        int b = 1;
        int c;

        System.out.print("Enter n: ");
        n = sc.nextInt();

        for (int i = 1; i <= n; i++) {
            System.out.print(a + " ");

            c = a + b;
            a = b;
            b = c;
        }
    }
}