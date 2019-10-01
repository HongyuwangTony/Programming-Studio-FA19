package test.pieces;

import junit.framework.*;
import main.model.*;
import main.model.pieces.*;

public class DragonTest extends TestCase {
    public void testMoveNormally() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "r___k___\n" +
                "________\n" +
                "__D_____\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "d_______\n" +
                "RNBQK___\n";
        Board board = new Board(players, before);
        String after =
                "D___k___\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "dNBQK___\n";
        // Test Dragon can move diagonally or straight
        assertTrue(board.getPiece(new Position("C6")) instanceof Dragon);
        assertTrue(board.getPiece(new Position("A2")) instanceof Dragon);
        assertTrue(board.movePieceByPosition(players[0], new Position("C6"), new Position("A8")));
        assertTrue(board.movePieceByPosition(players[1], new Position("A2"), new Position("A1")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[1]));
    }

    public void testInvalidMoveWithCrossing() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "r___k___\n" +
                "_R______\n" +
                "__D_____\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "d_______\n" +
                "RNBQK___\n";
        Board board = new Board(players, before);
        // Test Dragon cannot cross any piece
        assertTrue(board.getPiece(new Position("C6")) instanceof Dragon);
        assertFalse(board.movePieceByPosition(players[0], new Position("C6"), new Position("A8")));
        assertTrue(board.toString().equals(before));
    }

    public void testInvalidMoveStraight() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "r___k___\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "D_______\n" +
                "RNBQK___\n";
        Board board = new Board(players, before);
        // Test Dragon cannot cross any piece
        assertTrue(board.getPiece(new Position("A2")) instanceof Dragon);
        assertFalse(board.movePieceByPosition(players[0], new Position("A2"), new Position("A8")));
        assertTrue(board.toString().equals(before));
    }
}
