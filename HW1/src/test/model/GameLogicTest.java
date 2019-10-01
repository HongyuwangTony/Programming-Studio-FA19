package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameLogicTest extends MovementTest {
    @Test
    public void testRoundAlternating() {
        Game game = new Game(false);
        assertFalse(game.isStarted());
        game.start();
        assertTrue(game.isStarted());

        assertEquals(0, game.getCurrRound());
        game.alternateRound();
        assertEquals(1, game.getCurrRound());
    }

    @Test
    public void testGameEndsAfterCheckmate() {
        String currStatus =
                "________\n" +
                "________\n" +
                "________\n" +
                "_____K_k\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "_______R\n";
        initializeBoard(currStatus, false);
        Game game = new Game(false);
        game.setBoard(board);
        game.setPlayers(players);
        assertEquals(Game.Status.CHECKMATE, game.getStatus());
    }

    @Test
    public void testGameEndsAfterStalemate() {
        String currStatus =
                "_______k\n" +
                "_____K__\n" +
                "______Q_\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n";
        initializeBoard(currStatus, false);
        Game game = new Game(false);
        game.setBoard(board);
        game.setPlayers(players);
        assertEquals(Game.Status.STALEMATE, game.getStatus());
    }

    @Test
    public void testUndo() {
        String after =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "P_______\n" +
                "_PPPPPPP\n" +
                "RNBQKBNR\n";
        Game game = new Game(false);
        game.setPlayers(players);
        initializeBoard(null, false);
        game.setBoard(board);
        testLegalMove(0, "A2,A3", after, Game.Status.CONTINUE);
        game.recordCommand(
                new Command(new Position("A2"), new Position("A3"), null));
        assertTrue(game.undoLastCommand());

        game.restart(false);
        assertFalse(game.undoLastCommand());
    }
}
