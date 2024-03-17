package root.minimax;

import root.Board;
import root.Vehicle;

public class MiniMaxAIFunctions {

    // Constants for scoring
    private static final int WINNING_SCORE = 1000000; // A score representing a win
    private static final int BLOCKING_VEHICLE_MULTIPLIER = 10; // A multiplier for blocking vehicles

    public int evaluate(Board board, boolean isMaximisingPlayer) {
        // If the game is won by the maximising player (AI), return a large positive
        // value.
        if (hasWon(board, isMaximisingPlayer)) {
            return WINNING_SCORE;
        }

        // If the game is won by the minimising player (human), return a large negative
        // value.
        if (hasWon(board, !isMaximisingPlayer)) {
            return -WINNING_SCORE;
        }

        // Evaluate the distance to the goal for both players.
        int aiDistanceToGoal = distanceToGoal(board, false);
        int humanDistanceToGoal = distanceToGoal(board, true);

        // Count the number of blocking vehicles for both players.
        int aiBlockingVehicles = numOfBlockingVehicles(board, false);
        int humanBlockingVehicles = numOfBlockingVehicles(board, true);

        // The scoring formula could be adjusted based on testing to see what provides
        // the best behavior.
        int score = aiDistanceToGoal - humanDistanceToGoal
                + ( - humanBlockingVehicles + aiBlockingVehicles) * BLOCKING_VEHICLE_MULTIPLIER;

        return isMaximisingPlayer ? score : -score; // If we are minimising, invert the score.
    }

    private boolean hasWon(Board board, boolean isMaximisingPlayer) {
        // Return true if the maximising player's hero vehicle has reached the goal.
        // Assuming that reaching the goal means getting to the left edge for AI and
        // right edge for the human.
        Vehicle heroVehicle = board.getHeroVehicle(isMaximisingPlayer);
        return isMaximisingPlayer ? heroVehicle.getColEnd() == Board.TRUE_WIDTH - 1 : heroVehicle.getColStart() == 0;
    }

    private int distanceToGoal(Board board, boolean isMaximisingPlayer) {
        // Calculate the distance of the hero vehicle to the goal.
        // Assuming that for AI the goal is to reach the left edge (column 0) and for
        // the human is the right edge (last column).
        Vehicle heroVehicle = board.getHeroVehicle(isMaximisingPlayer);
        return isMaximisingPlayer ? Board.TRUE_WIDTH - 1 - heroVehicle.getColEnd() : heroVehicle.getColStart();
    }

    private int numOfBlockingVehicles(Board board, boolean isMaximisingPlayer) {
        Vehicle hero = board.getHeroVehicle(isMaximisingPlayer);
        int blockingCount = 0;

        // Assuming vertical movement is up and down, and horizontal is left and right.
        for (Vehicle v : board.getVehicles()) {
            if (isMaximisingPlayer) {
                // For the human, assuming the goal is on the right, we check vehicles on the
                // right of the hero.
                if ((!v.isVertical() && v.getRowStart() == hero.getRowStart() && v.getColStart() > hero.getColEnd()) ||
                        (v.isVertical() && v.getRowStart() <= hero.getRowStart() && v.getRowEnd() >= hero.getRowStart()
                                && v.getColStart() > hero.getColEnd())) {
                    blockingCount++;
                }
            } else {
                // // For the AI, we're assuming the goal is on the left, so we check vehicles
                // on the left of the hero.
                if ((!v.isVertical() && v.getRowStart() == hero.getRowStart() &&
                v.getColEnd() < hero.getColStart()) ||
                (v.isVertical() && v.getRowStart() <= hero.getRowStart() && v.getRowEnd() >=
                hero.getRowStart() && v.getColStart() < hero.getColStart())) {
                    blockingCount++;
                }
            }
        }
        return blockingCount;
    }
}
