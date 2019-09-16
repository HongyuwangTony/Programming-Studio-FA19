package test;

import junit.framework.*;
import main.model.*;
import main.model.pieces.*;

public class BishopTest extends TestCase {
    public void testMoveNormally() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "____p___\n" +
                "___P____\n" +
                "________\n" +
                "PPP_PPPP\n" +
                "RNBQKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "____p___\n" +
                "___P_B__\n" +
                "________\n" +
                "PPP_PPPP\n" +
                "RN_QKBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.movePieceByPosition(players[0], new Position("C1"), new Position("F4")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testInvalidDirection() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "pppp_pp_\n" +
                "________\n" +
                "_______p\n" +
                "___P_B__\n" +
                "________\n" +
                "PPP_PPPP\n" +
                "RN_QKBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("F4")) instanceof Bishop);
        // Test Bishop cannot move straight and the board stays the same
        assertFalse(board.movePieceByPosition(players[0], new Position("F4"), new Position("G4")));
        assertTrue(board.toString().equals(before));
        assertFalse(board.movePieceByPosition(players[0], new Position("F4"), new Position("F6")));
        assertTrue(board.toString().equals(before));
    }

    public void testCannotCross() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "p____ppp\n" +
                "_p_p____\n" +
                "__P_p___\n" +
                "___B_P__\n" +
                "________\n" +
                "PP_PP_PP\n" +
                "RNBQK_NR\n";
        Board board = new Board(players, before);
        // Test Bishop cannot cross any pieces and the board stays the same
        assertTrue(board.getPiece(new Position("D4")) instanceof Bishop);
        // Cross opponent
        assertFalse(board.movePieceByPosition(players[0], new Position("D4"), new Position("B6")));
        assertTrue(board.toString().equals(before));
        // Cross his own piece
        assertFalse(board.movePieceByPosition(players[0], new Position("D4"), new Position("F6")));
        assertTrue(board.toString().equals(before));
    }
}
