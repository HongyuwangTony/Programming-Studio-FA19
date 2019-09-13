package main.model.pieces;

import main.model.*;

public class Knight extends Piece {
    public Knight(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(Position dest) {
        // Check if direction is legal
        Direction dir = currPos.getDirectionTo(dest);
        return dir == Direction.KNIGHT;
    }

    @Override
    public String toString() {
        return "N";
    }
}
