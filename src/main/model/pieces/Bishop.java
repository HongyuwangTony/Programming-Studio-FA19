package main.model.pieces;

import main.model.*;

public class Bishop extends Piece {
    public Bishop(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(Position dest) {
        // Check if direction is legal
        Direction dir = currPos.getDirectionTo(dest);
        if (!dir.isDiagonal()) return false;
        // TODO: Check if it doesn't cross any pieces
        return true;
    }

    @Override
    public String toString() {
        return "B";
    }
}
