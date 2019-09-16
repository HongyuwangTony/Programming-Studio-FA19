package test.pieces;

import junit.framework.TestCase;
import main.model.*;
import main.model.pieces.*;

public class QueenTest extends TestCase {
    public void testMoveVertically() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "___p____\n" +
                "________\n" +
                "PPP_PPPP\n" +
                "RNBQKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "___Q____\n" +
                "________\n" +
                "PPP_PPPP\n" +
                "RNB_KBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.movePieceByPosition(players[0], new Position("D1"), new Position("D4")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testMoveHorizontally() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "____p___\n" +
                "PPP_PPPP\n" +
                "RN_QKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "____p___\n" +
                "PPP_PPPP\n" +
                "RNQ_KBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.movePieceByPosition(players[0], new Position("D1"), new Position("C1")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testMoveDiagonally() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "____p___\n" +
                "PPP_PPPP\n" +
                "RNQ_KBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "____Q___\n" +
                "PPP_PPPP\n" +
                "RN__KBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.movePieceByPosition(players[0], new Position("C1"), new Position("E3")));
        assertTrue(board.toString().equals(after));
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testInvalidDirection() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "____Q___\n" +
                "PPP_PPPP\n" +
                "RN__KBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("E3")) instanceof Queen);
        // Test in L-shape
        assertFalse(board.movePieceByPosition(players[0], new Position("E3"), new Position("G4")));
        assertTrue(board.toString().equals(before));
    }

    public void testCannotCross() {
        Player[] players = Game.generatePlayers("White", "Black");
        String before =
                "rnbqkbnr\n" +
                "ppppp_pp\n" +
                "________\n" +
                "_____p__\n" +
                "____P___\n" +
                "P____Q__\n" +
                "_PPP_PPP\n" +
                "RNB_KBNR\n";
        Board board = new Board(players, before);
        assertTrue(board.getPiece(new Position("F3")) instanceof Queen);
        // Test if Queen cannot cross any pieces
        // Cross opponent
        assertFalse(board.movePieceByPosition(players[0], new Position("F3"), new Position("F6")));
        assertTrue(board.toString().equals(before));
        // Cross his own pieces
        assertFalse(board.movePieceByPosition(players[0], new Position("F3"), new Position("D5")));
        assertTrue(board.toString().equals(before));
    }
}
