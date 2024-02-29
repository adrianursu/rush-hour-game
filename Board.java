import java.util.ArrayList;
import java.util.Arrays;
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


    public void moveVehicle(String vehicleId, int offset) throws Exception {
        //return if reached the vicrto

        Vehicle vehicle = vehicles.stream().filter(veh -> veh.getId().equals(vehicleId)).findFirst().orElseThrow(() -> new NoSuchElementException("Vehicle with Id " + vehicleId + " not found"));
        vehicles.remove(vehicle);

        Vehicle vehicleCopy = vehicle.copy();
        vehicleCopy.move(offset);

        try {
            if (offset == 0) throw new Exception("0 move does not make sense");
            checkIfVehicleLeavesTheBoard(vehicleCopy);
            checkIfVehicleCollidesWithExistingOnes(vehicleCopy);


            addVehicle(vehicleCopy);
        } catch (Exception e) {
            addVehicle(vehicle);

            throw e;
        }
    }

    private void checkIfVehicleCollidesWithExistingOnes(Vehicle vehicle) throws Exception {
        String[][] board = getStringBoard();

        if (vehicle.isVertical()) {
            for (int i = 0; i < vehicle.getLength(); i++) {
                if (!board[vehicle.getRow() + i][vehicle.getCol()].equals("."))
                    throw new Exception("Vehicle collides with another vehicle");
            }
        } else {
            for (int i = 0; i < vehicle.getLength(); i++) {
                if (!board[vehicle.getRow()][vehicle.getCol() + i].equals("."))
                    throw new Exception("Vehicle collides with another vehicle");
            }
        }
    }

    private void checkIfVehicleLeavesTheBoard(Vehicle vehicle) throws Exception {
        if (vehicle.isVertical()) {
            if (vehicle.getRow() < 0 || vehicle.getRow() + vehicle.getLength() > HEIGHT)
                throw new Exception("Vehicle leaves the board");
        } else {
            if (vehicle.getCol() < 0 || vehicle.getCol() + vehicle.getLength() > WIDTH)
                throw new Exception("Vehicle leaves the board");
        }
    }

    private String[][] getStringBoard() {
        String[][] board = new String[HEIGHT][WIDTH];

        Arrays.stream(board).forEach(row -> Arrays.fill(row, "."));

        for (Vehicle vehicle : vehicles) {
            if (vehicle.isVertical()) {
                for (int i = 0; i < vehicle.getLength(); i++)
                    board[vehicle.getRow() + i][vehicle.getCol()] = vehicle.getId();
            } else {
                for (int i = 0; i < vehicle.getLength(); i++)
                    board[vehicle.getRow()][vehicle.getCol() + i] = vehicle.getId();
            }
        }

        return board;
    }
}
