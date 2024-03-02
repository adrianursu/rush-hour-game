import java.util.Arrays;
import java.util.Scanner;

public class Main {
    //e.g. of input:    'a1' moves a vehicle 1 to right/down    (all vehicles are small letters)
    //                  'b-3' moves b vehicle 3 to left/top     (all vehicles are small letters)
    //                  'L1' moves left board 1 to down         (2 boards can be moved L and R)
    //                  'R-2' moves right board 2 to up         (2 boards can be moved L and R)
    //                  '-1' exits the game

    public static void main(String[] args) {
        Board b = getInitialState1();
        printBoard(b);

        Scanner scanner = new Scanner(System.in);

        boolean isLeftPlayersMove = true;

        while (true) {
            if (isLeftPlayersMove) {
                System.out.println("LEFT PLAYER MOVE: ");
            } else {
                System.out.println("RIGHT PLAYER MOVE: ");
            }

            String input = scanner.nextLine();

            if ("-1".equals(input)) break;

            try {
                String str = input.substring(0, 1);
                int offset = Integer.parseInt(input.substring(1));

                if (str.equals("L")) {
                    b.moveBoardPart(true, offset);
                } else if (str.equals("R")) {
                    b.moveBoardPart(false, offset);
                } else {
                    b.moveVehicle(str, offset, isLeftPlayersMove);
                }

                printBoard(b);
                isLeftPlayersMove = !isLeftPlayersMove;
            } catch (Exception e) {
                if (e instanceof VictoryException) {
                    System.out.println(e.getMessage());
                    printBoard(b);
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

        Vehicle heroLeft = new Vehicle(true, true, 2, 1 + Board.PART_MAX_OFFSET_ABS, 0, false, '>');
        Vehicle heroRight = new Vehicle(true, false, 2, 4 + Board.PART_MAX_OFFSET_ABS, 12, false, '<');
        Vehicle a = new Vehicle(false, false, 3, 4 + Board.PART_MAX_OFFSET_ABS, 0, false, 'a');
        Vehicle b = new Vehicle(false, false, 3, 3 + Board.PART_MAX_OFFSET_ABS, 3, true, 'b');
        Vehicle c = new Vehicle(false, false, 2, Board.PART_MAX_OFFSET_ABS, 4, true, 'c');
        Vehicle d = new Vehicle(false, false, 2, 1 + Board.PART_MAX_OFFSET_ABS, 5, true, 'd');
        Vehicle e = new Vehicle(false, false, 2, 2 + Board.PART_MAX_OFFSET_ABS, 6, true, 'e');
        Vehicle f = new Vehicle(false, false, 2, 2 + Board.PART_MAX_OFFSET_ABS, 7, true, 'f');
        Vehicle g = new Vehicle(false, false, 2, 3 + Board.PART_MAX_OFFSET_ABS, 8, true, 'g');
        Vehicle h = new Vehicle(false, false, 2, 4 + Board.PART_MAX_OFFSET_ABS, 9, true, 'h');
        Vehicle i = new Vehicle(false, false, 3, Board.PART_MAX_OFFSET_ABS, 10, true, 'i');
        Vehicle j = new Vehicle(false, false, 3, 1 + Board.PART_MAX_OFFSET_ABS, 11, false, 'j');

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

        return board;
    }

    private static void printBoard(Board b) {
        String[][] board = new String[Board.TRUE_HEIGHT][Board.TRUE_WIDTH];
        Arrays.stream(board).forEach(row -> Arrays.fill(row, " "));

        for (int i = Board.PART_MAX_OFFSET_ABS + b.getLeftPartOffset(); i < Board.PART_MAX_OFFSET_ABS + Board.PART_HEIGHT + b.getLeftPartOffset(); i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = ".";
            }
        }

        for (int i = Board.PART_MAX_OFFSET_ABS; i < Board.PART_MAX_OFFSET_ABS + Board.PART_HEIGHT; i++) {
            for (int j = 5; j < 9; j++) {
                board[i][j] = ".";
            }
        }

        for (int i = Board.PART_MAX_OFFSET_ABS + b.getRightPartOffset(); i < Board.PART_MAX_OFFSET_ABS + Board.PART_HEIGHT + b.getRightPartOffset(); i++) {
            for (int j = 9; j < 14; j++) {
                board[i][j] = ".";
            }
        }

        for (Vehicle vehicle : b.getVehicles()) {
            if (vehicle.isVertical()) {
                for (int i = 0; i < vehicle.getLength(); i++)
                    board[vehicle.getRowStart() + i][vehicle.getColStart()] = vehicle.getId();
            } else {
                for (int i = 0; i < vehicle.getLength(); i++)
                    board[vehicle.getRowStart()][vehicle.getColStart() + i] = vehicle.getId();
            }
        }

        System.out.println("_ _ _ _ _         _ _ _ _ _ ");

        for (String[] strings : board) {

            int nrEmptyStrings = 0;
            for (int j = 0; j < Board.TRUE_WIDTH; j++) {
                if (strings[j].equals(" ")) nrEmptyStrings++;
            }

            if (nrEmptyStrings != Board.TRUE_WIDTH) {
                for (int j = 0; j < Board.TRUE_WIDTH; j++) {
                    System.out.print(strings[j] + " ");
                }

                System.out.println();
            }
        }

        System.out.println("_ _ _ _ _         _ _ _ _ _ ");
        System.out.println();
    }
}
