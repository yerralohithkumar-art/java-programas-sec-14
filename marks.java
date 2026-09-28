public class marks {
    public static void main(String[] args) {

        String name = "tanay";
        int rollno = 123;
        int mathmarks = 90;
        int physicsmarks = 85;
        int chemistrymarks = 50;
        int sanskritmarks = 99;
        int englishmarks = 80;
        int totalmarks = mathmarks + physicsmarks + chemistrymarks + sanskritmarks + englishmarks;
        System.out.println("Name: " + name);
        System.out.println("Roll No: " + rollno);
        System.out.println("Math Marks: " + mathmarks);
        System.out.println("Physics Marks: " + physicsmarks);
        System.out.println("Chemistry Marks: " + chemistrymarks);
        System.out.println("Sanskrit Marks: " + sanskritmarks);
        System.out.println("English Marks: " + englishmarks);
        System.out.println("Total Marks: " + totalmarks);
        int average = totalmarks / 5;
        System.out.println("Average Marks: " + average);
        if (average >= 90) {
            System.out.println("Grade: A+");
        } else if (average >= 80) {
            System.out.println("Grade: A");
        } else if (average >= 70) {
            System.out.println("Grade: B");
        } else if (average >= 60) {
            System.out.println("Grade: C");
        } else if (average < 50) {
            System.out.println("Grade: fail");
        }
    }
}