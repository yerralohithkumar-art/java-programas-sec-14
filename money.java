import java.util.Scanner;

public class money {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        double money;
        double total = 0;

        System.out.println("Monthly Expense and Budget Analyzer");

        System.out.print("Enter your monthly budget: ");
        money = sc.nextDouble();
        sc.nextLine();

        
        for (int i = 1; i <= 20 ; i++) {

            System.out.print("Enter what you spent on: ");
            String item = sc.nextLine();

            System.out.print("Enter how much you spent: ");
            double expense = sc.nextDouble();
            sc.nextLine();

            total = total + expense;

            
            if (i < 20) {
                System.out.print("Do you want to enter another expense? (yes/no): ");
                String answer = sc.nextLine();

                if (answer.equalsIgnoreCase("no")) {
                    break;
                }
            }
        }

        double balance = money - total;

        System.out.println("\nMonthly Report");

        System.out.println("Budget: Rs." + money);
        System.out.println("Total expenses: Rs." + total);

        if (balance > 0) {

            System.out.println("Money left: Rs." + balance);
            System.out.println("You are within your budget.");

        } else if (balance == 0) {

            System.out.println("You used all your budget.");
            System.out.println("You reached your budget limit exactly.");

        } else {

            double extra = total - money;

            System.out.println("Your budget limit has been crossed!");
            System.out.println("You spent extra: Rs." + extra);
        }

        sc.close();
    }
}