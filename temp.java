import java.util.Scanner; 
public class temp {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.println("Enter temperature value in centigrade ");
      double temp = sc.nextDouble();

      if (temp >= 0 && temp <= 10) {
         System.out.println("Very Cold weather");
      }
      else if ( temp >=10 && temp <= 20)
      {
        System.out.println("cold weather");
      }
      else if (temp>=20 && temp<=30)
      {
        System.out.println("normal temperature");
      }
      else if (temp>=30 && temp <=40)
      {
        System.out.println(" its hot ");

      }
      else 
        {
            System.out.println(" its very hot");
        }
        


      

      sc.close();
   }
}