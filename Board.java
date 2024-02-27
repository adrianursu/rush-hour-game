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

        // Place vehicles
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

        // Print the board
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

    public boolean isMoveLegal(Vehicle vehicle, int i) {
        //todo impl
        return true;
    }

    public void moveVehicle1(Vehicle vehicle, int offset) { //todo remove this
        vehicle.move(offset);
    }


    public void moveVehicle(String vehicleId, int offset) {
        Vehicle v = vehicles.stream().filter(veh -> veh.getId().equals(vehicleId)).findFirst().orElseThrow(() -> new NoSuchElementException("Vehicle with Id " + vehicleId + " not found"));

        v.move(offset);
    }
}
