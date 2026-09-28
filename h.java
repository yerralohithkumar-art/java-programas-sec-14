public class h {
    public static void main(String[] args) {

        int[] marks = {85, 92, 78, 96, 88};
        int searchValue = 78;

        System.out.println("Marks of the student");

        for (int i = 0; i < marks.length; i++) {
            System.out.println(marks[i]);

            if (marks[i] == searchValue) {
                System.out.println("78 is found at index: " + i);

                int count = 0;

                for (int j = 0; j < marks.length; j++) {
                    if (marks[j] == searchValue) {
                        count++;
                    }
                }

                System.out.println("78 is found " + count + " times in the array.");
                int highestMark = marks[0];
                int lowestMark = marks[0];
                for(int k = 1; k < marks.length; k++) {
                    if (marks[k] > highestMark) {
                        highestMark = marks[k];
                    }
                    if (marks[k] < lowestMark) {
                        lowestMark = marks[k];
                    }
                }
                System.out.println("Highest mark: " + highestMark);
                System.out.println("Lowest mark: " + lowestMark);
            }
        }
    }
}