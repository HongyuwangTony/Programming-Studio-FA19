package model.pieces;

import model.Game;
import model.pieces.Bishop;
import model.MovementTest;
import org.junit.jupiter.api.Test;

public class BishopTest extends MovementTest {
    @Test
    public void testMoveNormally() {
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "____p___\n" +
                "___P____\n" +
                "________\n" +
                "PPP_PPPP\n" +
                "RNBQKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "____p___\n" +
                "___P_B__\n" +
                "________\n" +
                "PPP_PPPP\n" +
                "RN_QKBNR\n";
        initializeBoard(before, false);
        testType("C1", Bishop.class);
        testLegalMove(0, "C1,F4", after, Game.Status.CONTINUE);
    }

    @Test
    public void testInvalidDirection() {
        String before =
                "rnbqkbnr\n" +
                "pppp_pp_\n" +
                "________\n" +
                "_______p\n" +
                "___P_B__\n" +
                "________\n" +
                "PPP_PPPP\n" +
                "RN_QKBNR\n";
        initializeBoard(before, false);
        testType("F4", Bishop.class);
        // Tests Bishop cannot move straight and the board stays the same
        testIllegalMove(0, "F4,G4");
        testIllegalMove(0, "F4,F6");
    }

    @Test
    public void testCannotCross() {
        String before =
                "rnbqkbnr\n" +
                "p____ppp\n" +
                "_p_p____\n" +
                "__P_p___\n" +
                "___B_P__\n" +
                "________\n" +
                "PP_PP_PP\n" +
                "RNBQK_NR\n";
        initializeBoard(before, false);
        testType("D4", Bishop.class);
        // Tests Bishop cannot cross any pieces and the board stays the same
        // Crosses opponent
        testIllegalMove(0, "D4,B6");
        // Crosses his own piece
        testIllegalMove(0, "D4,F6");
    }
}
