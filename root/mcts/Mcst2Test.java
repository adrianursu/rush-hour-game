package root.mcts;

import org.junit.jupiter.api.Test;
import root.Board;
import root.BoardSetups;
import root.Game;

class Mcst2Test {
    @Test
    void expandTest() throws Exception {
        Game game = getState1();
        Node node = new Node(game);

        Mcst2.expand(node, true);
    }

    @Test
    void searchTest() throws Exception {
        Game game = getState1();
        Node node = new Node(game);

        Mcst2.search(node, 1000, true);

        node.printTree();
    }

    Game getState1() throws Exception {
        Board b = new Board();
        BoardSetups[] setups = BoardSetups.values();
        BoardSetups randomSetup = setups[0];

        randomSetup.initialize(b);

        return new Game(b);
    }
}