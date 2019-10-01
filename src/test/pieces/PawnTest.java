package test.pieces;

import junit.framework.*;
import main.model.*;
import main.model.pieces.*;

public class PawnTest extends TestCase {
    public void testInvalidDirection() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "__P_p___\n" +
                "________\n" +
                "________\n" +
                "PP_PPPPP\n" +
                "RNBQKBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("C5")) instanceof Pawn);
        assertTrue(board.getPiece(new Position("A2")) instanceof Pawn);
        assertTrue(board.getPiece(new Position("E5")) instanceof Pawn);
        // Test Pawn cannot move horizontally and the board stays the same
        assertFalse(board.movePieceByPosition(players[0], new Position("C5"), new Position("A5")));
        assertTrue(board.toString().equals(before));
        // Test Pawn cannot move more than two squares
        assertFalse(board.movePieceByPosition(players[0], new Position("A2"), new Position("A5")));
        assertTrue(board.toString().equals(before));
        // Test Pawn cannot move back
        assertFalse(board.movePieceByPosition(players[0], new Position("C5"), new Position("C4")));
        assertTrue(board.toString().equals(before));
        assertFalse(board.movePieceByPosition(players[0], new Position("E5"), new Position("E6")));
        assertTrue(board.toString().equals(before));
    }

    public void testMoveOneSquare() {
        Player[] players = Game.generatePlayers("White", "Black");
        Board board = new Board(players, false);
        String after =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "__P_____\n" +
                "PP_PPPPP\n" +
                "RNBQKBNR\n";
        assertTrue(board.movePieceByPosition(players[0], new Position("C2"), new Position("C3")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[1]));
    }

    public void testInvalidMoveOneSquare() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "pp_ppppp\n" +
                "__p_____\n" +
                "__P_____\n" +
                "________\n" +
                "________\n" +
                "PP_PPPPP\n" +
                "RNBQKBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("C5")) instanceof Pawn);
        assertTrue(board.getPiece(new Position("C6")) instanceof Pawn);
        // Test Pawn cannot capture pieces if it moves straight and the board stays the same
        assertFalse(board.movePieceByPosition(players[0], new Position("C5"), new Position("C6")));
        assertTrue(board.toString().equals(before));
        assertFalse(board.movePieceByPosition(players[1], new Position("C6"), new Position("C5")));
        assertTrue(board.toString().equals(before));
    }

    public void testMoveTwoSquares() {
        Player[] players = Game.generatePlayers("White", "Black");
        Board board = new Board(players, false);
        String after =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "__P_____\n" +
                "________\n" +
                "PP_PPPPP\n" +
                "RNBQKBNR\n";
        assertTrue(board.movePieceByPosition(players[0], new Position("C2"), new Position("C4")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[1]));
    }

    public void testInvalidMoveTwoSquares() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "_P__N___\n" +
                "B______P\n" +
                "P_PPPPP_\n" +
                "R__QKBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("H3")) instanceof Pawn);
        assertTrue(board.getPiece(new Position("A2")) instanceof Pawn);
        assertTrue(board.getPiece(new Position("E2")) instanceof Pawn);
        // Test Pawn cannot move two squares after its first move
        assertFalse(board.movePieceByPosition(players[0], new Position("H3"), new Position("H5")));
        assertTrue(board.toString().equals(before));
        // Test Pawn cannot cross or occupy
        assertFalse(board.movePieceByPosition(players[0], new Position("A2"), new Position("A4")));
        assertTrue(board.toString().equals(before));
        assertFalse(board.movePieceByPosition(players[0], new Position("E2"), new Position("E4")));
        assertTrue(board.toString().equals(before));
    }

    public void testCapture() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "ppppp_pp\n" +
                "________\n" +
                "_____p__\n" +
                "______P_\n" +
                "________\n" +
                "PPPPPP_P\n" +
                "RNBQKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "ppppp_pp\n" +
                "________\n" +
                "________\n" +
                "______p_\n" +
                "________\n" +
                "PPPPPP_P\n" +
                "RNBQKBNR\n";
        Board board = new Board(players, before);
        // Test Pawn cannot move two squares after its first move
        assertTrue(board.movePieceByPosition(players[1], new Position("F5"), new Position("G4")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[1]));
    }

    public void testInvalidDiagonalMove() {
        Player[] players = Game.generatePlayers("White", "Black");
        Board board = new Board(players, false);
        String before = board.toString();
        assertTrue(board.getPiece(new Position("A2")) instanceof Pawn);
        // Test Pawn cannot move diagonally to an empty square
        assertFalse(board.movePieceByPosition(players[1], new Position("A2"), new Position("B3")));
        assertTrue(board.toString().equals(before));
    }

    public void testInvalidCapture() {
        Player[] players = Game.generatePlayers("White", "Black");
        Board board = new Board(players, false);
        String before = board.toString();
        assertTrue(board.getPiece(new Position("A2")) instanceof Pawn);
        // Test Pawn cannot move diagonally further than one square
        assertFalse(board.movePieceByPosition(players[1], new Position("A2"), new Position("F7")));
        assertTrue(board.toString().equals(before));
    }
}
