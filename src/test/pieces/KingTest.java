package test.pieces;

import junit.framework.TestCase;
import main.model.*;
import main.model.pieces.*;

public class KingTest extends TestCase {
    public void testMoveNormally() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "p_pp_ppp\n" +
                "________\n" +
                "____P___\n" +
                "_p______\n" +
                "__KP____\n" +
                "PPP__PPP\n" +
                "RNBQ_BNR\n";
        String after =
                "rnbq_bnr\n" +
                "p_ppkppp\n" +
                "________\n" +
                "____P___\n" +
                "_K______\n" +
                "___P____\n" +
                "PPP__PPP\n" +
                "RNBQ_BNR\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("E8")) instanceof King);
        assertTrue(board.getPiece(new Position("C3")) instanceof King);
        assertTrue(board.movePieceByPosition(players[1], new Position("E8"), new Position("E7")));
        assertTrue(board.movePieceByPosition(players[0], new Position("C3"), new Position("B4")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testInvalidDirection() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "________\n" +
                "________\n" +
                "________\n" +
                "k__K____\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("A5")) instanceof King);
        // Move in L-shape
        assertFalse(board.movePieceByPosition(players[1], new Position("A5"), new Position("C4")));
        assertTrue(board.toString().equals(before));
    }

    public void testCannotPutKingInCheck() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "________\n" +
                "________\n" +
                "________\n" +
                "k_K_____\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "R_______\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("A5")) instanceof King);
        assertFalse(board.movePieceByPosition(players[1], new Position("A5"), new Position("B4")));
        assertTrue(board.toString().equals(before));
    }
}
