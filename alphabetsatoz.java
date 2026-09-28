public class alphabetsatoz {
    public static void main(String[] args) {

        System.out.println("a to z");
        for (char i = 'a'; i <= 'z'; i++) {
            System.out.print(i + " ");
        }

        System.out.println();
        System.out.println("A to Z");
        for (char i = 'A'; i <= 'Z'; i++) {
            System.out.print(i + " ");
        }

        System.out.println();
        System.out.println("z to a");
        for (char i = 'z'; i >= 'a'; i--) {
            System.out.print(i + " ");
        }

        System.out.println();
        System.out.println("Z to A");
        for (char i = 'Z'; i >= 'A'; i--) {
            System.out.print(i + " ");
        }
    }
}