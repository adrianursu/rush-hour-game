import root.Board;
import root.BoardSetups;
import root.Game;
import root.mcts.Mcst1;
import root.mcts.Mcst2;
import root.mcts.Mcst4;
import root.mcts.Mcts3;
import root.minimax.Minimax;

import java.util.Random;
import java.util.Scanner;

//e.g. of input:    'a1' moves a vehicle 1 to right/down    (all vehicles are small letters)
//                  'b-3' moves b vehicle 3 to left/top     (all vehicles are small letters)
//                  'L1' moves left board 1 to down         (2 boards can be moved L and R)
//                  'R-2' moves right board 2 to up         (2 boards can be moved L and R)
//                  '-1' exits the game

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(
                "Select the AI algorithm and press ENTER: \n 1 for Minimax \n 2 for MCTS1 \n 3 for MCTS2 \n 4 for MCTS3 \n 5 for MCTS4 ");
        int choice = scanner.nextInt();

        Game game = getRandomGame();

        switch (choice) {
            case 1:
                Minimax.play(game);
                break;
            case 2:
                Mcst1.play(game);
                break;
            case 3:
                Mcst2.play(game);
                break;
            case 4:
                Mcts3.play(game);
                break;
            case 5:
                Mcst4.play(game);
                break;
            default:
                System.out.println("Invalid choice, choosing Minimax as your AI algorithm");
                Minimax.play(game);
                break;
        }

    }

    public static Game getRandomGame() {
        Board b = new Board();
        Random random = new Random();

        BoardSetups[] setups = BoardSetups.values();
        int index = random.nextInt(setups.length);
        // root.BoardSetups randomSetup = setups[index];
        BoardSetups randomSetup = setups[0];

        randomSetup.initialize(b);

        return new Game(b);
    }
}
