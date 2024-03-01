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
        Vehicle vehicle = vehicles.stream().filter(veh -> veh.getId().equals(vehicleId)).findFirst().orElseThrow(() -> new NoSuchElementException("Vehicle with Id " + vehicleId + " not found"));

        Vehicle vehicleCopy = vehicle.copy();
        vehicleCopy.move(offset);

        try {
            if (offset == 0) throw new Exception("0 move does not make sense");
            checkIfVehicleLeavesTheBoard(vehicleCopy);
            checkIfVehicleCollidesWithExistingOnes(vehicle, vehicleCopy);
            vehicle.move(offset);

            checkIfVehicleIsInWinningState(vehicleCopy);
        } catch (Exception e) {
            addVehicle(vehicle);

            throw e;
        }
    }

    private void checkIfVehicleIsInWinningState(Vehicle vehicleCopy) throws VictoryException {
        if (!vehicleCopy.isHero()) return;

        if (vehicleCopy.isLeft() && vehicleCopy.getColStart() + vehicleCopy.getLength() == WIDTH)
            throw new VictoryException("VICTORY: Left player won!");

        if (!vehicleCopy.isLeft() && vehicleCopy.getColStart() == 0)
            throw new VictoryException("VICTORY: Right player won!");
    }

    private void checkIfVehicleCollidesWithExistingOnes(Vehicle existingV, Vehicle vWithNewPos) throws Exception {
        for (Vehicle vFromBoard : vehicles) {
            if (vFromBoard.getId().equals(existingV.getId())) continue;

            if (vFromBoard.isVertical() && existingV.isVertical()) {
                if (vFromBoard.getColStart() != existingV.getColStart()) continue;

                int vFromBoardStart = vFromBoard.getRowStart();
                int vFromBoardEnd = vFromBoard.getRowEnd();

                int vStart = Integer.min(existingV.getRowStart(), vWithNewPos.getRowStart());
                int vEnd = Integer.max(existingV.getRowEnd(), vWithNewPos.getRowEnd());

                if (vFromBoardEnd >= vStart && vEnd >= vFromBoardStart)
                    throw new Exception("Vehicle collides with another vehicle");

            } else if (!vFromBoard.isVertical() && !existingV.isVertical()) {
                if (vFromBoard.getRowStart() != existingV.getRowStart()) continue;

                int vFromBoardStart = vFromBoard.getColStart();
                int vFromBoardEnd = vFromBoard.getColEnd();

                int vStart = Integer.min(existingV.getColStart(), vWithNewPos.getColStart());
                int vEnd = Integer.max(existingV.getColEnd(), vWithNewPos.getColEnd());

                if (vFromBoardEnd >= vStart && vEnd >= vFromBoardStart)
                    throw new Exception("Vehicle collides with another vehicle");

            } else if (vFromBoard.isVertical() && !existingV.isVertical()) {
                int vFromBoardStart = vFromBoard.getRowStart();
                int vFromBoardEnd = vFromBoard.getRowEnd();

                if (existingV.getRowStart() < vFromBoardStart || existingV.getRowStart() > vFromBoardEnd) continue;

                int vFromBoardCol = vFromBoard.getColStart();

                int vStart = Integer.min(existingV.getColStart(), vWithNewPos.getColStart());
                int vEnd = Integer.max(existingV.getColEnd(), vWithNewPos.getColEnd());

                if (!(vFromBoardCol < vStart || vFromBoardCol > vEnd))
                    throw new Exception("Vehicle collides with another vehicle");

            } else if (!vFromBoard.isVertical() && existingV.isVertical()) {
                int vFromBoardStart = vFromBoard.getColStart();
                int vFromBoardEnd = vFromBoard.getColEnd();

                if (existingV.getColStart() < vFromBoardStart || existingV.getColStart() > vFromBoardEnd) continue;

                int vFromBoardRow = vFromBoard.getRowStart();

                int vStart = Integer.min(existingV.getRowStart(), vWithNewPos.getRowStart());
                int vEnd = Integer.max(existingV.getRowEnd(), vWithNewPos.getRowEnd());

                if (!(vFromBoardRow < vStart || vFromBoardRow > vEnd))
                    throw new Exception("Vehicle collides with another vehicle");
            }
        }
    }

    private void checkIfVehicleLeavesTheBoard(Vehicle vehicle) throws Exception {
        if (vehicle.isVertical()) {
            if (vehicle.getRowStart() < 0 || vehicle.getRowStart() + vehicle.getLength() > HEIGHT)
                throw new Exception("Vehicle leaves the board");
        } else {
            if (vehicle.getColStart() < 0 || vehicle.getColStart() + vehicle.getLength() > WIDTH)
                throw new Exception("Vehicle leaves the board");
        }
    }

    private String[][] getStringBoard() {
        String[][] board = new String[HEIGHT][WIDTH];

        Arrays.stream(board).forEach(row -> Arrays.fill(row, "."));

        for (Vehicle vehicle : vehicles) {
            if (vehicle.isVertical()) {
                for (int i = 0; i < vehicle.getLength(); i++)
                    board[vehicle.getRowStart() + i][vehicle.getColStart()] = vehicle.getId();
            } else {
                for (int i = 0; i < vehicle.getLength(); i++)
                    board[vehicle.getRowStart()][vehicle.getColStart() + i] = vehicle.getId();
            }
        }

        return board;
    }
}
