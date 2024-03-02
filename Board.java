import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Board {
    public static final int TRUE_HEIGHT = 16; //0-15, normal pos of parts 5-10
    public static final int TRUE_WIDTH = 14; //0-13
    public static final int PART_MAX_OFFSET_ABS = 5;
    public static final int PART_HEIGHT = 6;

    private final List<Vehicle> vehicles;
    private int leftPartOffset = 0;
    private int rightPartOffset = 0;

    public int getLeftPartOffset() {
        return leftPartOffset;
    }

    public int getRightPartOffset() {
        return rightPartOffset;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public Board() {
        this.vehicles = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }

    public void moveVehicle(String vehicleId, int offset) throws Exception {
        Vehicle vehicle = vehicles.stream().filter(veh -> veh.getId().equals(vehicleId)).findFirst().orElseThrow(() -> new NoSuchElementException("Vehicle with Id " + vehicleId + " not found"));

        Vehicle vehicleCopy = vehicle.copy();
        vehicleCopy.move(offset);

        try {
            if (offset == 0) throw new Exception("0 move does not make sense");
            checkIfVehicleLeavesTheBoard(vehicle, vehicleCopy);
            checkIfVehicleCollidesWithExistingOnes(vehicle, vehicleCopy);
            vehicle.move(offset);

            checkIfVehicleIsInWinningState(vehicleCopy);
        } catch (Exception e) {
            addVehicle(vehicle);

            throw e;
        }
    }

    public void moveBoardPart(boolean isLeft, int offset) throws Exception {
        if (offset == 0) throw new Exception("0 move does not make sense");

        if (isLeft) {
            if (leftPartOffset + offset > PART_MAX_OFFSET_ABS) throw new Exception("Offset is too large");

            checkIfAnyVehicleInBetweenLeftAndMiddlePart();

            leftPartOffset += offset;
            getLeftVehicles().forEach(vehicle -> vehicle.setRowStart(vehicle.getRowStart() + offset));
        } else {
            if (rightPartOffset + offset > PART_MAX_OFFSET_ABS) throw new Exception("Offset is too large");

            checkIfAnyVehicleInBetweenMiddleAndRightPart();

            rightPartOffset += offset;
            getRightVehicles().forEach(vehicle -> vehicle.setRowStart(vehicle.getRowStart() + offset));
        }
    }

    private void checkIfAnyVehicleInBetweenLeftAndMiddlePart() throws Exception {
        List<Vehicle> horizontalVInCol4 = vehicles.stream().filter(v -> (!v.isVertical() && v.getColStart() == 4)).toList();

        if (!horizontalVInCol4.isEmpty()) throw new Exception("Vehicle stuck in between left and middle parts");

        List<Vehicle> horizontalVOfLen3InCol3 = vehicles.stream().filter(v -> (!v.isVertical() && v.getColStart() == 3 && v.getLength() == 3)).toList();

        if (!horizontalVOfLen3InCol3.isEmpty()) throw new Exception("Vehicle stuck in between left and middle parts");
    }

    private void checkIfAnyVehicleInBetweenMiddleAndRightPart() throws Exception {
        List<Vehicle> horizontalVInCol8 = vehicles.stream().filter(v -> (!v.isVertical() && v.getColStart() == 8)).toList();

        if (!horizontalVInCol8.isEmpty()) throw new Exception("Vehicle stuck in between middle and right parts");

        List<Vehicle> horizontalVOfLen3InCol7 = vehicles.stream().filter(v -> (!v.isVertical() && v.getColStart() == 7 && v.getLength() == 3)).toList();

        if (!horizontalVOfLen3InCol7.isEmpty()) throw new Exception("Vehicle stuck in between middle and right parts");
    }

    private void checkIfVehicleIsInWinningState(Vehicle vehicleCopy) throws VictoryException {
        //todo check
        if (!vehicleCopy.isHero()) return;

        if (vehicleCopy.isLeft() && vehicleCopy.getColStart() + vehicleCopy.getLength() == TRUE_WIDTH)
            throw new VictoryException("VICTORY: Left player won!");

        if (!vehicleCopy.isLeft() && vehicleCopy.getColStart() == 0)
            throw new VictoryException("VICTORY: Right player won!");
    }

    private void checkIfVehicleCollidesWithExistingOnes(Vehicle vInCurPos, Vehicle vInNewPos) throws Exception {
        for (Vehicle vFromBoard : vehicles) {
            if (vFromBoard.getId().equals(vInCurPos.getId())) continue;

            if (vFromBoard.isVertical() && vInCurPos.isVertical()) {
                if (vFromBoard.getColStart() != vInCurPos.getColStart()) continue;

                int vFromBoardStart = vFromBoard.getRowStart();
                int vFromBoardEnd = vFromBoard.getRowEnd();

                int vStart = Integer.min(vInCurPos.getRowStart(), vInNewPos.getRowStart());
                int vEnd = Integer.max(vInCurPos.getRowEnd(), vInNewPos.getRowEnd());

                if (vFromBoardEnd >= vStart && vEnd >= vFromBoardStart)
                    throw new Exception("Vehicle collides with another vehicle");

            } else if (!vFromBoard.isVertical() && !vInCurPos.isVertical()) {
                if (vFromBoard.getRowStart() != vInCurPos.getRowStart()) continue;

                int vFromBoardStart = vFromBoard.getColStart();
                int vFromBoardEnd = vFromBoard.getColEnd();

                int vStart = Integer.min(vInCurPos.getColStart(), vInNewPos.getColStart());
                int vEnd = Integer.max(vInCurPos.getColEnd(), vInNewPos.getColEnd());

                if (vFromBoardEnd >= vStart && vEnd >= vFromBoardStart)
                    throw new Exception("Vehicle collides with another vehicle");

            } else if (vFromBoard.isVertical() && !vInCurPos.isVertical()) {
                int vFromBoardStart = vFromBoard.getRowStart();
                int vFromBoardEnd = vFromBoard.getRowEnd();

                if (vInCurPos.getRowStart() < vFromBoardStart || vInCurPos.getRowStart() > vFromBoardEnd) continue;

                int vFromBoardCol = vFromBoard.getColStart();

                int vStart = Integer.min(vInCurPos.getColStart(), vInNewPos.getColStart());
                int vEnd = Integer.max(vInCurPos.getColEnd(), vInNewPos.getColEnd());

                if (!(vFromBoardCol < vStart || vFromBoardCol > vEnd))
                    throw new Exception("Vehicle collides with another vehicle");

            } else if (!vFromBoard.isVertical() && vInCurPos.isVertical()) {
                int vFromBoardStart = vFromBoard.getColStart();
                int vFromBoardEnd = vFromBoard.getColEnd();

                if (vInCurPos.getColStart() < vFromBoardStart || vInCurPos.getColStart() > vFromBoardEnd) continue;

                int vFromBoardRow = vFromBoard.getRowStart();

                int vStart = Integer.min(vInCurPos.getRowStart(), vInNewPos.getRowStart());
                int vEnd = Integer.max(vInCurPos.getRowEnd(), vInNewPos.getRowEnd());

                if (!(vFromBoardRow < vStart || vFromBoardRow > vEnd))
                    throw new Exception("Vehicle collides with another vehicle");
            }
        }
    }

    private void checkIfVehicleLeavesTheBoard(Vehicle vInCurPos, Vehicle vInNewPos) throws Exception {
        //todo check for part boundaries
        if (vInCurPos.isVertical()) {
            int vCol = vInCurPos.getColStart();



//                throw new Exception("Vehicle leaves the board");
        } else {
            if (vInNewPos.getColStart() < 0 || vInNewPos.getColEnd() > (TRUE_WIDTH - 1))
                throw new Exception("Vehicle leaves the board");
        }
    }

    private List<Vehicle> getLeftVehicles() {
        return vehicles.stream().filter(veh -> veh.getColStart() < 5).toList();
    }

    private List<Vehicle> getRightVehicles() {
        return vehicles.stream().filter(veh -> veh.getColStart() > 8).toList();
    }
}
