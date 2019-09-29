package model.pieces;

import main.model.*;
import main.model.pieces.*;
import model.MovementTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DragonTest extends MovementTest {
    @Test
    public void testFullName() {
        assertEquals("Dragon", new Dragon(0, 0, players[0]).getFullName());
    }

    @Test
    public void testMoveNormally() {
        String before =
                "r___k___\n" +
                "________\n" +
                "__D_____\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "d_______\n" +
                "RNBQK___\n";
        String after1 =
                "D___k___\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "d_______\n" +
                "RNBQK___\n";
        String after2 =
                "D___k___\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "dNBQK___\n";
        initializeBoard(before, true);
        // Test Dragon can move diagonally or straight
        testType("C6", Dragon.class);
        testLegalMove(0, "C6,A8", after1, Game.Status.CONTINUE);
        testType("A2", Dragon.class);
        testLegalMove(1, "A2,A1", after2, Game.Status.CONTINUE);
    }

    @Test
    public void testInvalidMoveWithCrossing() {
        String before =
                "r___k___\n" +
                "_R______\n" +
                "__D_____\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "d_______\n" +
                "RNBQK___\n";
        initializeBoard(before, true);
        // Test Dragon cannot cross any piece
        testType("C6", Dragon.class);
        testIllegalMove(0, "C6,A8");
    }

    @Test
    public void testInvalidMoveStraight() {
        String before =
                "r___k___\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "D_______\n" +
                "RNBQK___\n";
        initializeBoard(before, true);
        // Test Dragon cannot move straight by more than 1 square
        testType("A2", Dragon.class);
        testIllegalMove(0, "A2,A8");
    }
}
