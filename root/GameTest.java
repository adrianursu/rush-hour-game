package root;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game getGameWithInitialState1() {
        Board b = new Board();

        BoardSetups.values()[0].initialize(b);

        return new Game(b);
    }

    @Test
    void testResult1() {
        Game game = getGameWithInitialState1();

        List<String> actions = Game.actions(game);
        Board.printBoard(game.getBoard());

        assertTrue(actions.containsAll(List.of("L-5", "L-4", "L-3", "L-2", "L-1", "L1", "L2", "L3", "L4", "L5")));
        assertFalse(actions.containsAll(List.of("L6", "L-6")));

        assertTrue(actions.containsAll(List.of(">1", ">2")));
        assertFalse(actions.containsAll(List.of(">3")));
    }

    @Test
    void testResult2() throws Exception {
        Game game = getGameWithInitialState1();
        game = Game.result(game, "L-1");
        Board.printBoard(game.getBoard());

        List<String> actions = Game.actions(game);

        assertTrue(actions.containsAll(List.of("L-4", "L-4", "L-3", "L-2", "L-1", "L1", "L2", "L3", "L4", "L6")));
        assertFalse(actions.containsAll(List.of("L-5")));
    }

    @Test
    void testResult3() throws Exception {
        Game game = getGameWithInitialState1();

        game = Game.result(game, "c2"); //l
        game = Game.result(game, "d1"); //r
        game = Game.result(game, ">4"); //l
        game = Game.result(game, "<-1"); //r
        Board.printBoard(game.getBoard());

        List<String> actions = Game.actions(game);

        assertFalse(actions.stream().anyMatch(action -> action.startsWith("L")));
    }

    @Test
    void testResult4() throws Exception {
        Game game = getGameWithInitialState1();

        game = Game.result(game, "c2"); //l
        game = Game.result(game, "d1"); //r
        game = Game.result(game, "L-5"); //l
        game = Game.result(game, "R-4"); //r
        game = Game.result(game, "i3"); //l
        game = Game.result(game, "<-1"); //r
        Board.printBoard(game.getBoard());

        List<String> actions = Game.actions(game);

        System.out.println();
    }

    @Test
    void otherTest() {
        List<Integer> potentialLeftPartMoves = new ArrayList<>(Arrays.asList(-12, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));

        int kek = 0;

        List<Integer> test = potentialLeftPartMoves.stream().filter(m -> ((kek + m) >= 0 && (kek + 1 + m) <= 13)).toList();

        System.out.println();

    }
}