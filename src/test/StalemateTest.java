package test;

import junit.framework.*;
import main.model.*;

public class StalemateTest extends TestCase {
    // Tests below are from https://en.wikipedia.org/wiki/Stalemate#Simple_examples

    public void testStaleMateSimple() {
        Player[] players = Game.generatePlayers("White", "Black");
        String currStatus =
                "_______k\n" +
                "_____K__\n" +
                "______Q_\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n";
        Board board = new Board(players, currStatus);
        assertEquals(Game.Status.STALEMATE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testStaleMate1() {
        Player[] players = Game.generatePlayers("White", "Black");
        String currStatus =
                "_____k__\n" +
                "_____P__\n" +
                "_____K__\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n";
        Board board = new Board(players, currStatus);
        assertEquals(Game.Status.STALEMATE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testStaleMate2() {
        Player[] players = Game.generatePlayers("White", "Black");
        String currStatus =
                "kb_____R\n" +
                "________\n" +
                "_K______\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n";
        Board board = new Board(players, currStatus);
        assertEquals(Game.Status.STALEMATE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testStaleMate3() {
        Player[] players = Game.generatePlayers("White", "Black");
        String currStatus =
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "__K_____\n" +
                "_R______\n" +
                "k_______\n";
        Board board = new Board(players, currStatus);
        assertEquals(Game.Status.STALEMATE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testStaleMate4() {
        Player[] players = Game.generatePlayers("White", "Black");
        String currStatus =
                "________\n" +
                "________\n" +
                "________\n" +
                "______K_\n" +
                "________\n" +
                "_Q______\n" +
                "p_______\n" +
                "k_______\n";
        Board board = new Board(players, currStatus);
        assertEquals(Game.Status.STALEMATE, board.isCheckmateOrStalemate(players[0]));
    }

    public void testStaleMate5() {
        Player[] players = Game.generatePlayers("White", "Black");
        String currStatus =
                "k_______\n" +
                "P_______\n" +
                "K_______\n" +
                "________\n" +
                "_____B__\n" +
                "________\n" +
                "________\n" +
                "________\n";
        Board board = new Board(players, currStatus);
        assertEquals(Game.Status.STALEMATE, board.isCheckmateOrStalemate(players[0]));
    }
}
