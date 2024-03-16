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
            Vehicle vehicle = game.getBoard().getVehicles().stream().filter(veh -> veh.getId().equals(str)).findFirst()
                    .orElseThrow(() -> new NoSuchElementException("root.Vehicle with Id " + str + " not found"));
            game.checkIfPlayerIsAllowedToMoveVehicle(vehicle);
            game.checkIfMoveIsNotZero(offset);
            game.getBoard().moveVehicle(vehicle, offset);
        }

        game.setLeftPlayerMove(!game.isLeftPlayerMove());

        return game;
    }

    // TODO: change the condition to check if any of the heros passed the board ->
    // they need to be outside of the board in order to win
    public static boolean terminalTest(Game game) {
        Vehicle leftHero = game.getBoard().getVehicles().stream().filter(veh -> veh.isHero() && veh.isLeft())
                .findFirst().orElseThrow(() -> new NoSuchElementException("Left hero not found"));
        Vehicle rightHero = game.getBoard().getVehicles().stream().filter(veh -> veh.isHero() && !veh.isLeft())
                .findFirst().orElseThrow(() -> new NoSuchElementException("Right hero not found"));

        return (leftHero.getColEnd() == Board.TRUE_WIDTH - 1) || (rightHero.getColStart() == 0);
    }

    public static int utility(Game game, boolean isLeftPlayer) {
        if (!terminalTest(game))
            throw new IllegalArgumentException("Game is not terminal");

        Vehicle leftHero = game.getBoard().getVehicles().stream().filter(veh -> veh.isHero() && veh.isLeft())
                .findFirst().orElseThrow(() -> new NoSuchElementException("Left hero not found"));
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
        if (offset == 0)
            throw new Exception("0 move does not make sense");
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
        List<Integer> potentialMovesForHorizontalVehicles = new ArrayList<>(Arrays.asList(-12, -11, -10, -9, -8, -7, -6,
                -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));

        for (Vehicle vehicle : game.getBoard().getVehicles()) {
            if (vehicle.isVertical()) {
                for (int action : potentialMovesForVerticalVehicles) {
                    Board b = game.getBoard();
                    Vehicle vCopy = vehicle.copy();
                    vCopy.move(action);

                    if (!b.isVehicleLeavesTheBoard(vehicle, vCopy)
                            && !b.isVehicleCollidesWithOtherVehicles(vehicle, vCopy)) {
                        actions.add(vehicle.getId() + action);
                    }
                }
            } else {
                if (game.isLeftPlayerMove() && vehicle.isHero() && !vehicle.isLeft())
                    continue;

                if (!game.isLeftPlayerMove() && vehicle.isHero() && vehicle.isLeft())
                    continue;

                List<Integer> filteredPotentialMoves = potentialMovesForHorizontalVehicles.stream()
                        .filter(m -> ((vehicle.getColStart() + m) >= 0 && (vehicle.getColEnd() + m) <= 13)).toList();

                for (int action : filteredPotentialMoves) {
                    Board b = game.getBoard().copy();
                    Vehicle vCopy = vehicle.copy();
                    vCopy.move(action);

                    if (!b.isVehicleLeavesTheBoard(vehicle, vCopy)
                            && !b.isVehicleCollidesWithOtherVehicles(vehicle, vCopy)) {
                        actions.add(vehicle.getId() + action);
                    }
                }
            }
        }

        return actions;
    }

    public static double evaluate(Game game, boolean isLeftPlayer) {
        if (terminalTest(game))
            return utility(game, isLeftPlayer);

        int f1 = game.getBoard().getNumberOfObstacleVehiclesFromHeroToGoal(true); // range of values 0-11
        int f11 = game.getBoard().getNumberOfObstacleVehiclesFromHeroToGoal(false); // range of values 0-11

        int f2 = game.getBoard().getNumberOfPotentialObstacleBoardPartsFromHeroToGoal(true); // 0/1/2
        int f22 = game.getBoard().getNumberOfPotentialObstacleBoardPartsFromHeroToGoal(false); // 0/1/2

        int f3 = game.getBoard().getNumberOfObstacleBoardPartsFromHeroToGoal(true); // 0/1/2
        int f33 = game.getBoard().getNumberOfObstacleBoardPartsFromHeroToGoal(false); // 0/1/2

        // int f4 = game.getBoard().getDistanceToGoal(true); //range 0-12
        // int f44 = game.getBoard().getDistanceToGoal(false); //range 0-12

        // if were to normalize so that all are of equal importance
        // int w1 = 2;
        // int w2 = 11;
        // int w3 = 11;
        // int w4 = 1.9;

        if (isLeftPlayer) {
            double w1 = 2;
            double w2 = 6;
            double w3 = 9;

            double w11 = 2;
            double w22 = 5;
            double w33 = 7;

            double worstPossibleOffenseScore = (11 * w1) + (2 * w2) + (2 * w3);
            double howBadIsOffensiveScore = (f1 * w1) + (f2 * w2) + (f3 * w3);

            double offensiveScore = worstPossibleOffenseScore - howBadIsOffensiveScore; // higher is better
            double defensiveScore = (f11 * w11) + (f22 * w22) + (f33 * w33); // higher is better

            double bestPossibleDefenseScore = (11 * w11) + (2 * w22) + (2 * w33);

            double score = offensiveScore + defensiveScore;
            double normalizedScore = score / (bestPossibleDefenseScore + worstPossibleOffenseScore);
            return normalizedScore;
        } else {
            double w11 = 2;
            double w22 = 6;
            double w33 = 9;

            double w1 = 2;
            double w2 = 5;
            double w3 = 7;

            double worstPossibleOffenseScore = (11 * w11) + (2 * w22) + (2 * w33);
            double howBadIsOffensiveScore = (f11 * w11) + (f22 * w22) + (f33 * w33);

            double offensiveScore = worstPossibleOffenseScore - howBadIsOffensiveScore; // higher is better
            double defensiveScore = (f1 * w1) + (f2 * w2) + (f3 * w3); // higher is better

            double bestPossibleDefenseScore = (11 * w1) + (2 * w2) + (2 * w3);

            double score = offensiveScore + defensiveScore;
            double normalizedScore = score / (bestPossibleDefenseScore + worstPossibleOffenseScore);
            return normalizedScore;
        }
    }
}