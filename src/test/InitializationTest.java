package test;

import main.model.*;
import junit.framework.*;
import junit.extensions.*;
import java.lang.AssertionError;

public class InitializationTest extends TestCase {
    public void testBoardInitialization() {
        Player[] players = new Player[]{
                new Player("TestPlayer1", 0),
                new Player("TestPlayer2", 1)};
        Board board = new Board(players);
        String expectedBoardStr =
                "RNBKQBNR\n" +
                "PPPPPPPP\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "PPPPPPPP\n" +
                "RNBKQBNR\n";
        Assert.assertTrue(board.toString().equals(expectedBoardStr));
    }
}
