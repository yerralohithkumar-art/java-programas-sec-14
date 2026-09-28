import java.util.Scanner;
public class Class8 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
       try{
        System.out.print("enter parcle weight': ");
        double weight = Double.parseDouble(scanner.nextLine());
        System.out.print("weight accepthed: " + weight + " kg");
       }
         catch (NumberFormatException e) {
                System.out.println("invalid weight." + " Please enter a valid number.");

          }
            finally {
                System.out.println("weight check completed.");
                
            }
         
         scanner.close();
    }
}