import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board b = new Board();

        Vehicle v1 = Vehicle.createMainVehicle();
        v1.setRow(2);

        Vehicle v2 = new Vehicle(false, 3, 0, 3, true, 'A');

        b.addVehicle(v1);
        b.addVehicle(v2);

        b.printBoard();

        b.moveVehicle(">", 1);

        b.printBoard();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter in format: PIECE_NAME,MOVE_OFFSET"); //e.g. 'A,2' => this will move A 2 places DOWN/RIGHT
            String input = scanner.nextLine(); // Read user input

            if ("-1".equals(input)) {
                System.out.println("Exiting...");
                break; // Exit the loop (and the program)
            }

            System.out.println("You entered: " + input); // Echo the input back to the user
        }

        scanner.close(); // Close the scanner
    }

    private Board getRandomInitialState() {
        return new Board();
    }
}
