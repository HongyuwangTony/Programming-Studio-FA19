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
