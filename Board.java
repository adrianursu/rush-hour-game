import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int HEIGHT = 6;
    public static final int WIDTH = 10;

    private List<Vehicle> vehicles;

    public Board() {
        this.vehicles = new ArrayList<>();
    }

    public Board(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }

    public void printBoard() {
        String[][] board = new String[HEIGHT][WIDTH];

        // Fill the board with dots
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                board[i][j] = ".";
            }
        }

        for (Vehicle vehicle : vehicles) {
            board[vehicle.getRow()][vehicle.getCol()] = "X";
            if (vehicle.isVertical()) {
                for (int i = 1; i < vehicle.getLength(); i++) {
                    board[vehicle.getRow() + i][vehicle.getCol()] = "X";
                }
            } else {
                for (int i = 1; i < vehicle.getLength(); i++) {
                    board[vehicle.getRow()][vehicle.getCol() + i] = "X";
                }
            }
        }

        // Print the board
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println(); // Move to the next line after printing a row
        }

        System.out.println();
    }

    public boolean isMoveLegal(Vehicle vehicle, int i) {
        //todo impl
        return true;
    }

    public void moveVehicle(Vehicle vehicle, int offset) {
        vehicle.move(offset);
    }
}
