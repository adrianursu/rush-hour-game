package root.mcts;

import org.junit.jupiter.api.Test;
import root.Board;
import root.BoardSetups;
import root.Game;

import static org.junit.jupiter.api.Assertions.*;


class Mcst1Test {
    @Test
    void avgBranchingFactorTestState1() throws Exception {
        Game game = getState1();


        Node root1 = new Node(game);

        Mcst1.search(root1, 200000, true);

        double avgBranchingFactor = root1.calculateAvgBranchingFactorWithoutLeafs();

        System.out.println(avgBranchingFactor);
        System.out.println(root1.getNumberOfNonLeafNodes());
        System.out.println(root1.getTotalNumberOfNodes());
    }

    @Test
    void avgBranchingFactorTestState4() throws Exception {
        Game game = getState4();


        Node root1 = new Node(game);

        Mcst1.search(root1, 200000, true);

        double avgBranchingFactor = root1.calculateAvgBranchingFactorWithoutLeafs();

        System.out.println(avgBranchingFactor);
        System.out.println(root1.getNumberOfNonLeafNodes());
        System.out.println(root1.getTotalNumberOfNodes());
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

    @Test
    void rolloutTest() throws Exception {
        Game game = getState1();
        Node root = new Node(game);

        Mcst1.rollout(root, false);

        System.out.println();
    }

    @Test
    void searchTest() throws Exception {
        Game game = getState1();
        game = Game.result(game, ">1");
        Node root = new Node(game);

        Node best = Mcst1.search(root, 1000, false);

        root.printTree();

        System.out.println();
    }

    @Test
    void searchTestFromState1() throws Exception {
        long totalSeconds = 0;

        for (int i = 0; i < 5; i++) {
            Node root1 = new Node(getState1());
            long startTime = System.currentTimeMillis();
            Mcst1.search(root1, 1000, true);
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
            Mcst1.search(root1, 1000, true);
            long endTime = System.currentTimeMillis();

            long time = endTime - startTime;
            totalSeconds += time;
        }

        double divisor = 5.0;

        double mean = totalSeconds / divisor;

        System.out.println(mean);
    }

    @Test
    void selectTest() throws Exception {
        Game game = getState1();
        Node root = new Node(game);
        Node child1 = new Node(game);
        Node child2 = new Node(game);
        Node child3 = new Node(game);
        root.addChild(child1);
        root.addChild(child2);
        root.addChild(child3);
        root.setN(1);
        child1.setN(1);

        Node selectedChild = Mcst1.select(root);

        assertTrue(selectedChild == child2 || selectedChild == child3);
    }


    @Test
    void ucb1Test1() {
        Node n1 = new Node(new Game(new Board()));
        n1.n = 0;

        assertEquals(Double.MAX_VALUE, Mcst1.ucb1Value(n1));
    }

    @Test
    void ucb1Test2() {
        Node parent = new Node(new Game(new Board()));
        parent.n = 1;

        Node n1 = new Node(new Game(new Board()));
        Node n2 = new Node(new Game(new Board()));
        n1.n = 0;
        n2.n = 1;
        n1.parent = parent;
        n2.parent = parent;


        assertTrue(Mcst1.ucb1Value(n1) > Mcst1.ucb1Value(n2));
    }

    @Test
    void ucb1Test3() {
        Node parent = new Node(new Game(new Board()));
        parent.n = 2;

        Node n1 = new Node(new Game(new Board()));
        Node n2 = new Node(new Game(new Board()));
        n1.n = 1;
        n2.n = 1;
        n2.v = 1;
        n1.parent = parent;
        n2.parent = parent;

        assertTrue(Mcst1.ucb1Value(n1) < Mcst1.ucb1Value(n2));
    }

    @Test
    void rolloutTestState1() throws Exception {
        Node root1 = new Node(getState1());

        long msecStart = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {

            Mcst1.rollout(root1, true);
        }

        long msecEnd = System.currentTimeMillis();

        long msec = msecEnd - msecStart;
        System.out.println(msec);
    }

    @Test
    void rolloutTestState4() throws Exception {
        Node root1 = new Node(getState4());

        long msecStart = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {

            Mcst1.rollout(root1, true);
        }

        long msecEnd = System.currentTimeMillis();

        long msec = msecEnd - msecStart;
        System.out.println(msec);
    }
}