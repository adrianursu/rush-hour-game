package root;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

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

        if (isVehicleLeavesTheBoard(vehicle, vehicleCopy)) throw new Exception("Vehicle leaves the board");
        if (isVehicleCollidesWithOtherVehicles(vehicle, vehicleCopy))
            throw new Exception("Vehicle collides with other vehicles");
        vehicle.move(offset);
    }

    public void moveLeftPart(int offset) throws Exception {
        if (isAnyVehicleInBetweenLeftAndMiddlePart())
            throw new Exception("root.Vehicle stuck in between left and middle parts");

        if (!getPossibleOffsetsForLeftPartMovement().contains(offset)) throw new Exception("Offset is not valid");

        leftPartOffset += offset;
        getLeftVehicles().forEach(vehicle -> vehicle.setRowStart(vehicle.getRowStart() + offset));
    }

    public List<Integer> getPossibleOffsetsForLeftPartMovement() {
        if (isAnyVehicleInBetweenLeftAndMiddlePart()) return new ArrayList<>();

        List<Integer> potentialLeftPartMoves = new ArrayList<>(Arrays.asList(-10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        return potentialLeftPartMoves.stream().filter(m -> Math.abs(leftPartOffset + m) <= Board.PART_MAX_OFFSET_ABS).toList();
    }

    public void moveRightPart(int offset) throws Exception {
        if (isAnyVehicleInBetweenMiddleAndRightPart())
            throw new Exception("root.Vehicle stuck in between middle and right parts");

        if (!getPossibleOffsetsForRightPartMovement().contains(offset)) throw new Exception("Offset is not valid");

        rightPartOffset += offset;
        getRightVehicles().forEach(vehicle -> vehicle.setRowStart(vehicle.getRowStart() + offset));
    }

    public List<Integer> getPossibleOffsetsForRightPartMovement() {
        if (isAnyVehicleInBetweenMiddleAndRightPart()) return new ArrayList<>();

        List<Integer> potentialRightPartMoves = new ArrayList<>(Arrays.asList(-10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        return potentialRightPartMoves.stream().filter(m -> Math.abs(rightPartOffset + m) <= Board.PART_MAX_OFFSET_ABS).toList();
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

    public boolean isVehicleCollidesWithOtherVehicles(Vehicle vInCurPos, Vehicle vInNewPos) {
        for (Vehicle vFromBoard : vehicles) {
            if (vFromBoard.getId().equals(vInCurPos.getId())) continue;

            if (vFromBoard.isVertical() && vInCurPos.isVertical()) {
                if (vFromBoard.getColStart() != vInCurPos.getColStart()) continue;

                int vFromBoardStart = vFromBoard.getRowStart();
                int vFromBoardEnd = vFromBoard.getRowEnd();

                int vStart = Integer.min(vInCurPos.getRowStart(), vInNewPos.getRowStart());
                int vEnd = Integer.max(vInCurPos.getRowEnd(), vInNewPos.getRowEnd());

                if (vFromBoardEnd >= vStart && vEnd >= vFromBoardStart)
                    return true;

            } else if (!vFromBoard.isVertical() && !vInCurPos.isVertical()) {
                if (vFromBoard.getRowStart() != vInCurPos.getRowStart()) continue;

                int vFromBoardStart = vFromBoard.getColStart();
                int vFromBoardEnd = vFromBoard.getColEnd();

                int vStart = Integer.min(vInCurPos.getColStart(), vInNewPos.getColStart());
                int vEnd = Integer.max(vInCurPos.getColEnd(), vInNewPos.getColEnd());

                if (vFromBoardEnd >= vStart && vEnd >= vFromBoardStart)
                    return true;

            } else if (vFromBoard.isVertical() && !vInCurPos.isVertical()) {
                int vFromBoardStart = vFromBoard.getRowStart();
                int vFromBoardEnd = vFromBoard.getRowEnd();

                if (vInCurPos.getRowStart() < vFromBoardStart || vInCurPos.getRowStart() > vFromBoardEnd) continue;

                int vFromBoardCol = vFromBoard.getColStart();

                int vStart = Integer.min(vInCurPos.getColStart(), vInNewPos.getColStart());
                int vEnd = Integer.max(vInCurPos.getColEnd(), vInNewPos.getColEnd());

                if (!(vFromBoardCol < vStart || vFromBoardCol > vEnd))
                    return true;

            } else if (!vFromBoard.isVertical() && vInCurPos.isVertical()) {
                int vFromBoardStart = vFromBoard.getColStart();
                int vFromBoardEnd = vFromBoard.getColEnd();

                if (vInCurPos.getColStart() < vFromBoardStart || vInCurPos.getColStart() > vFromBoardEnd) continue;

                int vFromBoardRow = vFromBoard.getRowStart();

                int vStart = Integer.min(vInCurPos.getRowStart(), vInNewPos.getRowStart());
                int vEnd = Integer.max(vInCurPos.getRowEnd(), vInNewPos.getRowEnd());

                if (!(vFromBoardRow < vStart || vFromBoardRow > vEnd))
                    return true;
            }
        }

        return false;
    }

    public boolean isVehicleLeavesTheBoard(Vehicle vInCurPos, Vehicle vInNewPos) {
        if (vInCurPos.isVertical()) {
            int vCol = vInCurPos.getColStart();

            int vPathStart = Integer.min(vInCurPos.getRowStart(), vInNewPos.getRowStart());
            int vPathEnd = Integer.max(vInCurPos.getRowEnd(), vInNewPos.getRowEnd());

            if (vCol < 5) {
                int partStart = 5 + leftPartOffset;
                int partEnd = 10 + leftPartOffset;

                if (vPathStart < partStart || vPathEnd > partEnd) return true;

            } else if (vCol < 9) {
                int partStart = 5;
                int partEnd = 10;

                if (vPathStart < partStart || vPathEnd > partEnd) return true;

            } else {
                int partStart = 5 + rightPartOffset;
                int partEnd = 10 + rightPartOffset;

                if (vPathStart < partStart || vPathEnd > partEnd) return true;
            }
        } else {
            int vRow = vInCurPos.getRowStart();

            int vPathStart = Integer.min(vInCurPos.getColStart(), vInNewPos.getColStart());
            int vPathEnd = Integer.max(vInCurPos.getColEnd(), vInNewPos.getColEnd());

            if (vPathStart >= 0 && vPathEnd <= 4) return false; //perfectly fits into left part
            if (vPathStart >= 5 && vPathEnd <= 8) return false; //perfectly fits into middle part
            if (vPathStart >= 9 && vPathEnd <= 13) return false; //perfectly fits into right part

            int leftPartStartRow = 5 + leftPartOffset;
            int leftPartEndRow = 10 + leftPartOffset;

            int middlePartStartRow = 5;
            int middlePartEndRow = 10;

            int rightPartStartRow = 5 + rightPartOffset;
            int rightPartEndRow = 10 + rightPartOffset;

            if (vPathStart >= 0 && vPathEnd <= 8) { //fits between left and middle
                if (!(vRow >= leftPartStartRow && vRow <= leftPartEndRow && vRow >= middlePartStartRow && vRow <= middlePartEndRow))
                    return true;

            } else if (vPathStart >= 5 && vPathEnd <= 13) { //fits between middle and right
                if (!(vRow >= middlePartStartRow && vRow <= middlePartEndRow && vRow >= rightPartStartRow && vRow <= rightPartEndRow))
                    return true;

            } else if (vPathStart >= 0 && vPathEnd <= 13) { //fits between all 3 parts
                if (!(vRow >= leftPartStartRow && vRow <= leftPartEndRow && vRow >= middlePartStartRow && vRow <= middlePartEndRow && vRow >= rightPartStartRow && vRow <= rightPartEndRow))
                    return true;
            } else return true;
        }

        return false;
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

    public int getNumberOfObstacleVehiclesFromHeroToGoal(boolean isForLeftHero) {
        int count = 0;

        Vehicle hero;
        if (isForLeftHero) {
            hero = vehicles.stream().filter(veh -> veh.isHero() && veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Left hero not found"));
        } else {
            hero = vehicles.stream().filter(veh -> veh.isHero() && !veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Right hero not found"));
        }

        int heroPathStart = isForLeftHero ? hero.getColStart() : 0;
        int heroPathEnd = isForLeftHero ? (TRUE_WIDTH - 1 - 1) : hero.getColEnd();

        for (Vehicle vFromBoard : vehicles) {
            if (vFromBoard.getId().equals(hero.getId())) continue;

            if (vFromBoard.isVertical()) {
                int vFromBoardStart = vFromBoard.getRowStart();
                int vFromBoardEnd = vFromBoard.getRowEnd();

                if (hero.getRowStart() < vFromBoardStart || hero.getRowStart() > vFromBoardEnd) continue;

                int vFromBoardCol = vFromBoard.getColStart();

                if (!(vFromBoardCol < heroPathStart || vFromBoardCol > heroPathEnd)) count++;
            } else {
                if (vFromBoard.getRowStart() != hero.getRowStart()) continue;

                int vFromBoardStart = vFromBoard.getColStart();
                int vFromBoardEnd = vFromBoard.getColEnd();

                if (vFromBoardEnd >= heroPathStart && heroPathEnd >= vFromBoardStart) count++;
            }
        }

        return count;
    }

    public int getNumberOfPotentialObstacleBoardPartsFromHeroToGoal(boolean isForLeftHero) {
        Vehicle hero;
        if (isForLeftHero) {
            hero = vehicles.stream().filter(veh -> veh.isHero() && veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Left hero not found"));

            if (hero.getColEnd() < 5) return 2;
            if (hero.getColEnd() < 9) return 1;
        } else {
            hero = vehicles.stream().filter(veh -> veh.isHero() && !veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Right hero not found"));

            if (hero.getColStart() > 8) return 2;
            if (hero.getColStart() > 4) return 1;
        }
        return 0;
    }

    public int getNumberOfObstacleBoardPartsFromHeroToGoal(boolean isForLeftHero) {
        int leftPartStartRow = 5 + leftPartOffset;
        int leftPartEndRow = 10 + leftPartOffset;

        int middlePartStartRow = 5;
        int middlePartEndRow = 10;

        int rightPartStartRow = 5 + rightPartOffset;
        int rightPartEndRow = 10 + rightPartOffset;

        Vehicle hero;
        if (isForLeftHero) {
            hero = vehicles.stream().filter(veh -> veh.isHero() && veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Left hero not found"));

            int heroRow = hero.getRowStart();

            int heroPathStart = hero.getColStart();

            if (heroPathStart >= 8) return 0;

            if (heroPathStart >= 4) {
                if (!(heroRow >= rightPartStartRow && heroRow <= rightPartEndRow))
                    return 1;
                return 0;

            }

            int count = 0;

            if (!(heroRow >= middlePartStartRow && heroRow <= middlePartEndRow)) count++;
            if (!(heroRow >= rightPartStartRow && heroRow <= rightPartEndRow)) count++;

            return count;
        } else {
            hero = vehicles.stream().filter(veh -> veh.isHero() && !veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Right hero not found"));

            int heroRow = hero.getRowStart();

            int heroPathEnd = hero.getColEnd();

            if (heroPathEnd <= 5) return 0;

            if (heroPathEnd <= 9) {
                if (!(heroRow >= leftPartStartRow && heroRow <= leftPartEndRow))
                    return 1;
                return 0;
            }

            int count = 0;

            if (!(heroRow >= leftPartStartRow && heroRow <= leftPartEndRow)) count++;
            if (!(heroRow >= middlePartStartRow && heroRow <= middlePartEndRow)) count++;

            return count;
        }
    }

    public int getDistanceToGoal(boolean isForLeftHero) {
        if (isForLeftHero) {
            return TRUE_WIDTH - 1 - vehicles.stream().filter(veh -> veh.isHero() && veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Left hero not found")).getColEnd();
        } else {
            return vehicles.stream().filter(veh -> veh.isHero() && !veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Right hero not found")).getColStart();
        }
    }
}
