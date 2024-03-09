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

    public Vehicle getVehicleById(String id) {
        return vehicles.stream().filter(v -> v.getId().equals(id)).findFirst().orElse(null);
    }

    public Board() {
        this.vehicles = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }

    public void moveVehicle(Vehicle vehicle, int offset) throws Exception {
        Vehicle vehicleCopy = vehicle.copy();
        vehicleCopy.move(offset);

        checkIfVehicleLeavesTheBoard(vehicle, vehicleCopy);
        checkIfVehicleCollidesWithExistingOnes(vehicle, vehicleCopy);
        vehicle.move(offset);
    }

    public void moveBoardPart(boolean isLeft, int offset) throws Exception {
        if (isLeft) {
            if (Math.abs(leftPartOffset + offset) > PART_MAX_OFFSET_ABS) throw new Exception("Offset is too large");

            checkIfAnyVehicleInBetweenLeftAndMiddlePart();

            leftPartOffset += offset;
            getLeftVehicles().forEach(vehicle -> vehicle.setRowStart(vehicle.getRowStart() + offset));
        } else {
            if (Math.abs(rightPartOffset + offset) > PART_MAX_OFFSET_ABS) throw new Exception("Offset is too large");

            checkIfAnyVehicleInBetweenMiddleAndRightPart();

            rightPartOffset += offset;
            getRightVehicles().forEach(vehicle -> vehicle.setRowStart(vehicle.getRowStart() + offset));
        }
    }

    public int getNumOfBlockingVehicles(String heroId) {
        int numOfBlockingVehicles = 0;
        Vehicle hero = getVehicleById(heroId);
        int heroRow = hero.getRowStart();
        // check if any vehicle is blocking the hero
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isVertical()) {
                // if it's the left hero, we only care about vehicles on the right from him
                // if it's the right hero, we only care about vehicles on the left from him 
                if (hero.isLeft()) {
                    if (vehicle.getColStart() < hero.getColStart()) continue;
                } else {
                    if (vehicle.getColStart() > hero.getColStart()) continue;
                }

                // check if the vehicle is in the same row as the hero
                int vRowStart = vehicle.getRowStart();
                int vRowEnd = vehicle.getRowEnd();
                if (vRowEnd >= heroRow && heroRow >= vRowStart) {
                    numOfBlockingVehicles++;
                }

            } else {
                if (hero.isLeft()) {
                    if (vehicle.getColStart() < hero.getColStart()) continue;
                } else {
                    if (vehicle.getColStart() > hero.getColStart()) continue;
                }

                int vRow = vehicle.getRowStart();

                if (vRow == hero.getRowStart()) {
                    numOfBlockingVehicles++;
                }
            }
        }
        return numOfBlockingVehicles;
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
        if (vInCurPos.isVertical()) {
            int vCol = vInCurPos.getColStart();

            int vPathStart = Integer.min(vInCurPos.getRowStart(), vInNewPos.getRowStart());
            int vPathEnd = Integer.max(vInCurPos.getRowEnd(), vInNewPos.getRowEnd());

            if (vCol < 5) {
                int partStart = 5 + leftPartOffset;
                int partEnd = 10 + leftPartOffset;

                if (vPathStart < partStart || vPathEnd > partEnd) throw new Exception("Vehicle leaves board");

            } else if (vCol < 9) {
                int partStart = 5;
                int partEnd = 10;

                if (vPathStart < partStart || vPathEnd > partEnd) throw new Exception("Vehicle leaves board");

            } else {
                int partStart = 5 + rightPartOffset;
                int partEnd = 10 + rightPartOffset;

                if (vPathStart < partStart || vPathEnd > partEnd) throw new Exception("Vehicle leaves board");
            }
        } else {
            int vRow = vInCurPos.getRowStart();

            int vPathStart = Integer.min(vInCurPos.getColStart(), vInNewPos.getColStart());
            int vPathEnd = Integer.max(vInCurPos.getColEnd(), vInNewPos.getColEnd());

            if (vPathStart >= 0 && vPathEnd <= 4) return; //perfectly fits into left part
            if (vPathStart >= 5 && vPathEnd <= 8) return; //perfectly fits into middle part
            if (vPathStart >= 9 && vPathEnd <= 13) return; //perfectly fits into right part

            int leftPartStartRow = 5 + leftPartOffset;
            int leftPartEndRow = 10 + leftPartOffset;

            int middlePartStartRow = 5;
            int middlePartEndRow = 10;

            int rightPartStartRow = 5 + rightPartOffset;
            int rightPartEndRow = 10 + rightPartOffset;

            if (vPathStart >= 0 && vPathEnd <= 8) { //fits between left and middle
                if (!(vRow >= leftPartStartRow && vRow <= leftPartEndRow && vRow >= middlePartStartRow && vRow <= middlePartEndRow))
                    throw new Exception("Vehicle leaves board");

            } else if (vPathStart >= 5 && vPathEnd <= 13) { //fits between middle and right
                if (!(vRow >= middlePartStartRow && vRow <= middlePartEndRow && vRow >= rightPartStartRow && vRow <= rightPartEndRow))
                    throw new Exception("Vehicle leaves board");

            } else if (vPathStart >= 0 && vPathEnd <= 13) { //fits between all 3 parts
                if (!(vRow >= leftPartStartRow && vRow <= leftPartEndRow && vRow >= middlePartStartRow && vRow <= middlePartEndRow && vRow >= rightPartStartRow && vRow <= rightPartEndRow))
                    throw new Exception("Vehicle leaves board");
            } else throw new Exception("Vehicle leaves board");
        }
    }

    private List<Vehicle> getLeftVehicles() {
        return vehicles.stream().filter(veh -> veh.getColStart() < 5).toList();
    }

    private List<Vehicle> getRightVehicles() {
        return vehicles.stream().filter(veh -> veh.getColStart() > 8).toList();
    }
}
