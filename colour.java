import java.util.Scanner;

public class colour {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        char colour;

        System.out.print("Enter traffic light colour: ");
        colour = sc.next().charAt(0);

        switch (colour) {
            case 'R':
                System.out.println(" red STOP.");
                break;

            case 'Y':
                System.out.println(" yellow PREPARE TO STOP.");
                break;

            case 'G':
                System.out.println(" green PROCEED.");
                break;

            default:
                System.out.println("invalid input");
        }
    }
}