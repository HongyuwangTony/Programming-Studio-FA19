package test.pieces;

import junit.framework.TestCase;
import main.model.*;
import main.model.pieces.Cannon;

public class CannonTest extends TestCase {
    public void testInvalidDirection() {
        Player[] players = Game.generatePlayers("White", "Black");
        Board board = new Board(players, true);
        String before = board.toString();
        assertTrue(board.getPiece(new Position("G1")) instanceof Cannon);
        // Test Cannon cannot leap diagonally
        assertFalse(board.movePieceByPosition(players[0], new Position("G1"), new Position("A7")));
        assertTrue(board.toString().equals(before));
    }

    public void testMoveNormally() {
        Player[] players = Game.generatePlayers("White", "Black");
        Board board = new Board(players, true);
        String after =
                "rnbqkdcr\n" +
                "ppppppCp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "PPPPPPPP\n" +
                "RNBQKD_R\n";
        // Test Cannon can leap over one hurdle
        assertTrue(board.movePieceByPosition(players[0], new Position("G1"), new Position("G7")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testInvalidMoveWithoutCapture() {
        Player[] players = Game.generatePlayers("White", "Black");
        Board board = new Board(players, true);
        String before = board.toString();
        assertTrue(board.getPiece(new Position("G8")) instanceof Cannon);
        // Test Cannon cannot move without capture
        assertFalse(board.movePieceByPosition(players[1], new Position("G8"), new Position("G6")));
        assertTrue(board.toString().equals(before));
    }

    public void testInvalidLeap() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkdcr\n" +
                "pppppp_p\n" +
                "______p_\n" +
                "________\n" +
                "________\n" +
                "______P_\n" +
                "PPPPPP_P\n" +
                "RNBQKDCR\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("G1")) instanceof Cannon);
        // Test Cannon cannot leap without hurdle
        assertFalse(board.movePieceByPosition(players[0], new Position("G1"), new Position("G2")));
        assertTrue(board.toString().equals(before));
        // Test Cannon cannot leap too far, i.e. two or more hurdles
        assertFalse(board.movePieceByPosition(players[0], new Position("G1"), new Position("G8")));
        assertTrue(board.toString().equals(before));
    }
}
