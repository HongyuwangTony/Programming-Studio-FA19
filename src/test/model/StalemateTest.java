package model;

import model.Game;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StalemateTest extends MovementTest {
    // Tests below are from https://en.wikipedia.org/wiki/Stalemate#Simple_examples

    @Test
    public void testStaleMateSimple() {
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
        // Black is stalemated
        assertEquals(Game.Status.STALEMATE, board.isCheckmateOrStalemate(players[0]));
    }

    @Test
    public void testStaleMate1() {
        String currStatus =
                "_____k__\n" +
                "_____P__\n" +
                "_____K__\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n";
        initializeBoard(currStatus, false);
        // Black is stalemated
        assertEquals(Game.Status.STALEMATE, board.isCheckmateOrStalemate(players[0]));
    }

    @Test
    public void testStaleMate2() {
        String currStatus =
                "kb_____R\n" +
                "________\n" +
                "_K______\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n";
        initializeBoard(currStatus, false);
        // Black is stalemated
        assertEquals(Game.Status.STALEMATE, board.isCheckmateOrStalemate(players[0]));
    }

    @Test
    public void testStaleMate3() {
        String currStatus =
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "__K_____\n" +
                "_R______\n" +
                "k_______\n";
        initializeBoard(currStatus, false);
        // Black is stalemated
        assertEquals(Game.Status.STALEMATE, board.isCheckmateOrStalemate(players[0]));
    }

    @Test
    public void testStaleMate4() {
        String currStatus =
                "________\n" +
                "________\n" +
                "________\n" +
                "______K_\n" +
                "________\n" +
                "_Q______\n" +
                "p_______\n" +
                "k_______\n";
        initializeBoard(currStatus, false);
        // Black is stalemated
        assertEquals(Game.Status.STALEMATE, board.isCheckmateOrStalemate(players[0]));
    }

    @Test
    public void testStaleMate5() {
        String currStatus =
                "k_______\n" +
                "P_______\n" +
                "K_______\n" +
                "________\n" +
                "_____B__\n" +
                "________\n" +
                "________\n" +
                "________\n";
        initializeBoard(currStatus, false);
        // Black is stalemated
        assertEquals(Game.Status.STALEMATE, board.isCheckmateOrStalemate(players[0]));
    }
}
