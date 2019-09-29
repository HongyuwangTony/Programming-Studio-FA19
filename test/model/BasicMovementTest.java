package model;

import main.model.pieces.*;

import org.junit.jupiter.api.Test;

public class BasicMovementTest extends MovementTest {
    /**
     * Test if a player tries to move a piece to an invalid space (off the board)
     */
    @Test
    public void testMoveToOutside() {
        initializeBoard(null, false);
        testType("A1", Rook.class);
        testIllegalMove(0, "A1,A0");
    }

    /**
     * Test if a player tries to move an empty space on the board
     */
    @Test
    public void testMoveEmptySpace() {
        initializeBoard(null, false);
        testType("A3", null);
        testIllegalMove(0, "A3,A4");
    }

    /**
     * Test if a player tries to move to space already containing one of his/her pieces
     */
    @Test
    public void testMoveToHisOwn() {
        initializeBoard(null, false);
        testType("D1", Queen.class);
        testType("D2", Pawn.class);
        testIllegalMove(0, "D1,D2");
    }
}