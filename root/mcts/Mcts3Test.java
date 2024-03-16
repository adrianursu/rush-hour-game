package root.mcts;

import org.junit.jupiter.api.Test;
import root.Board;
import root.BoardSetups;
import root.Game;

class Mcts3Test {

    @Test
    void rollout1Test() throws Exception {
        Game game = getState1();
        Node node = new Node(game);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            Mcts3.rollout1(node, true);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
    }

    @Test
    void rollout2Test() throws Exception {
        Game game = getState1();
        Node node = new Node(game);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            Mcts3.rollout2(node, true);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
    }

    @Test
    void rollout3Test() throws Exception {
        Game game = getState1();
        Node node = new Node(game);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1; i++) {
            Mcts3.rollout3(node, true);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
    }

    @Test
    void rolloutTestState1() throws Exception {
        Game game = getState1();
        Node node = new Node(game);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            Mcts3.rollout(node, true);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
    }

    @Test
    void rolloutTestState4() throws Exception {
        Game game = getState4();
        Node node = new Node(game);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            Mcts3.rollout(node, true);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
    }

    @Test
    void searchTestFromState1() throws Exception {
        long totalSeconds = 0;

        for (int i = 0; i < 5; i++) {
            Node root1 = new Node(getState1());
            long startTime = System.currentTimeMillis();
            Mcts3.search(root1, 1000, true);
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
            Mcts3.search(root1, 1000, true);
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