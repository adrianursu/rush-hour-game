package root;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class Game {
    private final Board board;
    private boolean isLeftPlayerMove;

    public Game(Board board) {
        this.board = board;
        this.isLeftPlayerMove = true;
    }

    public boolean isLeftPlayerMove() {
        return isLeftPlayerMove;
    }

    public void setLeftPlayerMove(boolean leftPlayerMove) {
        isLeftPlayerMove = leftPlayerMove;
    }

    public Board getBoard() {
        return board;
    }

    public static boolean player(Game game) {
        return game.isLeftPlayerMove();
    }

    public static List<String> actions(Game game) {

        //todo optimize this shit
        List<String> actions = new ArrayList<>(getActionForVehicles(game));

        List<Integer> possibleLeftMovementsOffsets = game.getBoard().getPossibleOffsetsForLeftPartMovement();
        actions.addAll(possibleLeftMovementsOffsets.stream().map(e -> "L" + e).toList());

        List<Integer> possibleRightMovementsOffsets = game.getBoard().getPossibleOffsetsForRightPartMovement();
        actions.addAll(possibleRightMovementsOffsets.stream().map(e -> "R" + e).toList());

        return actions;
    }

    public static Game result(Game game, String action) throws Exception {
        String str = action.substring(0, 1);
        int offset = Integer.parseInt(action.substring(1));

        if (str.equals("L")) {
            game.checkIfMoveIsNotZero(offset);
            game.getBoard().moveLeftPart(offset);
        } else if (str.equals("R")) {
            game.checkIfMoveIsNotZero(offset);
            game.getBoard().moveRightPart(offset);
        } else {
            Vehicle vehicle = game.getBoard().getVehicles().stream().filter(veh -> veh.getId().equals(str)).findFirst().orElseThrow(() -> new NoSuchElementException("root.Vehicle with Id " + str + " not found"));
            game.checkIfPlayerIsAllowedToMoveVehicle(vehicle);
            game.checkIfMoveIsNotZero(offset);
            game.getBoard().moveVehicle(vehicle, offset);
        }

        game.setLeftPlayerMove(!game.isLeftPlayerMove());

        return game;
    }

    public static boolean terminalTest(Game game) {
        Vehicle leftHero = game.getBoard().getVehicles().stream().filter(veh -> veh.isHero() && veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Left hero not found"));
        Vehicle rightHero = game.getBoard().getVehicles().stream().filter(veh -> veh.isHero() && !veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Right hero not found"));

        return (leftHero.getColEnd() == Board.TRUE_WIDTH - 1) || (rightHero.getColStart() == 0);
    }

    public static int utility(Game game, boolean isLeftPlayer) {
        if (!terminalTest(game)) throw new IllegalArgumentException("Game is not terminal");

        Vehicle leftHero = game.getBoard().getVehicles().stream().filter(veh -> veh.isHero() && veh.isLeft()).findFirst().orElseThrow(() -> new NoSuchElementException("Left hero not found"));
        boolean leftPlayerWon = leftHero.getColEnd() == Board.TRUE_WIDTH - 1;

        if (isLeftPlayer && leftPlayerWon) {
            return 1;
        } else if (isLeftPlayer && !leftPlayerWon) {
            return 0;
        } else if (!isLeftPlayer && leftPlayerWon) {
            return 0;
        } else if (!isLeftPlayer && !leftPlayerWon) {
            return 1;
        }

        return -1;
    }

    private void checkIfPlayerIsAllowedToMoveVehicle(Vehicle vehicle) throws Exception {
        if (isLeftPlayerMove() && !vehicle.isLeft() && vehicle.isHero())
            throw new IllegalArgumentException("Player is not allowed to move this vehicle");
        if (!isLeftPlayerMove() && vehicle.isLeft() && vehicle.isHero())
            throw new IllegalArgumentException("Player is not allowed to move this vehicle");
    }

    private void checkIfMoveIsNotZero(int offset) throws Exception {
        if (offset == 0) throw new Exception("0 move does not make sense");
    }

    public Game copy() {
        Board newBoard = this.board.copy();
        Game newGame = new Game(newBoard);
        newGame.setLeftPlayerMove(this.isLeftPlayerMove());

        return newGame;
    }

    private static List<String> getActionForVehicles(Game game) {
        List<String> actions = new ArrayList<>();
        List<Integer> potentialMovesForVerticalVehicles = new ArrayList<>(Arrays.asList(-4, -3, -2, -1, 1, 2, 3, 4));
        List<Integer> potentialMovesForHorizontalVehicles = new ArrayList<>(Arrays.asList(-12, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));


        for (Vehicle vehicle : game.getBoard().getVehicles()) {
            if (vehicle.isVertical()) {
                for (int action : potentialMovesForVerticalVehicles) {
                    try {
                        Board bCopy = game.getBoard().copy();
                        Vehicle vCopy = bCopy.getVehicles().stream().filter(veh -> veh.getId().equals(vehicle.getId())).findFirst().orElseThrow(() -> new NoSuchElementException("root.Vehicle with Id " + vehicle.getId() + " not found"));
                        bCopy.moveVehicle(vCopy, action);
                        actions.add(vehicle.getId() + action);
                    } catch (Exception ignored) {
                    }
                }
            } else {
                if (game.isLeftPlayerMove() && vehicle.isHero() && !vehicle.isLeft()) continue;

                if (!game.isLeftPlayerMove() && vehicle.isHero() && vehicle.isLeft()) continue;

                for (int action : potentialMovesForHorizontalVehicles) {
                    try {
                        Board bCopy = game.getBoard().copy();
                        Vehicle vCopy = bCopy.getVehicles().stream().filter(veh -> veh.getId().equals(vehicle.getId())).findFirst().orElseThrow(() -> new NoSuchElementException("root.Vehicle with Id " + vehicle.getId() + " not found"));
                        bCopy.moveVehicle(vCopy, action);
                        actions.add(vehicle.getId() + action);
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        return actions;
    }
}
