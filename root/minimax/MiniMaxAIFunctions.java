package root.minimax;

import root.Board;
import root.Vehicle;

public class MiniMaxAIFunctions {

    // Constants for scoring
    private static final int WINNING_SCORE = 1000000; // A score representing a win

    public int evaluate(Board board, boolean isMaximisingPlayer) {
        if (hasWon(board, isMaximisingPlayer)) {
            return WINNING_SCORE;
        } else if (hasWon(board, !isMaximisingPlayer)) {
            return -WINNING_SCORE;
        }

        int score = 0;

        // Path Clarity
        score += evaluatePathClarity(board, isMaximisingPlayer);

        // Mobility
        score += evaluateMobility(board, isMaximisingPlayer);

        // Strategic Blocking
        score += evaluateStrategicBlocking(board, isMaximisingPlayer);

        // Board Shifting Potential
        score += evaluateBoardShiftingPotential(board, isMaximisingPlayer);

        return isMaximisingPlayer ? score : -score;
    }

    private int evaluatePathClarity(Board board, boolean isMaximisingPlayer) {
        int pathClarityScore = 0;
        Vehicle hero = board.getHeroVehicle(isMaximisingPlayer);
        // Assuming horizontal movement towards the goal
        int obstacles = board.getNumOfBlockingVehicles(hero.getId());

        // Inverting the number of obstacles to reflect that fewer obstacles mean a
        // clearer path.
        pathClarityScore = (Board.TRUE_WIDTH - hero.getColStart()) - obstacles * 100; // Weighted for impact

        return pathClarityScore;
    }

    private int evaluateMobility(Board board, boolean isMaximisingPlayer) {
        int mobilityScore = 0;
        Vehicle hero = board.getHeroVehicle(isMaximisingPlayer);

        // Check potential moves in each direction, assuming +/-1 for simplicity
        int possibleMoves = 0;
        try {
            board.moveVehicle(hero, 1);
            possibleMoves++;
            board.moveVehicle(hero, -1); // Revert move
        } catch (Exception ignored) {
        }
        try {
            board.moveVehicle(hero, -1);
            possibleMoves++;
            board.moveVehicle(hero, 1); // Revert move
        } catch (Exception ignored) {
        }

        mobilityScore = possibleMoves * 50; // Assign a value to each possible move

        return mobilityScore;
    }

    private int evaluateStrategicBlocking(Board board, boolean isMaximisingPlayer) {
        int strategicBlockingScore = 0;
        Vehicle enemyHero = board.getHeroVehicle(!isMaximisingPlayer);
        int blockingVehicles = board.getNumOfBlockingVehicles(enemyHero.getId());

        // The more vehicles blocking the opponent, the higher the score
        strategicBlockingScore = blockingVehicles * 150; // Weighted for impact

        return strategicBlockingScore;
    }

    private int evaluateBoardShiftingPotential(Board board, boolean isMaximisingPlayer) {
        int shiftingPotentialScore = 0;

        // Check potential shifts and their impact on the path clarity for the hero car
        Vehicle hero = board.getHeroVehicle(isMaximisingPlayer);
        int[] potentialShifts = isMaximisingPlayer
                ? board.getPossibleOffsetsForRightPartMovement().stream().mapToInt(i -> i).toArray()
                : board.getPossibleOffsetsForLeftPartMovement().stream().mapToInt(i -> i).toArray();

        for (int shift : potentialShifts) {
            // Perform the shift hypothetically and evaluate the path clarity post-shift
            Board hypotheticalBoard = board.copy();
            try {
                if (isMaximisingPlayer) {
                    hypotheticalBoard.moveRightPart(shift);
                } else {
                    hypotheticalBoard.moveLeftPart(shift);
                }

                int pathClarityPostShift = evaluatePathClarity(hypotheticalBoard, isMaximisingPlayer);
                shiftingPotentialScore = Math.max(shiftingPotentialScore, pathClarityPostShift);

                // Undo the hypothetical shift to restore the board to its original state
                if (isMaximisingPlayer) {
                    hypotheticalBoard.moveRightPart(-shift);
                } else {
                    hypotheticalBoard.moveLeftPart(-shift);
                }

            } catch (Exception e) {
                // If the shift is not possible, continue with the next potential shift
            }
        }

        return shiftingPotentialScore;
    }

    private boolean hasWon(Board board, boolean isMaximisingPlayer) {
        // Return true if the maximising player's hero vehicle has reached the goal.
        // Assuming that reaching the goal means getting to the left edge for AI and
        // right edge for the human.
        Vehicle heroVehicle = board.getHeroVehicle(isMaximisingPlayer);
        return isMaximisingPlayer ? heroVehicle.getColEnd() == Board.TRUE_WIDTH - 1 : heroVehicle.getColStart() == 0;
    }
}
