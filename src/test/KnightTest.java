package test;

import junit.framework.*;
import main.model.*;
import main.model.pieces.*;

public class KnightTest extends TestCase {
    public void testMoveNormally() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "____p___\n" +
                "__P_____\n" +
                "________\n" +
                "PP_PPPPP\n" +
                "RNBQKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "____p___\n" +
                "__P_____\n" +
                "N_______\n" +
                "PP_PPPPP\n" +
                "R_BQKBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.movePieceByPosition(players[0], new Position("B1"), new Position("A3")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testInvalidDirection() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqk_nr\n" +
                "ppppbppp\n" +
                "________\n" +
                "____p___\n" +
                "__P_____\n" +
                "__N_____\n" +
                "PP_PPPPP\n" +
                "R_BQKBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("C3")) instanceof Knight);
        // Test if Knight can neither move straight nor move diagonally
        assertFalse(board.movePieceByPosition(players[0], new Position("C3"), new Position("C2")));
        assertTrue(board.toString().equals(before));
        assertFalse(board.movePieceByPosition(players[0], new Position("C3"), new Position("B4")));
        assertTrue(board.toString().equals(before));
        assertFalse(board.movePieceByPosition(players[0], new Position("C3"), new Position("B3")));
        assertTrue(board.toString().equals(before));
    }
}
