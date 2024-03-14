import root.Board;
import root.BoardSetups;
import root.Game;
import root.mcts.Mcst;
import root.mcts.Mcst2;
import root.two_player_console_ver.TwoPlayerConsoleVersion;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Game game = getRandomGame();

//        TwoPlayerConsoleVersion.play(game);
//        Mcst.play(game);
        Mcst2.play(game);
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


