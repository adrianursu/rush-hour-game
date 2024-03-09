import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    //e.g. of input:    'a1' moves a vehicle 1 to right/down    (all vehicles are small letters)
    //                  'b-3' moves b vehicle 3 to left/top     (all vehicles are small letters)
    //                  'L1' moves left board 1 to down         (2 boards can be moved L and R)
    //                  'R-2' moves right board 2 to up         (2 boards can be moved L and R)
    //                  '-1' exits the game

    public static void main(String[] args) {
        Board b = getInitialState1();
        Game g = new Game(b);
        printBoard(g.getBoard());

        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (Game.player(g)) {
                System.out.println("LEFT PLAYER MOVE: ");
            } else {
                System.out.println("RIGHT PLAYER MOVE: ");
            }

            String input = scanner.nextLine();

            if ("-1".equals(input)) break;

            try {
                g = Game.result(g, input);

                printBoard(g.getBoard());

                if (Game.terminalTest(g)) {
                    System.out.println("TERMINAL STATE");
                    break;
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    //p23 in tutorial
    public static Board getInitialState1() {
        Board board = new Board();
        Random random = new Random();

        BoardSetups[] setups = BoardSetups.values();
        int index = random.nextInt(setups.length);
//        BoardSetups randomSetup = setups[index];
        BoardSetups randomSetup = setups[0];

        randomSetup.initialize(board);

        return board;
    }

    public static void printBoard(Board b) {
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


