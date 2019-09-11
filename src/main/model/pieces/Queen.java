package main.model.pieces;

import main.model.*;

public class Queen extends Piece {
    public Queen(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(int x, int y) {
        // TODO: Implement Queen's rules
        return true;
    }

    @Override
    public String toString() {
        return "Q";
    }
}
