package root;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    public static final int TRUE_HEIGHT = 16; //0-15, initial pos of parts 5-10. When offset is max up then pos of part is 0-5, when offset is max down then pos of part is 10-15
    public static final int TRUE_WIDTH = 14; //0-13, left part:  0-4, middle part: 5-8, right part: 9-13
    public static final int PART_MAX_OFFSET_ABS = 5;
    public static final int PART_HEIGHT = 6;

    private List<Vehicle> vehicles;
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
            if (isAnyVehicleInBetweenLeftAndMiddlePart())
                throw new Exception("root.Vehicle stuck in between left and middle parts");

            leftPartOffset += offset;
            getLeftVehicles().forEach(vehicle -> vehicle.setRowStart(vehicle.getRowStart() + offset));
        } else {
            if (Math.abs(rightPartOffset + offset) > PART_MAX_OFFSET_ABS) throw new Exception("Offset is too large");
            if (isAnyVehicleInBetweenMiddleAndRightPart())
                throw new Exception("root.Vehicle stuck in between middle and right parts");

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

    public boolean isAnyVehicleInBetweenLeftAndMiddlePart() {
        List<Vehicle> horizontalVInCol4 = vehicles.stream().filter(v -> (!v.isVertical() && v.getColStart() == 4)).toList();

        if (!horizontalVInCol4.isEmpty()) return true;

        List<Vehicle> horizontalVOfLen3InCol3 = vehicles.stream().filter(v -> (!v.isVertical() && v.getColStart() == 3 && v.getLength() == 3)).toList();

        return !horizontalVOfLen3InCol3.isEmpty();
    }

    public boolean isAnyVehicleInBetweenMiddleAndRightPart() {
        List<Vehicle> horizontalVInCol8 = vehicles.stream().filter(v -> (!v.isVertical() && v.getColStart() == 8)).toList();

        if (!horizontalVInCol8.isEmpty()) return true;

        List<Vehicle> horizontalVOfLen3InCol7 = vehicles.stream().filter(v -> (!v.isVertical() && v.getColStart() == 7 && v.getLength() == 3)).toList();

        return !horizontalVOfLen3InCol7.isEmpty();
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
                    throw new Exception("root.Vehicle collides with another vehicle");

            } else if (!vFromBoard.isVertical() && !vInCurPos.isVertical()) {
                if (vFromBoard.getRowStart() != vInCurPos.getRowStart()) continue;

                int vFromBoardStart = vFromBoard.getColStart();
                int vFromBoardEnd = vFromBoard.getColEnd();

                int vStart = Integer.min(vInCurPos.getColStart(), vInNewPos.getColStart());
                int vEnd = Integer.max(vInCurPos.getColEnd(), vInNewPos.getColEnd());

                if (vFromBoardEnd >= vStart && vEnd >= vFromBoardStart)
                    throw new Exception("root.Vehicle collides with another vehicle");

            } else if (vFromBoard.isVertical() && !vInCurPos.isVertical()) {
                int vFromBoardStart = vFromBoard.getRowStart();
                int vFromBoardEnd = vFromBoard.getRowEnd();

                if (vInCurPos.getRowStart() < vFromBoardStart || vInCurPos.getRowStart() > vFromBoardEnd) continue;

                int vFromBoardCol = vFromBoard.getColStart();

                int vStart = Integer.min(vInCurPos.getColStart(), vInNewPos.getColStart());
                int vEnd = Integer.max(vInCurPos.getColEnd(), vInNewPos.getColEnd());

                if (!(vFromBoardCol < vStart || vFromBoardCol > vEnd))
                    throw new Exception("root.Vehicle collides with another vehicle");

            } else if (!vFromBoard.isVertical() && vInCurPos.isVertical()) {
                int vFromBoardStart = vFromBoard.getColStart();
                int vFromBoardEnd = vFromBoard.getColEnd();

                if (vInCurPos.getColStart() < vFromBoardStart || vInCurPos.getColStart() > vFromBoardEnd) continue;

                int vFromBoardRow = vFromBoard.getRowStart();

                int vStart = Integer.min(vInCurPos.getRowStart(), vInNewPos.getRowStart());
                int vEnd = Integer.max(vInCurPos.getRowEnd(), vInNewPos.getRowEnd());

                if (!(vFromBoardRow < vStart || vFromBoardRow > vEnd))
                    throw new Exception("root.Vehicle collides with another vehicle");
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

                if (vPathStart < partStart || vPathEnd > partEnd) throw new Exception("root.Vehicle leaves board");

            } else if (vCol < 9) {
                int partStart = 5;
                int partEnd = 10;

                if (vPathStart < partStart || vPathEnd > partEnd) throw new Exception("root.Vehicle leaves board");

            } else {
                int partStart = 5 + rightPartOffset;
                int partEnd = 10 + rightPartOffset;

                if (vPathStart < partStart || vPathEnd > partEnd) throw new Exception("root.Vehicle leaves board");
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
                    throw new Exception("root.Vehicle leaves board");

            } else if (vPathStart >= 5 && vPathEnd <= 13) { //fits between middle and right
                if (!(vRow >= middlePartStartRow && vRow <= middlePartEndRow && vRow >= rightPartStartRow && vRow <= rightPartEndRow))
                    throw new Exception("root.Vehicle leaves board");

            } else if (vPathStart >= 0 && vPathEnd <= 13) { //fits between all 3 parts
                if (!(vRow >= leftPartStartRow && vRow <= leftPartEndRow && vRow >= middlePartStartRow && vRow <= middlePartEndRow && vRow >= rightPartStartRow && vRow <= rightPartEndRow))
                    throw new Exception("root.Vehicle leaves board");
            } else throw new Exception("root.Vehicle leaves board");
        }
    }

    private List<Vehicle> getLeftVehicles() {
        return vehicles.stream().filter(veh -> veh.getColStart() < 5).toList();
    }

    private List<Vehicle> getRightVehicles() {
        return vehicles.stream().filter(veh -> veh.getColStart() > 8).toList();
    }

    public Board copy() {
        Board board = new Board();
        board.vehicles = vehicles.stream().map(Vehicle::copy).toList();
        board.leftPartOffset = leftPartOffset;
        board.rightPartOffset = rightPartOffset;
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