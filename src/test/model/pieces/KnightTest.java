package model.pieces;

import model.Game;
import model.pieces.Knight;
import model.MovementTest;
import org.junit.jupiter.api.Test;

public class KnightTest extends MovementTest {
    @Test
    public void testMoveNormally() {
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "____p___\n" +
                "__P_____\n" +
                "________\n" +
                "PP_PPPPP\n" +
                "RNBQKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "____p___\n" +
                "__P_____\n" +
                "N_______\n" +
                "PP_PPPPP\n" +
                "R_BQKBNR\n";
        initializeBoard(before, false);
        testType("B1", Knight.class);
        testLegalMove(0, "B1,A3", after, Game.Status.CONTINUE);
    }

    @Test
    public void testInvalidDirection() {
        String before =
                "rnbqk_nr\n" +
                "ppppbppp\n" +
                "________\n" +
                "____p___\n" +
                "__P_____\n" +
                "__N_____\n" +
                "PP_PPPPP\n" +
                "R_BQKBNR\n";
        initializeBoard(before, false);
        testType("C3", Knight.class);
        // Test if Knight can neither move straight nor move diagonally
        testIllegalMove(0, "C3,C2");
        testIllegalMove(0, "C3,B4");
        testIllegalMove(0, "C3,B3");
    }
}
