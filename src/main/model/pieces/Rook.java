package main.model.pieces;

import main.model.*;

import java.util.List;

public class Rook extends Piece {
    /**
     * Constructor by its position and owner
     * @param x The x coordinate of this Rook piece
     * @param y The y coordinate of this Rook piece
     * @param owner The player who owns this Rook piece
     */
    public Rook(int x, int y, Player owner) {
        super(x, y, owner);
    }

    /**
     * Judge whether the Rook piece can move to the given destination
     * The Rook can move straight without leaping over other pieces
     * @param dest The destination for the Rook piece to move to
     * @param checkOccupied A list of positions for callee to check if they are occupied
     * @param checkUnoccupied A list of positions for callee to check if they are unoccupied
     * @return True if the Rook piece can move to dest
     *         False otherwise
     */
    @Override
    public boolean canMoveTo(Position dest, List<Position> checkOccupied, List<Position> checkUnoccupied) {
        // Check if direction is legal
        Direction dir = currPos.getDirectionTo(dest);
        if (!dir.isStraight()) return false;
        checkUnoccupied.addAll(currPos.getPositionsCrossed(dest, false));
        return true;
    }

    /**
     * Encode the Rook piece into String
     * Upper case for the White Player(0) and lower case for the Black Player(1)
     * @return The String representation of this Rook piece
     */
    @Override
    public String toString() {
        return owner.getPlayerNo() == 0 ? "R" : "r";
    }
}
