package model.pieces;

import main.model.*;
import main.model.pieces.*;
import model.MovementTest;
import org.junit.jupiter.api.Test;

public class PawnTest extends MovementTest {
    @Test
    public void testInvalidDirection() {
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "__P_p___\n" +
                "________\n" +
                "________\n" +
                "PP_PPPPP\n" +
                "RNBQKBNR\n";
        initializeBoard(before, false);
        testType("C5", Pawn.class);
        testType("A2", Pawn.class);
        testType("E5", Pawn.class);
        // Test Pawn cannot move horizontally and the board stays the same
        testIllegalMove(0,  "C5,E5");
        // Test Pawn cannot move more than two squares
        testIllegalMove(0,  "A2,A5");
        // Tests Pawn cannot move back
        testIllegalMove(0,  "C5,C4");
        testIllegalMove(1,  "E5,E6");
    }

    @Test
    public void testMoveOneSquare() {
        String after =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "__P_____\n" +
                "PP_PPPPP\n" +
                "RNBQKBNR\n";
        initializeBoard(null, false);
        testLegalMove(0,  "C2,C3", after, Game.Status.CONTINUE);
    }

    @Test
    public void testInvalidMoveOneSquare() {
        String before =
                "rnbqkbnr\n" +
                "pp_ppppp\n" +
                "__p_____\n" +
                "__P_____\n" +
                "________\n" +
                "________\n" +
                "PP_PPPPP\n" +
                "RNBQKBNR\n";
        initializeBoard(before, false);
        testType("C5", Pawn.class);
        testType("C6", Pawn.class);
        // Test Pawn cannot capture pieces if it moves straight and the board stays the same
        testIllegalMove(0,  "C5,C6");
        testIllegalMove(1,  "C6,C5");
    }

    @Test
    public void testMoveTwoSquares() {
        String after =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "__P_____\n" +
                "________\n" +
                "PP_PPPPP\n" +
                "RNBQKBNR\n";
        initializeBoard(null, false);
        testType("C2", Pawn.class);
        testLegalMove(0,  "C2,C4", after, Game.Status.CONTINUE);
    }

    @Test
    public void testInvalidMoveTwoSquares() {
        String before =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "_P__N___\n" +
                "B______P\n" +
                "P_PPPPP_\n" +
                "R__QKBNR\n";
        initializeBoard(before, false);
        testType("H3", Pawn.class);
        testType("A2", Pawn.class);
        testType("E2", Pawn.class);
        // Tests Pawn cannot move two squares after its first move
        testIllegalMove(0,  "H3,H5");
        // Tests Pawn cannot cross or occupy
        testIllegalMove(0,  "A2,A4");
        testIllegalMove(0,  "E2,E4");
    }

    @Test
    public void testCapture() {
        String before =
                "rnbqkbnr\n" +
                "ppppp_pp\n" +
                "________\n" +
                "_____p__\n" +
                "______P_\n" +
                "________\n" +
                "PPPPPP_P\n" +
                "RNBQKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "ppppp_pp\n" +
                "________\n" +
                "________\n" +
                "______p_\n" +
                "________\n" +
                "PPPPPP_P\n" +
                "RNBQKBNR\n";
        initializeBoard(before, false);
        testType("F5", Pawn.class);
        // Tests Pawn cannot move two squares after its first move
        testLegalMove(1,  "F5,G4", after, Game.Status.CONTINUE);
    }

    @Test
    public void testInvalidDiagonalMove() {
        initializeBoard(null, false);
        testType("A2", Pawn.class);
        // Tests Pawn cannot move diagonally to an empty square
        testIllegalMove(0, "A2,B3");
    }

    @Test
    public void testInvalidCapture() {
        initializeBoard(null, false);
        testType("A2", Pawn.class);
        // Test Pawn cannot move diagonally further than one square
        testIllegalMove(0, "A2,F7");
    }
}
