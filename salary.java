import java.util.Scanner;
public class salary{
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        
        System.out.print("Enter the basic salary: ");
        double basicSalary = sc.nextDouble();
        double hra , da , grossSalary;
        
        if (basicSalary <= 10000){
            hra = 0.20 * basicSalary;
            da = 0.80 * basicSalary;
        } 
        else if (basicSalary >= 10001 && basicSalary <= 20000){
            hra = 0.25 * basicSalary;
            da = 0.90 * basicSalary;
        } 
        else{
            hra = 0.30 * basicSalary;
            da = 0.95 * basicSalary;
        }

        grossSalary = basicSalary + hra + da;
        System.out.println("Basic Salary: " + basicSalary);
        System.out.println("HRA: " + hra);
        System.out.println("DA: " + da);
        System.out.println("Gross Salary: " + grossSalary);

        sc.close();
    }
}