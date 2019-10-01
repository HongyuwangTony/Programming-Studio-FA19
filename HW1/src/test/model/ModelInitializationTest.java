package model;

import model.Piece;
import model.Player;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ModelInitializationTest extends MovementTest {
    @Test
    public void testPlayerInitialization() {
        // Test Number of Players
        assertEquals(2, players.length);
        // Test Player Name
        assertEquals("White", players[0].getPlayerColor());
        assertEquals("Black", players[1].getPlayerColor());
        // Test Player Number
        assertEquals(0, players[0].getPlayerNo());
        assertEquals(1, players[1].getPlayerNo());
        // Test Opponent status
        assertEquals(players[1], players[0].getOpponent());
        assertEquals(players[0], players[1].getOpponent());
    }

    @Test
    public void testBoardInitialization() {
        initializeBoard(null, false);
        String expectedBoardStr =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "PPPPPPPP\n" +
                "RNBQKBNR\n";
        assertEquals(expectedBoardStr, board.toString());

        for (Player player : players) {
            assertEquals(16, player.getPieces().size());
            assertNotNull(player.getKing());
            for (Piece piece : player.getPieces()) {
                assertTrue(new File(piece.getImageFileName()).exists());
            }
        }
    }

    @Test
    public void testBoardInitializationFromString() {
        String expectedBoardStr =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "PPPPPPPP\n" +
                "RNBQKBNR\n";
        initializeBoard(expectedBoardStr, false);
        assertEquals(expectedBoardStr, board.toString());
        assertEquals(16, players[0].getPieces().size());
        assertEquals(16, players[1].getPieces().size());
        assertNotNull(players[0].getKing());
        assertNotNull(players[1].getKing());
    }

    @Test
    public void testCustomPiecesInitialization() {
        initializeBoard(null, true);
        String expectedBoardStr =
                "rnbqkdcr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "PPPPPPPP\n" +
                "RNBQKDCR\n";
        assertEquals(expectedBoardStr, board.toString());
        assertEquals(16, players[0].getPieces().size());
        assertEquals(16, players[1].getPieces().size());
        assertNotNull(players[0].getKing());
        assertNotNull(players[1].getKing());
    }

    @Test
    public void testCustomPiecesInitializationFromString() {
        String expectedBoardStr =
                "rnbqkdcr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "PPPPPPPP\n" +
                "RNBQKDCR\n";
        initializeBoard(expectedBoardStr, true);
        assertEquals(expectedBoardStr, board.toString());
        assertEquals(16, players[0].getPieces().size());
        assertEquals(16, players[1].getPieces().size());
        assertNotNull(players[0].getKing());
        assertNotNull(players[1].getKing());
    }
}
