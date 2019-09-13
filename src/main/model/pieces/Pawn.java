package main.model.pieces;

import main.model.*;

public class Pawn extends Piece {
    private boolean hasMoved = false;

    public Pawn(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public void moveTo(int x_dest, int y_dest) {
        super.moveTo(x_dest, y_dest);
        if (!hasMoved) hasMoved = true;
    }

    @Override
    public boolean canMoveTo(Position dest) {
        Direction dir = currPos.getDirectionTo(dest);
        int x_dist = Math.abs(dest.x - currPos.x);
        if (owner.getPlayerNo() == 0) { // If it's White Player's round
            if (dir == Direction.UP) {
                if ((x_dist == 2 && hasMoved) || x_dist > 2) return false; // Either move 2 in the 1st round or move 1
                // TODO: Check if it doesn't cross or capture any pieces
                return true;
            } else if (dir == Direction.UP_LEFT || dir == Direction.UP_RIGHT) {
                // TODO: Check if it does capture a piece
                return true;
            } else return false; // Illegal Direction
        } else { // If it's Black Player's round
            if (dir == Direction.DOWN) {
                if ((x_dist == 2 && hasMoved) || x_dist > 2) return false; // Either move 2 in the 1st round or move 1
                // TODO: Check if it doesn't cross or capture any pieces
                return true;
            } else if (dir == Direction.DOWN_LEFT || dir == Direction.DOWN_RIGHT) {
                // TODO: Check if it does capture a piece
                return true;
            } else return false; // Illegal Direction
        }
    }

    @Override
    public String toString() {
        return "P";
    }
}
