import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board b = getInitialState1();
        b.printBoard();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter move:"); //e.g. 'a2' => this will move a 2 places DOWN/RIGHT
            String input = scanner.nextLine();

            if ("-1".equals(input)) break;

            try {
                b.moveVehicle(input.substring(0, 1), Integer.parseInt(input.substring(1)));
                b.printBoard();
            } catch (Exception e) {
                if (e instanceof VictoryException) {
                    System.out.println(e.getMessage());
                    b.printBoard();
                    break;
                } else {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }

        scanner.close();
    }

    private static Board getInitialState1() {
        Board b = new Board();

        Vehicle heroLeft = new Vehicle(true, true, 2, 2, 0, false, '>');
//        Vehicle v1 = new Vehicle(false, false, 2, 1, 3, false, 'a');
        Vehicle v2 = new Vehicle(false, false, 2, 1, 6, true, 'b');
        Vehicle v3 = new Vehicle(false, false, 2, 4, 6, true, 'c');

        b.addVehicle(heroLeft);
//        b.addVehicle(v1);
        b.addVehicle(v2);
//        b.addVehicle(v3);

        return b;
    }
}
