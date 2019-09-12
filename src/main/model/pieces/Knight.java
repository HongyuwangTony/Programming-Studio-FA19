package main.model.pieces;

import main.model.*;

public class Knight extends Piece {
    public Knight(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(int x_dest, int y_dest) {
        int x_src = getX();
        int y_src = getY();
        // Check if direction is legal
        Direction dir = Position.getDirection(x_src, y_src, x_dest, y_dest);
        return dir == Direction.KNIGHT;
    }

    @Override
    public String toString() {
        return "N";
    }
}
