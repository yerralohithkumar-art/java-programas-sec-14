import java.util.Scanner;
  public class unary {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int num = sc.nextInt();
        String unary = "";
        for (int i = 0; i < num; i++) {
            unary += "1";
        }
        System.out.println("Unary representation: " + unary);
    }