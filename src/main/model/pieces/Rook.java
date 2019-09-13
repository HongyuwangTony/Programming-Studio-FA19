package main.model.pieces;

import main.model.*;

public class Rook extends Piece {
    public Rook(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(Position dest) {
        // Check if direction is legal
        Direction dir = currPos.getDirectionTo(dest);
        if (!dir.isStraight()) return false;
        // TODO: Check if it doesn't cross any pieces
        return true;
    }

    @Override
    public String toString() {
        return "R";
    }
}
