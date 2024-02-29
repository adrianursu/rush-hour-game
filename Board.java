import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Board {
    public static final int HEIGHT = 6;
    public static final int WIDTH = 14;
    private final List<Vehicle> vehicles;

    public Board() {
        this.vehicles = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }

    public void printBoard() {
        String[][] board = getStringBoard();

        // Print the board
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }


    public void moveVehicle(String vehicleId, int offset) {
        Vehicle v = vehicles.stream().filter(veh -> veh.getId().equals(vehicleId)).findFirst().orElseThrow(() -> new NoSuchElementException("Vehicle with Id " + vehicleId + " not found"));

        v.move(offset);
    }

    private String[][] getStringBoard() {
        String[][] board = new String[HEIGHT][WIDTH];

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                board[i][j] = ".";
            }
        }

        for (Vehicle vehicle : vehicles) {
            if (vehicle.isVertical()) {
                for (int i = 0; i < vehicle.getLength(); i++) {
                    board[vehicle.getRow() + i][vehicle.getCol()] = vehicle.getId();
                }
            } else {
                for (int i = 0; i < vehicle.getLength(); i++) {
                    board[vehicle.getRow()][vehicle.getCol() + i] = vehicle.getId();
                }
            }
        }

        return board;
    }
}
