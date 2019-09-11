package main.model.pieces;

import main.model.*;

public class Rook extends Piece {
    public Rook(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(int x, int y) {
        // TODO: Implement Rook's rules
        return true;
    }

    @Override
    public String toString() {
        return "R";
    }
}
