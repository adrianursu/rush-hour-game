import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private Game getGameWithInitialState1() {
        Board b = Main.getInitialState1();
        return new Game(b);
    }

    @Test
    void testResult1() {
        Game game = getGameWithInitialState1();

        List<String> actions = Game.actions(game);
        Main.printBoard(game.getBoard());

        assertTrue(actions.containsAll(List.of("L-5", "L-4", "L-3", "L-2", "L-1", "L1", "L2", "L3", "L4", "L5")));
        assertFalse(actions.containsAll(List.of("L6", "L-6")));

        assertTrue(actions.containsAll(List.of(">1", ">2")));
        assertFalse(actions.containsAll(List.of(">3")));
    }

    @Test
    void testResult2() throws Exception {
        Game game = getGameWithInitialState1();
        game = Game.result(game, "L-1");
        Main.printBoard(game.getBoard());

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
        Main.printBoard(game.getBoard());

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
        Main.printBoard(game.getBoard());

        List<String> actions = Game.actions(game);

        System.out.println();
    }
}