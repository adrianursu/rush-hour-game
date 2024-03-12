
package root.minimax;

import root.Game;
import root.RushHourAI;

public class Minimax {
    
    // The depth is how far the algorithm will look ahead in the game. 
    // Adjust this value based on how complex the game is and how long you can wait for the AI to make a move.
    public static final int MAX_DEPTH = 3;
    
    public static String findBestMove(Game game, int depth, boolean isMaximisingPlayer) {
        System.out.println("Going in the findBestMove method");
        // Here we are at the terminal depth or the game is over
        if (depth == 0 || Game.terminalTest(game)) {
            return null; // At the terminal depth, we do not care about the move, but the score
        }

        String bestMove = null;
        int bestScore;
        
        if (isMaximisingPlayer) {
            bestScore = Integer.MIN_VALUE;
            for (String move : Game.actions(game)) {
                try {
                    Game newGame = game.copy();
                    Game.result(newGame, move);
                    int score = minimax(newGame, depth - 1, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = move;
                    }
                } catch (Exception e) {
                    // Handle the situation where the move is not valid (for example, catching the exception thrown by Game.result)
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (String move : Game.actions(game)) {
                try {
                    Game newGame = game.copy();
                    Game.result(newGame, move);
                    int score = minimax(newGame, depth - 1, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (score < bestScore) {
                        bestScore = score;
                        bestMove = move;
                    }
                } catch (Exception e) {
                    // Handle the situation where the move is not valid (for example, catching the exception thrown by Game.result)
                }
            }
        }
        System.out.println("Best move is: " + bestMove + " and the score is: " + bestScore);
        return bestMove;
    }

    public static int minimax(Game game, int depth, boolean isMaximisingPlayer, int alpha, int beta) {
        if (depth == 0 || Game.terminalTest(game)) {
            RushHourAI ai = new RushHourAI();
            return ai.evaluate(game.getBoard(), game.isLeftPlayerMove());
        }

        int bestScore;
        
        if (isMaximisingPlayer) {
            bestScore = Integer.MIN_VALUE;
            for (String move : Game.actions(game)) {
                try {
                    Game newGame = game.copy();
                    Game.result(newGame, move);
                    int score = minimax(newGame, depth - 1, false, alpha, beta);
                    bestScore = Math.max(bestScore, score);

                    alpha = Math.max(alpha, bestScore); // Update alpha
                    if (beta <= alpha) { //Beta cut-off
                        break;
                    }

                } catch (Exception e) {
                    // Handle the situation where the move is not valid (for example, catching the exception thrown by Game.result)
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (String move : Game.actions(game)) {
                try {
                    Game newGame = game.copy();
                    Game.result(newGame, move);
                    int score = minimax(newGame, depth - 1, true, alpha, beta);
                    bestScore = Math.min(bestScore, score);

                    beta = Math.min(beta, bestScore); // Update beta
                    if (beta <= alpha) { // Alpha cut-off 
                        break;
                    }

                } catch (Exception e) {
                    // Handle the situation where the move is not valid (for example, catching the exception thrown by Game.result)
                }
            }
        }
        
        return bestScore;
    }
}
