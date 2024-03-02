import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
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

    //p23 in tutorial
    private static Board getInitialState1() {
        Board board = new Board();

        Vehicle heroLeft = new Vehicle(true, true, 2, 1, 0, false, '>');
        Vehicle heroRight = new Vehicle(true, false, 2, 4, 12, false, '<');
        Vehicle a = new Vehicle(false, false, 2, 0, 2, false, 'a');
        Vehicle b = new Vehicle(false, false, 3, 3, 3, true, 'b');
        Vehicle c = new Vehicle(false, false, 2, 0, 4, true, 'c');
        Vehicle d = new Vehicle(false, false, 2, 1, 5, true, 'd');
        Vehicle e = new Vehicle(false, false, 2, 2, 6, true, 'e');
        Vehicle f = new Vehicle(false, false, 2, 2, 7, true, 'f');
        Vehicle g = new Vehicle(false, false, 2, 3, 8, true, 'g');
        Vehicle h = new Vehicle(false, false, 2, 4, 9, true, 'h');
        Vehicle i = new Vehicle(false, false, 3, 0, 10, true, 'i');
        Vehicle j = new Vehicle(false, false, 2, 2, 2, false, 'j');

        try {
            board.addVehicle(heroLeft);
            board.addVehicle(heroRight);
            board.addVehicle(a);
            board.addVehicle(b);
            board.addVehicle(c);
            board.addVehicle(d);
            board.addVehicle(e);
            board.addVehicle(f);
            board.addVehicle(g);
            board.addVehicle(h);
            board.addVehicle(i);
            board.addVehicle(j);
        } catch (Exception e1) {
            e1.printStackTrace();

        }
        return board;
}}

