package main.model.pieces;

import main.model.*;

public class Knight extends Piece {
    public Knight(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(int x, int y) {
        // TODO: Implement Knight's rules
        return true;
    }

    @Override
    public String toString() {
        return "N";
    }
}
