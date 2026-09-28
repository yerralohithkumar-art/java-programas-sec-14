public class Scenario1 {
    public static void main(String[] args) {

        String customerName = "Lohith";
        int numberOfTickets = 5;
        double ticketPrice = 600;
        double totalAmount = numberOfTickets * ticketPrice;
        int discount = (int) (totalAmount - 500);
        boolean seatAvailable = true;

        System.out.println("Customer Name: " + customerName);
        System.out.println("Number of Tickets: " + numberOfTickets);
        System.out.println("Ticket Price: " + ticketPrice);
        System.out.println("Total Amount: " + totalAmount);
        System.out.println("Seat Available: " + seatAvailable);

        if (totalAmount >= 3000) {
            System.out.println("Customer is eligible for 500 discount");
            System.out.println("After Discount: " + discount);
        } else {
            System.out.println("Customer is not eligible for discount");
            System.out.println("After Discount: " + totalAmount);
        }
    }
}