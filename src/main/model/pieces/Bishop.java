package main.model.pieces;

import main.model.*;

public class Bishop extends Piece {
    /**
     * Constructor by its position and owner
     * @param x The x coordinate of this Bishop piece
     * @param y The y coordinate of this Bishop piece
     * @param owner The player who owns this Bishop piece
     */
    public Bishop(int x, int y, Player owner) {
        super(x, y, owner);
    }

    /**
     * Judge whether the Bishop piece can move to the given destination
     * The Bishop can move diagonally without leaping over other pieces
     * @param dest The destination for the Bishop piece to move to
     * @return True if the Bishop piece can move to dest
     *         False otherwise
     */
    @Override
    public boolean canMoveTo(Position dest) {
        // Check if direction is legal
        Direction dir = currPos.getDirectionTo(dest);
        if (!dir.isDiagonal()) return false;
        // TODO: Check if it doesn't cross any pieces
        return true;
    }

    /**
     * Encode the Bishop piece into String
     * Upper case for the White Player(0) and lower case for the Black Player(1)
     * @return The String representation of this Bishop piece
     */
    @Override
    public String toString() {
        return owner.getPlayerNo() == 0 ? "B" : "b";
    }
}
