import root.Board;
import root.BoardSetups;
import root.Game;
import root.mcts.Mcst1;
import root.mcts.Mcst2;
import root.mcts.Mcst4;
import root.mcts.Mcts3;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Game game = getRandomGame();

//        TwoPlayerConsoleVersion.play(game);
//        Mcst1.play(game);
//        Mcst2.play(game);
//        Mcts3.play(game);
        Mcst4.play(game);
    }

    public static Game getRandomGame() {
        Board b = new Board();
        Random random = new Random();

        BoardSetups[] setups = BoardSetups.values();
        int index = random.nextInt(setups.length);
//        root.BoardSetups randomSetup = setups[index];
        BoardSetups randomSetup = setups[0];

        randomSetup.initialize(b);

        return new Game(b);
    }
}


