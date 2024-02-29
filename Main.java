import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Board b = new Board();

        Vehicle v1 = new Vehicle(true, 2, 2, 0, false, '>');
        Vehicle v2 = new Vehicle(false, 3, 0, 3, true, 'A');

        b.addVehicle(v1);
        b.addVehicle(v2);

        b.printBoard();

        b.moveVehicle(">", 1);

        b.printBoard();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter in format: PIECE_NAME,MOVE_OFFSET"); //e.g. 'A,2' => this will move A 2 places DOWN/RIGHT
            String input = scanner.nextLine();

            if ("-1".equals(input)) break;

            try {
                String[] strParts = input.split(",");
                b.moveVehicle(strParts[0], Integer.parseInt(strParts[1]));
                b.printBoard();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close(); // Close the scanner
    }

    private Board getRandomInitialState() {
        return new Board();
    }
}
