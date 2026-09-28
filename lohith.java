import java.util.Scanner;

public class lohith {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n;
        int reverse = 0;

        System.out.print("Enter a number: ");
        n = sc.nextInt();

        while (n > 0) {
            int digit = n % 10;
            reverse = reverse * 10 + digit;
            n = n / 10;
        }

        System.out.println(reverse);
    }
}