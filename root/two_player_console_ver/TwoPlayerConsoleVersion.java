package root.two_player_console_ver;

import root.Board;
import root.Game;
import root.minimax.Minimax;

import java.util.Scanner;
public class TwoPlayerConsoleVersion {
    public static void play(Game game) {
        Board.printBoard(game.getBoard());

        Scanner scanner = new Scanner(System.in);

        while (!Game.terminalTest(game)) {

            System.out.println("Current player (true for left, false for right): " + Game.player(game));
            if (Game.player(game)) {
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

                String bestMove = Minimax.findBestMove(game, Minimax.MAX_DEPTH, false);
                if (bestMove != null) {
                    try {
                        System.out.println("Attempting AI move: " + bestMove);
                        game = Game.result(game, bestMove);
                        System.out.println("AI successfully played: " + bestMove);

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


                    System.out.println("Player after move (true for left, false for right): " + Game.player(game));
            Board.printBoard(game.getBoard());

            if (Game.terminalTest(game)) {
                System.out.println("TERMINAL STATE");
                break;
            }
        }

        scanner.close();
    }
}
