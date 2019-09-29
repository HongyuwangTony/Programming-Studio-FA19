package model;

import main.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckmateTest extends MovementTest {
    // Tests are from https://en.wikipedia.org/wiki/Checkmate#Examples

    // Fool's Mate
    @Test
    public void testCheckmate1() {
        String currStatus =
                "rnb_kbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "____p___\n" +
                "______Pq\n" +
                "_____P__\n" +
                "PPPPP__P\n" +
                "RNBQKBNR\n";
        initializeBoard(currStatus, false);
        // White is checkmated
        assertEquals(Game.Status.CHECKMATE, board.isCheckmateOrStalemate(players[1]));
    }

    // D. Byrne vs. Fischer
    @Test
    public void testCheckmate2() {
        String currStatus =
                "_Q______\n" +
                "_____pk_\n" +
                "__p___p_\n" +
                "_p__N__p\n" +
                "_b_____P\n" +
                "_bn_____\n" +
                "__r___P_\n" +
                "__K_____\n";
        initializeBoard(currStatus, false);
        // White is checkmated
        assertEquals(Game.Status.CHECKMATE, board.isCheckmateOrStalemate(players[1]));
    }

    // Checkmate with a rook
    @Test
    public void testCheckmate3() {
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
        // White is checkmated
        assertEquals(Game.Status.CHECKMATE, board.isCheckmateOrStalemate(players[0]));
    }
}
