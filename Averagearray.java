public class Averagearray {
    public static void main(String[] args) {

        int[] arr = {10, 20, 30, 40, 50};
        int sum = 0;

        for (int i = 0; i < 5; i++) {
            sum = sum + arr[i];
        }

        double average = (double) sum / 5;
        System.out.println("Sum is: " + sum);
        System.out.println("Average is: " + average);
    }
}