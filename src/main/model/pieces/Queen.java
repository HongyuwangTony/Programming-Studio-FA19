package main.model.pieces;

import main.model.*;

import java.util.List;

public class Queen extends Piece {
    /**
     * Constructor by its position and owner
     * @param x The x coordinate of this Queen piece
     * @param y The y coordinate of this Queen piece
     * @param owner The player who owns this Queen piece
     */
    public Queen(int x, int y, Player owner) {
        super(x, y, owner);
    }

    /**
     * Judge whether the Queen piece can move to the given destination
     * The Queen can move straight or diagonally without leaping over other pieces
     * @param dest The destination for the Queen piece to move to
     * @param destOccupied True if dest is occupied by current player's opponent
     * @param checkUnoccupied A list of positions for callee to check if they are unoccupied
     * @return True if the Queen piece can move to dest
     *         False otherwise
     */
    @Override
    public boolean canMoveTo(Position dest, boolean destOccupied, List<Position> checkUnoccupied) {
        // Check if direction is legal
        Direction dir = currPos.getDirectionTo(dest);
        if (!dir.isDiagonal() && !dir.isStraight()) return false;
        checkUnoccupied.addAll(currPos.getPositionsCrossed(dest, false));
        return true;
    }

    /**
     * Encode the Queen piece into String
     * Upper case for the White Player(0) and lower case for the Black Player(1)
     * @return The String representation of this Queen piece
     */
    @Override
    public String toString() {
        return owner.getPlayerNo() == 0 ? "Q" : "q";
    }
}
