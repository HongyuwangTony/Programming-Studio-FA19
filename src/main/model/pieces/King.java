package model.pieces;

import model.*;

import java.util.List;

/**
 * King Class in the normal set of chess pieces
 */
public class King extends Piece {
    /**
     * Constructor by its position and owner
     * @param x The x coordinate of this King piece
     * @param y The y coordinate of this King piece
     * @param owner The player who owns this King piece
     */
    public King(int x, int y, Player owner) {
        super(x, y, owner);
    }

    /**
     * Judges whether the King piece can move to the given destination
     * The King can move one square in any direction
     * Note that the rule that the King cannot put itself in danger is judged in Board class
     * @param dest The destination for the King piece to move to
     * @param destOccupied True if dest is occupied by current player's opponent
     * @param checkUnoccupied A list of positions for callee to check if they are unoccupied
     * @return True if the King piece can move to dest
     *         False otherwise
     */
    @Override
    public boolean canMoveTo(Position dest, boolean destOccupied, List<Position> checkUnoccupied) {
        // Check if direction is legal
        Direction dir = currPos.getDirectionTo(dest);
        if (!dir.isDiagonal() && !dir.isStraight()) return false;
        // Check distance
        int dist_x = Math.abs(dest.x - currPos.x), dist_y = Math.abs(dest.y - currPos.y);
        return Math.max(dist_x, dist_y) == 1; // Ensure the displacement is (+/-1, +/-1) or (0, +/-1) or (+/-1, 0)
    }

    /**
     * Encodes the King piece into String
     * Upper case for the White Player(0) and lower case for the Black Player(1)
     * @return The String representation of this King piece
     */
    @Override
    public String toString() {
        return owner.getPlayerNo() == 0 ? "K" : "k";
    }

    @Override
    public String getFullName() {
        return "King";
    }
}
