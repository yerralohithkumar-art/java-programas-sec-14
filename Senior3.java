public class Senior3 {

    int displayBinNumber() {
        return 101;
    }

    String displayWasteType() {
        return "Plastic";
    }

    boolean displayBinStatus() {
        return true;
    }

    public static void main(String[] args) {

        Senior3 obj = new Senior3();

        System.out.println("Bin Number: " + obj.displayBinNumber());
        System.out.println("Waste Type: " + obj.displayWasteType());
        System.out.println("Bin Status: " + obj.displayBinStatus());
    }
}