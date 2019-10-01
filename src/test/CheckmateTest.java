package test;

import junit.framework.*;
import main.model.*;
import main.model.pieces.*;

public class CheckmateTest extends TestCase {
    // Tests are from https://en.wikipedia.org/wiki/Checkmate#Examples

    // Fool's Mate
    public void testCheckmate1() {
        Player[] players = Game.generatePlayers("White", "Black");
        String currStatus =
                "rnb_kbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "____p___\n" +
                "______Pq\n" +
                "_____P__\n" +
                "PPPPP__P\n" +
                "RNBQKBNR\n";
        Board board = new Board(players, currStatus);
        // White is checkmated
        assertEquals(Game.Status.CHECKMATE, board.isCheckmateOrStalemate(players[1]));
    }

    // D. Byrne vs. Fischer
    public void testCheckmate2() {
        Player[] players = Game.generatePlayers("White", "Black");
        String currStatus =
                "_Q______\n" +
                "_____pk_\n" +
                "__p___p_\n" +
                "_p__N__p\n" +
                "_b_____P\n" +
                "_bn_____\n" +
                "__r___P_\n" +
                "__K_____\n";
        Board board = new Board(players, currStatus);
        // White is checkmated
        assertEquals(Game.Status.CHECKMATE, board.isCheckmateOrStalemate(players[1]));
    }

    // Checkmate with a rook
    public void testCheckmate3() {
        Player[] players = Game.generatePlayers("White", "Black");
        String currStatus =
                "________\n" +
                "________\n" +
                "________\n" +
                "_____K_k\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "_______R\n";
        Board board = new Board(players, currStatus);
        // White is checkmated
        assertEquals(Game.Status.CHECKMATE, board.isCheckmateOrStalemate(players[0]));
    }
}
