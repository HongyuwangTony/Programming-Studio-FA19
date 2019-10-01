package model.pieces;

import model.*;

import java.util.List;

/**
 * Pawn Class in the normal set of chess pieces
 */
public class Pawn extends Piece {
    private Position initPos;

    /**
     * Constructor by its position and owner
     * @param x The x coordinate of this Pawn piece
     * @param y The y coordinate of this Pawn piece
     * @param owner The player who owns this Pawn piece
     * @param hasMoved True if this Pawn piece has already made its first move
     */
    public Pawn(int x, int y, Player owner, boolean hasMoved) {
        super(x, y, owner);
        if (!hasMoved) initPos = new Position(x, y);
    }

    /**
     * Checks if this Pawn has moved
     * @return True if this Pawn has moved
     */
    private boolean hasMoved() {
        return !currPos.equals(initPos);
    }

    /**
     * Judges whether the Pawn piece can move to the given destination
     * The Pawn may move forward to the unoccupied square immediately in front of it on the same file;
     * or on its first move it may advance two squares along the same file provided both squares are unoccupied;
     * or it may move to a square occupied by an opponent's piece which is diagonally in front of it on an adjacent file, capturing that piece.
     * @param dest The destination for the Pawn piece to move to
     * @param destOccupied True if dest is occupied by current player's opponent
     * @param checkUnoccupied A list of positions for callee to check if they are unoccupied
     * @return True if the Pawn piece can move to dest
     *         False otherwise
     */
    @Override
    public boolean canMoveTo(Position dest, boolean destOccupied, List<Position> checkUnoccupied) {
        Direction dir = currPos.getDirectionTo(dest);
        int y_dist = Math.abs(dest.y - currPos.y);

        // Pawn of White Player should go upward. Pawn of Black Player should go downward.
        if (!(owner.getPlayerNo() == 0 && dir.isUpward()) && !(owner.getPlayerNo() == 1 && dir.isDownward()))
            return false;

        if (dir.isStraight()) { // Move straight
            if ((y_dist == 2 && hasMoved()) || y_dist > 2) return false; // Either move 2 in the 1st round or move 1
            checkUnoccupied.addAll(currPos.getPositionsCrossed(dest, dir, true));
            return true;
        } else { // Move Diagonally otherwise
            if (y_dist > 1) return false;
            return destOccupied; // Should capture his opponent's piece in this case
        }
    }

    /**
     * Encodes the Pawn piece into String
     * Upper case for the White Player(0) and lower case for the Black Player(1)
     * @return The String representation of this Pawn piece
     */
    @Override
    public String toString() {
        return owner.getPlayerNo() == 0 ? "P" : "p";
    }

    /**
     * Gets the full name of this piece
     * @return The full name of this piece
     */
    @Override
    public String getFullName() {
        return "Pawn";
    }
}
