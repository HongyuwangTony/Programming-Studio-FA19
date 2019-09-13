package main.model.pieces;

import main.model.*;

public class Pawn extends Piece {
    private boolean hasMoved = false;

    public Pawn(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public void moveTo(Position dest) {
        super.moveTo(dest);
        if (!hasMoved) hasMoved = true;
    }

    @Override
    public boolean canMoveTo(Position dest) {
        Direction dir = currPos.getDirectionTo(dest);
        int x_dist = Math.abs(dest.x - currPos.x);

        // Pawn of White Player should go upward. Pawn of Black Player should go downward.
        if (!(owner.getPlayerNo() == 0 && dir.isUpward()) && !(owner.getPlayerNo() == 1 && dir.isDownward()))
            return false;

        if (dir.isStraight()) { // Move straight
            if ((x_dist == 2 && hasMoved) || x_dist > 2) return false; // Either move 2 in the 1st round or move 1
            // TODO: Check if it doesn't cross or capture any pieces
            return true;
        } else { // Move Diagonally otherwise
            // TODO: Check if it does capture a piece
            return true;
        }
    }

    @Override
    public String toString() {
        return "P";
    }
}
