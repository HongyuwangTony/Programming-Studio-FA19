package model;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class MovementTest {
    protected Player players[];
    protected Board board;

    @BeforeEach
    public void initialize() {
        players = Game.generatePlayers();
    }

    protected void initializeBoard(String strBoard, boolean custom) {
        if (strBoard == null) board = new Board(players, custom);
        else board = new Board(players, strBoard);
    }

    protected void testType(String target, Class type){
        if (type == null) assertNull(board.getPiece(new Position(target)));
        else assertTrue(type.isInstance(board.getPiece(new Position(target))));
    }

    protected void testLegalMove(int player_no, String targets, String after, Game.Status status) {
        String[] arr_target = targets.split(",");
        assertEquals(2, arr_target.length);
        Player currPlayer = players[player_no];
        Position src = new Position(arr_target[0]), dest = new Position(arr_target[1]);
        assertTrue(board.arePositionsLegal(currPlayer, src, dest)
                && board.tryMovePiece(currPlayer, src, dest));
        assertEquals(status, board.isCheckmateOrStalemate(players[player_no]));
        if (status == Game.Status.CONTINUE) assertEquals(after, board.toString());
    }

    protected void testIllegalMove(int player_no, String targets) {
        String before = board.toString();
        String[] arr_target = targets.split(",");
        assertEquals(2, arr_target.length);
        Player currPlayer = players[player_no];
        Position src = new Position(arr_target[0]), dest = new Position(arr_target[1]);
        assertFalse(board.arePositionsLegal(currPlayer, src, dest)
                && board.tryMovePiece(currPlayer, src, dest));
        assertEquals(before, board.toString());
        assertEquals(Game.Status.CONTINUE, board.isCheckmateOrStalemate(players[player_no]));
    }
}
