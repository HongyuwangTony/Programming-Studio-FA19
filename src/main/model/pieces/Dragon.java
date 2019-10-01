package main.model.pieces;

import main.model.*;

import java.util.*;

/**
 * Custom Chess Piece Dragon from "Dragon Horse" in Shogi (Japanese Chess), which is originally promoted Bishop in Shogi
 * The rule is like combining movements of King with Bishop
 * Dragon cannot leap over any pieces
 * Dragon can either move diagonally with any units or move straight with one unit
 */
public class Dragon extends Piece {
    /**
     * Constructor by its position and owner
     * @param x The x coordinate of this Dragon piece
     * @param y The y coordinate of this Dragon piece
     * @param owner The player who owns this Dragon piece
     */
    public Dragon(int x, int y, Player owner) {
        super(x, y, owner);
    }

    /**
     * Judge whether the Dragon piece can move to the given destination according to the piece type
     * @param dest The destination for the piece to move to
     * @param destOccupied True if dest is occupied by current player's opponent
     * @param checkUnoccupied A list of positions for callee to check if they are unoccupied
     * @return True if the piece can move to dest
     *         False otherwise
     */
    public boolean canMoveTo(Position dest, boolean destOccupied, List<Position> checkUnoccupied) {
        Direction dir = currPos.getDirectionTo(dest);
        if (dir.isDiagonal()) { // Move diagonally with any units
            // Cannot leap over any pieces in this case
            checkUnoccupied.addAll(currPos.getPositionsCrossed(dest, dir, false));
            return true;
        } else if (dir.isStraight()) { // Move straight with one unit
            return Math.abs(dest.x - currPos.x) == 1 || Math.abs(dest.y - currPos.y) == 1;
        }
        return false;
    }

    /**
     * Encode the Dragon piece into String
     * Upper case for the White Player(0) and lower case for the Black Player(1)
     * @return The String representation of this Bishop piece
     */
    @Override
    public String toString() {
        return owner.getPlayerNo() == 0 ? "D" : "d";
    }
}
