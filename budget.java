import java.util.ArrayList;
import java.util.Scanner;

class Expense {
    String category;
    String description;
    double amount;

    Expense(String category, String description, double amount) {
        this.category = category;
        this.description = description;
        this.amount = amount;
    }
}

public class budget {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Expense> expenses = new ArrayList<>();
    static double budget = 0;

    public static void main(String[] args) {

        while (true) {

            System.out.println();
            System.out.println("==============================");
            System.out.println("    MONTHLY BUDGET ANALYZER");
            System.out.println("==============================");
            System.out.println("1. Set Budget");
            System.out.println("2. Add Expense");
            System.out.println("3. View Expenses");
            System.out.println("4. Check Budget");
            System.out.println("5. Exit");
            System.out.println("==============================");

            System.out.print("Enter choice: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                setBudget();

            } else if (choice.equals("2")) {
                addExpense();

            } else if (choice.equals("3")) {
                viewExpenses();

            } else if (choice.equals("4")) {
                checkBudget();

            } else if (choice.equals("5")) {
                System.out.println("Thank you!");
                break;

            } else {
                System.out.println("Invalid choice!");
            }
        }

        sc.close();
    }

    static void setBudget() {

        System.out.print("\nEnter your monthly budget: Rs. ");

        try {
            double amount = Double.parseDouble(sc.nextLine());

            if (amount <= 0) {
                System.out.println("Budget must be greater than 0.");
            } else {
                budget = amount;
                System.out.println("Budget set successfully!");
                System.out.println("Budget Limit: Rs. " + budget);
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }

    static void addExpense() {

        System.out.print("\nEnter expense category: ");
        String category = sc.nextLine();

        if (category.trim().isEmpty()) {
            System.out.println("Category cannot be empty.");
            return;
        }

        System.out.print("Enter description: ");
        String description = sc.nextLine();

        if (description.trim().isEmpty()) {
            System.out.println("Description cannot be empty.");
            return;
        }

        System.out.print("Enter expense amount: Rs. ");

        try {
            double amount = Double.parseDouble(sc.nextLine());

            if (amount <= 0) {
                System.out.println("Amount must be greater than 0.");
                return;
            }

            Expense e = new Expense(category, description, amount);
            expenses.add(e);

            System.out.println("Expense added successfully!");

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid amount.");
        }
    }

    static void viewExpenses() {

        System.out.println("\n========== VIEW EXPENSES ==========");

        if (expenses.isEmpty()) {
            System.out.println("No expenses added yet.");
            return;
        }

        double total = 0;

        for (int i = 0; i < expenses.size(); i++) {

            Expense e = expenses.get(i);

            System.out.println(
                (i + 1) + ". " +
                e.category + " | " +
                e.description + " | Rs. " +
                e.amount
            );

            total = total + e.amount;
        }

        System.out.println("-----------------------------------");
        System.out.println("Total Expenses: Rs. " + total);
    }

    static void checkBudget() {

        System.out.println("\n========== CHECK BUDGET ==========");

        if (budget <= 0) {
            System.out.println("Please set your budget first.");
            return;
        }

        double total = 0;

        for (Expense e : expenses) {
            total = total + e.amount;
        }

        System.out.println("Budget Limit   : Rs. " + budget);
        System.out.println("Total Expenses : Rs. " + total);

        if (total > budget) {

            double crossed = total - budget;

            System.out.println();
            System.out.println("*** BUDGET LIMIT IS OVER! ***");
            System.out.println("Remaining Money: Rs. 0");
            System.out.println("Amount Crossed : Rs. " + crossed);

        } else {

            double remaining = budget - total;

            System.out.println();
            System.out.println("*** WITHIN BUDGET ***");
            System.out.println("Remaining Money: Rs. " + remaining);
            System.out.println("Amount Crossed : Rs. 0");
        }

        System.out.println("==================================");
    }
}
