package test;

import main.model.*;
import junit.framework.*;

public class InitializationTest extends TestCase {
    public void testPlayerInitialization() {
        Player[] players = Game.generatePlayers("White", "Black");
        // Test Number of Players
        Assert.assertEquals(2, players.length);
        // Test Player Name
        Assert.assertTrue(players[0].getName().equals("White"));
        Assert.assertTrue(players[1].getName().equals("Black"));
        // Test Player Number
        Assert.assertEquals(0, players[0].getPlayerNo());
        Assert.assertEquals(1, players[1].getPlayerNo());
        // Test Opponent status
        Assert.assertEquals(players[1], players[0].getOpponent());
        Assert.assertEquals(players[0], players[1].getOpponent());
    }

    public void testBoardInitialization() {
        Player[] players = Game.generatePlayers("White", "Black");
        Board board = new Board(players, false);
        String expectedBoardStr =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "PPPPPPPP\n" +
                "RNBQKBNR\n";
        Assert.assertTrue(board.toString().equals(expectedBoardStr));
        Assert.assertEquals(16, players[0].getPieces().size());
        Assert.assertEquals(16, players[1].getPieces().size());
        Assert.assertNotNull(players[0].getKing());
        Assert.assertNotNull(players[1].getKing());
    }

    public void testBoardInitializationFromString() {
        Player[] players = Game.generatePlayers("White", "Black");
        String expectedBoardStr =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "PPPPPPPP\n" +
                "RNBQKBNR\n";
        Board board = new Board(players, expectedBoardStr);
        Assert.assertTrue(board.toString().equals(expectedBoardStr));
        Assert.assertEquals(16, players[0].getPieces().size());
        Assert.assertEquals(16, players[1].getPieces().size());
        Assert.assertNotNull(players[0].getKing());
        Assert.assertNotNull(players[1].getKing());
    }

    public void testCustomPiecesInitialization() {
        Player[] players = Game.generatePlayers("White", "Black");
        Board board = new Board(players, true);
        String expectedBoardStr =
                "rnbqkdcr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "PPPPPPPP\n" +
                "RNBQKDCR\n";
        Assert.assertTrue(board.toString().equals(expectedBoardStr));
        Assert.assertEquals(16, players[0].getPieces().size());
        Assert.assertEquals(16, players[1].getPieces().size());
        Assert.assertNotNull(players[0].getKing());
        Assert.assertNotNull(players[1].getKing());
    }

    public void testCustomPiecesInitializationFromString() {
        Player[] players = Game.generatePlayers("White", "Black");
        String expectedBoardStr =
                "rnbqkdcr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "PPPPPPPP\n" +
                "RNBQKDCR\n";
        Board board = new Board(players, expectedBoardStr);
        Assert.assertTrue(board.toString().equals(expectedBoardStr));
        Assert.assertEquals(16, players[0].getPieces().size());
        Assert.assertEquals(16, players[1].getPieces().size());
        Assert.assertNotNull(players[0].getKing());
        Assert.assertNotNull(players[1].getKing());
    }
}
