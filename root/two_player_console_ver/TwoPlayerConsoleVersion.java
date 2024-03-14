package root.two_player_console_ver;

import root.Board;
import root.Game;

import java.util.Scanner;

//e.g. of input:    'a1' moves a vehicle 1 to right/down    (all vehicles are small letters)
//                  'b-3' moves b vehicle 3 to left/top     (all vehicles are small letters)
//                  'L1' moves left board 1 to down         (2 boards can be moved L and R)
//                  'R-2' moves right board 2 to up         (2 boards can be moved L and R)
//                  '-1' exits the game

public class TwoPlayerConsoleVersion {
    public static void play(Game g) {
        Board.printBoard(g.getBoard());
        System.out.println(Game.evaluate(g, true));
        System.out.println(Game.evaluate(g, false));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (Game.player(g)) {
                System.out.println("LEFT PLAYER MOVE: ");
            } else {
                System.out.println("RIGHT PLAYER MOVE: ");
            }

            String input = scanner.nextLine();

            if ("-1".equals(input)) break;

            try {
                g = Game.result(g, input);

                Board.printBoard(g.getBoard());
                System.out.println(Game.evaluate(g, true));
                System.out.println(Game.evaluate(g, false));

                if (Game.terminalTest(g)) {
                    System.out.println("TERMINAL STATE");
                    break;
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
