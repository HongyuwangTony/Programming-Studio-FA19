package main.model.pieces;

import main.model.*;

import java.util.List;

public class Knight extends Piece {
    /**
     * Constructor by its position and owner
     * @param x The x coordinate of this Knight piece
     * @param y The y coordinate of this Knight piece
     * @param owner The player who owns this Knight piece
     */
    public Knight(int x, int y, Player owner) {
        super(x, y, owner);
    }

    /**
     * Judge whether the Knight piece can move to the given destination
     * The Knight can move in a L-shape direction
     * @param dest The destination for the Knight piece to move to
     * @param destOccupied True if dest is occupied by current player's opponent
     * @param checkUnoccupied A list of positions for callee to check if they are unoccupied
     * @return True if the Knight piece can move to dest
     *         False otherwise
     */
    @Override
    public boolean canMoveTo(Position dest, boolean destOccupied, List<Position> checkUnoccupied) {
        // Check if direction is legal
        Direction dir = currPos.getDirectionTo(dest);
        return dir == Direction.KNIGHT;
    }

    /**
     * Encode the Knight piece into String
     * Upper case for the White Player(0) and lower case for the Black Player(1)
     * @return The String representation of this Knight piece
     */
    @Override
    public String toString() {
        return owner.getPlayerNo() == 0 ? "N" : "n";
    }
}
