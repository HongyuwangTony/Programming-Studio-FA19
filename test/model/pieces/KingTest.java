package model.pieces;

import main.model.*;
import main.model.pieces.*;
import model.MovementTest;
import org.junit.jupiter.api.Test;

public class KingTest extends MovementTest {
    @Test
    public void testMoveNormally() {
        String before =
                "rnbqkbnr\n" +
                "p_pp_ppp\n" +
                "________\n" +
                "____P___\n" +
                "_p______\n" +
                "__KP____\n" +
                "PPP__PPP\n" +
                "RNBQ_BNR\n";
        String after1 =
                "rnbq_bnr\n" +
                "p_ppkppp\n" +
                "________\n" +
                "____P___\n" +
                "_p______\n" +
                "__KP____\n" +
                "PPP__PPP\n" +
                "RNBQ_BNR\n";
        String after2 =
                "rnbq_bnr\n" +
                "p_ppkppp\n" +
                "________\n" +
                "____P___\n" +
                "_K______\n" +
                "___P____\n" +
                "PPP__PPP\n" +
                "RNBQ_BNR\n";
        initializeBoard(before, false);
        testType("E8", King.class);
        testType("C3", King.class);
        testLegalMove(1, "E8,E7", after1, Game.Status.CONTINUE);
        testLegalMove(0, "C3,B4", after2, Game.Status.CONTINUE);
    }

    @Test
    public void testInvalidDirection() {
        String before =
                "________\n" +
                "________\n" +
                "________\n" +
                "k__K____\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "________\n";
        initializeBoard(before, false);
        testType("A5", King.class);
        // Moves in L-shape
        testIllegalMove(1, "A5,C4");
    }

    @Test
    public void testCannotPutKingInCheck() {
        String before =
                "________\n" +
                "________\n" +
                "________\n" +
                "k_K_____\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "R_______\n";
        initializeBoard(before, false);
        testType("A5", King.class);
        testIllegalMove(1, "A5,B4");
    }
}
