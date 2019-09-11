package main.model.pieces;

import main.model.*;

public class Bishop extends Piece {
    public Bishop(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(int x, int y) {
        // TODO: Implement Bishop's rules
        return true;
    }

    @Override
    public String toString() {
        return "B";
    }
}
