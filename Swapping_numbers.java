import java.util.Scanner;

public class Swapping_numbers {
public static void main(String[] args) {
    
Scanner scanner = new Scanner (System.in);

System.out.print("Enter the value of A:");
int a = scanner.nextInt();

System.out.print("Enter the value of B:");
int b = scanner.nextInt();

int c;

c = a;
a = b;
b = c;

System.out.println("the value of a :" + a);
System.out.println("the value of b :" + b);

scanner.close();

}
}