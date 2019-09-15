package test;

import main.model.*;
import junit.framework.*;
import junit.extensions.*;
import java.lang.AssertionError;

public class InitializationTest extends TestCase {
    public void testBoardInitialization() {
        Player[] players = Game.generatePlayers("TestPlayer1", "TestPlayer2");
        Board board = new Board(players);
        String expectedBoardStr =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "PPPPPPPP\n" +
                "RNBQKBNR\n";
        System.out.println(board.toString());
        Assert.assertTrue(board.toString().equals(expectedBoardStr));
    }
}
