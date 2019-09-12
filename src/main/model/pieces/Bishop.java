package main.model.pieces;

import main.model.*;

public class Bishop extends Piece {
    public Bishop(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(int x_dest, int y_dest) {
        // Check if direction is legal
        Direction dir = Position.getDirection(x_curr, y_curr, x_dest, y_dest);
        switch (dir) {
            case UP_LEFT: case UP_RIGHT: case DOWN_LEFT: case DOWN_RIGHT:
                break;
            default:
                return false;
        }
        // TODO: Check if it doesn't cross any pieces
        return true;
    }

    @Override
    public String toString() {
        return "B";
    }
}
