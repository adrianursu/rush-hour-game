package root.minimax;

import java.util.Scanner;

import root.Board;
import root.Game;
import root.RushHourAI;

public class Minimax {
    
    // The depth is how far the algorithm will look ahead in the game. 
    // Adjust this value based on how complex the game is and how long you can wait for the AI to make a move.
    public static final int MAX_DEPTH = 5;
    
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
        System.out.println(Game.actions(game));
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

     public static void play(Game game) {
        Board.printBoard(game.getBoard());

        Scanner scanner = new Scanner(System.in);
        boolean currentPlayer;

        while (!Game.terminalTest(game)) {
            currentPlayer = Game.player(game);
            System.out.println("Current player : " + (currentPlayer? "left":"right"));
            if (currentPlayer) {
                // Left player (human) move
                System.out.println("LEFT PLAYER MOVE: ");
                String input = scanner.nextLine();

                if ("-1".equals(input)) break;

                try {
                    game = Game.result(game, input);
                    System.out.println("Human played: " + input);
                    System.out.println("Is it left player's move now? " + game.isLeftPlayerMove());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    continue; // Ask for input again
                }
            } else {
                // Right player (AI) move
                System.out.println("RIGHT PLAYER MOVE (AI): ");
                // Adding a log to confirm we reach this point.
                System.out.println("Preparing to find the best move for AI...");


                long startTime = System.currentTimeMillis();
                String bestMove = Minimax.findBestMove(game, Minimax.MAX_DEPTH, false);
                if (bestMove != null) {
                    try {
                        System.out.println("Attempting AI move: " + bestMove);
                        game = Game.result(game, bestMove);
                        System.out.println("AI successfully played: " + bestMove);
                        long endTime = System.currentTimeMillis();
                        long duration = endTime - startTime;
                        System.out.println("It took "+ duration + "ms for the AI to make this move!");
                    } catch (Exception e) {
                        System.out.println("==== ERROR WITH AI MOVE ====");

                    System.out.println("Error with AI move: " + e.getMessage());
                    // Handle AI move error, possibly by choosing a different move
                    }
                } else {
                    System.out.println("AI has no valid moves.");
                    // Handle the situation where AI has no moves, possibly by ending the game or skipping the turn
                }
            }


            System.out.println("Player after move: " + (currentPlayer? "left":"right"));
            Board.printBoard(game.getBoard());

            if (Game.terminalTest(game)) {
                System.out.println("TERMINAL STATE");
                break;
            }
        }

        scanner.close();
    }
}

