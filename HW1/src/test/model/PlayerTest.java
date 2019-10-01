package model;

import model.pieces.Pawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    @Test
    public void testPlayerReset() {
        Player player = new Player(0);
        for (int i = 0; i < 8; i++) {
            player.addPiece(new Pawn(i, 1, player, false));
        }
        assertEquals(8, player.getPieces().size());
        player.clearPieces();
        assertTrue(player.getPieces().isEmpty());
    }

    @Test
    public void testPlayerScore() {
        Player player = new Player(0);
        for (int i = 0; i < 3; i++) {
            player.incScore();
        }
        assertEquals(3, player.getScore());

        player.clearScore();
        assertEquals(0, player.getScore());
    }
}
