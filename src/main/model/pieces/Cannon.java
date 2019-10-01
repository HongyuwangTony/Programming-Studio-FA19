package main.model.pieces;

import main.model.*;

import java.util.List;

/**
 * Custom Chess Piece "Cannon" from Xiangqi (Chinese Chess)
 * The Cannon can leap straight over a hurdle to capture one piece of his opponent
 * if and only if there is only one hurdle between the piece captured and the cannon
 */
public class Cannon extends Piece {
    /**
     * Constructor by its position and owner
     * @param x The x coordinate of this Cannon piece
     * @param y The y coordinate of this Cannon piece
     * @param owner The player who owns this Cannon piece
     */
    public Cannon(int x, int y, Player owner) {
        super(x, y, owner);
    }

    /**
     * Judge whether the piece can move to the given destination according to the piece type
     * NOTE: The use of parameter is special for Cannon: Cannon allows one and only one piece crossed
     * @param dest The destination for the piece to move to
     * @param destOccupied True if dest is occupied by current player's opponent
     * @param checkUnoccupied A list of positions for callee to check if they are unoccupied
     * @return True if the piece can move to dest
     *         False otherwise
     */
    public boolean canMoveTo(Position dest, boolean destOccupied, List<Position> checkUnoccupied) {
        Direction dir = currPos.getDirectionTo(dest);
        if (!dir.isStraight()) return false;
        if (!destOccupied) return false; // The Cannon must capture one piece at dest
        checkUnoccupied.addAll(currPos.getPositionsCrossed(dest, dir, false));
        return true;
    }

    /**
     * Encode the Cannon piece into String
     * Upper case for the White Player(0) and lower case for the Black Player(1)
     * @return The String representation of this Cannon piece
     */
    @Override
    public String toString() {
        return owner.getPlayerNo() == 0 ? "C" : "c";
    }
}
