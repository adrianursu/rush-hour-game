package root.mcts;

import org.junit.jupiter.api.Test;
import root.Board;
import root.BoardSetups;
import root.Game;

import static org.junit.jupiter.api.Assertions.*;

class Mcst4Test {

    @Test
    void searchTestFromState1() throws Exception {
        long totalSeconds = 0;

        for (int i = 0; i < 1; i++) {
            Node root1 = new Node(getState1());
            long startTime = System.currentTimeMillis();
            Mcst4.search(root1, 1000, true);
            long endTime = System.currentTimeMillis();

            long time = endTime - startTime;
            totalSeconds += time;
        }

        double divisor = 5.0;

        double mean = totalSeconds / divisor;

        System.out.println(mean);
    }

    @Test
    void searchTestFromState4() throws Exception {
        long totalSeconds = 0;

        for (int i = 0; i < 5; i++) {
            Node root1 = new Node(getState4());
            long startTime = System.currentTimeMillis();
            Mcst4.search(root1, 1000, true);
            long endTime = System.currentTimeMillis();

            long time = endTime - startTime;
            totalSeconds += time;
        }

        double divisor = 5.0;

        double mean = totalSeconds / divisor;

        System.out.println(mean);
    }

    Game getState1() throws Exception {
        Board b = new Board();
        BoardSetups[] setups = BoardSetups.values();
        BoardSetups randomSetup = setups[0];

        randomSetup.initialize(b);

        return new Game(b);
    }

    Game getState4() throws Exception {
        Board b = new Board();
        BoardSetups[] setups = BoardSetups.values();
        BoardSetups randomSetup = setups[3];

        randomSetup.initialize(b);

        return new Game(b);
    }
}