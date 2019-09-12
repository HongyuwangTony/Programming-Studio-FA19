package main.model.pieces;

import main.model.*;

public class King extends Piece {
    public King(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(int x, int y) {
        // TODO: Implement King's rules
        return true;
    }

    @Override
    public String toString() {
        return "K";
    }
}