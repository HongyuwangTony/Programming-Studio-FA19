package model.pieces;

import main.model.*;
import main.model.pieces.*;
import model.MovementTest;
import org.junit.jupiter.api.Test;

public class RookTest extends MovementTest {
    @Test
    public void testMoveVertically() {
        String before =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "__N_____\n" +
                "PPPPPPPP\n" +
                "R_BQKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppppppp\n" +
                "________\n" +
                "________\n" +
                "________\n" +
                "__N_____\n" +
                "PPPPPPPP\n" +
                "_RBQKBNR\n";
        initializeBoard(before, false);
        testType("A1", Rook.class);
        testLegalMove(0, "A1,B1", after, Game.Status.CONTINUE);
    }

    @Test
    public void testMoveHorizontally() {
        String before =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "____p___\n" +
                "________\n" +
                "P_______\n" +
                "________\n" +
                "_PPPPPPP\n" +
                "RNBQKBNR\n";
        String after =
                "rnbqkbnr\n" +
                "pppp_ppp\n" +
                "____p___\n" +
                "________\n" +
                "P_______\n" +
                "R_______\n" +
                "_PPPPPPP\n" +
                "_NBQKBNR\n";
        initializeBoard(before, false);
        testType("A1", Rook.class);
        testLegalMove(0, "A1,A3", after, Game.Status.CONTINUE);
    }

    @Test
    public void testInvalidDirection() {
        String before =
                "rnbqkbnr\n" +
                "p_pppppp\n" +
                "_p______\n" +
                "________\n" +
                "________\n" +
                "_P______\n" +
                "P_PPPPPP\n" +
                "RNBQKBNR\n";
        initializeBoard(before, false);
        testType("A1", Rook.class);
        // Tests if Rook cannot move diagonally
        testIllegalMove(0, "A1,B2");
    }

    @Test
    public void testCannotCross() {
        String before =
                "rnbqkbnr\n" +
                "__pppppp\n" +
                "_p______\n" +
                "________\n" +
                "p_______\n" +
                "___PB___\n" +
                "_PP_PPPP\n" +
                "RN_QKBNR\n";
        initializeBoard(before, false);
        testType("A1", Rook.class);
        // Tests if Rook cannot cross any pieces
        // Crosses opponent
        testIllegalMove(0, "A1,A5");
        // Crosses his own pieces
        testIllegalMove(0, "A1,C1");
    }
}
