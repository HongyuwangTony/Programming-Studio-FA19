package test;

import junit.framework.*;
import main.model.*;
import main.model.pieces.*;

public class RookTest extends TestCase {
    public void testMoveVertically() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "__N_____\n" +
                "PPPPPPPP\n" +
                "R_BQKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "__N_____\n" +
                "PPPPPPPP\n" +
                "_RBQKBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.movePieceByPosition(players[0], new Position("A1"), new Position("B1")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testMoveHorizontally() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "____p___\n" +
                "________\n" +
                "P_______\n" +
                "________\n" +
                "_PPPPPPP\n" +
                "RNBQKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "____p___\n" +
                "________\n" +
                "P_______\n" +
                "R_______\n" +
                "_PPPPPPP\n" +
                "_NBQKBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.movePieceByPosition(players[0], new Position("A1"), new Position("A3")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testInvalidDirection() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "p_pppppp\n" +
                "_p______\n" +
                "________\n" +
                "________\n" +
                "_P______\n" +
                "P_PPPPPP\n" +
                "RNBQKBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("A1")) instanceof Rook);
        // Test if Rook can move diagonally
        assertFalse(board.movePieceByPosition(players[0], new Position("A1"), new Position("B2")));
        assertTrue(board.toString().equals(before));
    }

    public void testCannotCross() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "__pppppp\n" +
                "_p______\n" +
                "________\n" +
                "p_______\n" +
                "___PB___\n" +
                "_PP_PPPP\n" +
                "RN_QKBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("A1")) instanceof Rook);
        // Test if Queen cannot cross any pieces
        // Cross opponent
        assertFalse(board.movePieceByPosition(players[0], new Position("A1"), new Position("A5")));
        assertTrue(board.toString().equals(before));
        // Cross his own pieces
        assertFalse(board.movePieceByPosition(players[0], new Position("A1"), new Position("C1")));
        assertTrue(board.toString().equals(before));
    }
}
