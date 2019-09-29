package model.pieces;

import model.Game;
import model.pieces.Cannon;
import model.MovementTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CannonTest extends MovementTest {
    @Test
    public void testFullName() {
        assertEquals("Cannon", new Cannon(0, 0, players[0]).getFullName());
    }

    @Test
    public void testInvalidDirection() {
        initializeBoard(null, true);
        // Tests Cannon cannot leap diagonally
        testType("G1", Cannon.class);
        testIllegalMove(0, "G1,A7");
    }

    @Test
    public void testMoveNormally() {
        initializeBoard(null, true);
        String after =
                "rnbqkdcr\n" +
                "ppppppCp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "PPPPPPPP\n" +
                "RNBQKD_R\n";
        // Tests Cannon can leap over one hurdle
        testType("G1", Cannon.class);
        testLegalMove(0, "G1,G7", after, Game.Status.CONTINUE);
    }

    @Test
    public void testInvalidMoveWithoutCapture() {
        initializeBoard(null, true);
        // Tests Cannon cannot move without capture
        testType("G8", Cannon.class);
        testIllegalMove(1, "G8,G6");
    }

    @Test
    public void testInvalidLeap() {
        String before =
                "rnbqkdcr\n" +
                "pppppp_p\n" +
                "______p_\n" +
                "________\n" +
                "________\n" +
                "______P_\n" +
                "PPPPPP_P\n" +
                "RNBQKDCR\n";
        initializeBoard(before, true);
        testType("G1", Cannon.class);
        // Tests Cannon cannot leap without hurdle
        testIllegalMove(0, "G1,G2");
        // Tests Cannon cannot leap too far, i.e. two or more hurdles
        testIllegalMove(0, "G1,G8");

    }
}
