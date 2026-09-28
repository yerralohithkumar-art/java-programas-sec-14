import java.util.Scanner;

public class Class8 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter parcel weight: ");

            double weight = Double.parseDouble(scanner.nextLine());

            System.out.println("Weight accepted: " + weight + " kg");
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid weight." + " Please enter a valid number.");
        }
        finally {
            System.out.println("Weight check completed.");
        }

        scanner.close();  
    }
}