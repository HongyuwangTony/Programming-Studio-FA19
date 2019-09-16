package test;

import junit.framework.*;
import main.model.*;
import main.model.pieces.*;

public class BasicMovementTest extends TestCase {
    // Test if a player tries to move a piece to an invalid space (off the board)
    public void testMoveToOutside() {
        Player[] players = Game.generatePlayers("White", "Black");
        Board board = new Board(players);
        String before = board.toString();
        assertTrue(board.getPiece(new Position("A1")) instanceof Rook);
        assertFalse(board.movePieceByPosition(players[0], new Position("A1"), new Position("A0")));
        assertTrue(board.toString().equals(before));
    }

    // Test if a player tries to move an empty space on the board
    public void testMoveEmptySpace() {
        Player[] players = Game.generatePlayers("White", "Black");
        Board board = new Board(players);
        String before = board.toString();
        assertNull(board.getPiece(new Position("A3")));
        assertFalse(board.movePieceByPosition(players[0], new Position("A3"), new Position("A4")));
        assertTrue(board.toString().equals(before));
    }

    // Test if a player tries to move to space already containing one of his/her pieces
    public void testMoveToHisOwn() {
        Player[] players = Game.generatePlayers("White", "Black");
        Board board = new Board(players);
        String before = board.toString();
        assertTrue(board.getPiece(new Position("D1")) instanceof Queen);
        assertTrue(board.getPiece(new Position("D2")) instanceof Pawn);
        assertFalse(board.movePieceByPosition(players[0], new Position("D1"), new Position("D2")));
        assertTrue(board.toString().equals(before));
    }
}
