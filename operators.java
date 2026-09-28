public class operators{
   
    public static void main(String[] args) {
       int a = 17;
         int b = 5;
         System.out.println("a + b = " + (a + b));
         System.out.println("a - b = " + (a - b));
         System.out.println("a * b = " + (a * b));
         System.out.println("a / b = " + (a / b));
         System.out.println("a % b = " + (a % b));
         int neg = -a;
         boolean open = true;
         boolean shut = !open;
         int count = 0;
            count++;
            System.out.println("neg = " + neg + ", shut = " + shut + ", count = " + count);
            String parity = (a % 2 == 0) ? "even" : "odd";
            System.out.println("a is " + parity);
    }
}