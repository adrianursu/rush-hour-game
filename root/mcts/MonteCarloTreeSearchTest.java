package root.mcts;

import org.junit.jupiter.api.Test;
import root.Board;
import root.BoardSetups;
import root.Game;

import static org.junit.jupiter.api.Assertions.*;


class MonteCarloTreeSearchTest {
    Game getState1() throws Exception {
        Board b = new Board();
        BoardSetups[] setups = BoardSetups.values();
        BoardSetups randomSetup = setups[0];

        randomSetup.initialize(b);

        Game game = new Game(b);
        game = Game.result(game, ">1");

        return game;
    }

    @Test
    void rolloutTest() throws Exception {
        Game game = getState1();
        Node root = new Node(game);
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(root);

        mcts.rollout(root);

        System.out.println();
    }

    @Test
    void searchTest() throws Exception {
        Game game = getState1();
        Node root = new Node(game);
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(root);

        Node best = mcts.search(666);

        root.printTree();

        System.out.println();
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

        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(root);

        Node selectedChild = mcts.select(root);

        assertTrue(selectedChild == child2 || selectedChild == child3);
    }


    @Test
    void ucb1Test1() {
        Node n1 = new Node(new Game(new Board()));
        n1.n = 0;
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(n1);


        assertEquals(Double.MAX_VALUE, mcts.ucb1Value(n1));
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
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(n1);


        assertTrue(mcts.ucb1Value(n1) > mcts.ucb1Value(n2));
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
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch(n1);


        assertTrue(mcts.ucb1Value(n1) < mcts.ucb1Value(n2));
    }
}