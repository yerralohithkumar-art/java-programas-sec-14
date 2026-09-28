import java.util.Scanner;
 public class primre {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("enter a number");
        int n = sc.nextInt();
        if (n <= 1){
        System.out.println( "It is not a prime ");
        } else {
            int count = 0;
        for (int i = 1; i <= n; i++) {
                if (n % i == 0) {
                    count++;
                }
        }
        if (count == 2) {
                System.out.println(n + " is Prime");
            } else {
                System.out.println(n + " is Not Prime");
            }
        }

        sc.close();
    }
}
