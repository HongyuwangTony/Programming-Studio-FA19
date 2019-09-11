package main.model.pieces;

import main.model.*;

public class Pawn extends Piece {
    public Pawn(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(int x, int y) {
        // TODO: Implement Rook's rules
        return true;
    }

    @Override
    public String toString() {
        return "P";
    }
}
