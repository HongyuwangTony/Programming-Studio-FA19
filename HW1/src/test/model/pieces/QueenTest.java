package model.pieces;

import model.Game;
import model.pieces.Queen;
import model.MovementTest;
import org.junit.jupiter.api.Test;

public class QueenTest extends MovementTest {
    @Test
    public void testMoveVertically() {
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "___p____\n" +
                "________\n" +
                "PPP_PPPP\n" +
                "RNBQKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "___Q____\n" +
                "________\n" +
                "PPP_PPPP\n" +
                "RNB_KBNR\n";
        initializeBoard(before, false);
        testType("D1", Queen.class);
        testLegalMove(0, "D1,D4", after, Game.Status.CONTINUE);
    }

    @Test
    public void testMoveHorizontally() {
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "____p___\n" +
                "PPP_PPPP\n" +
                "RN_QKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "____p___\n" +
                "PPP_PPPP\n" +
                "RNQ_KBNR\n";
        initializeBoard(before, false);
        testType("D1", Queen.class);
        testLegalMove(0, "D1,C1", after, Game.Status.CONTINUE);
    }

    @Test
    public void testMoveDiagonally() {
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "____p___\n" +
                "PPP_PPPP\n" +
                "RNQ_KBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "____Q___\n" +
                "PPP_PPPP\n" +
                "RN__KBNR\n";
        initializeBoard(before, false);
        testType("C1", Queen.class);
        testLegalMove(0, "C1,E3", after, Game.Status.CONTINUE);
    }

    @Test
    public void testInvalidDirection() {
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "____Q___\n" +
                "PPP_PPPP\n" +
                "RN__KBNR\n";
        initializeBoard(before, false);
        // Tests in L-shape
        testType("E3", Queen.class);
        testIllegalMove(0, "E3,G4");
    }

    @Test
    public void testCannotCross() {
        String before =
                "rnbqkbnr\n" +
                "ppppp_pp\n" +
                "________\n" +
                "_____p__\n" +
                "____P___\n" +
                "P____Q__\n" +
                "_PPP_PPP\n" +
                "RNB_KBNR\n";
        initializeBoard(before, false);
        testType("F3", Queen.class);
        // Tests if Queen cannot cross any pieces
        // Crosses opponent
        testIllegalMove(0, "F3,F6");
        // Crosses his own pieces
        testIllegalMove(0, "F3,D5");
    }
}
