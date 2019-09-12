package main.model.pieces;

import main.model.*;

public class Rook extends Piece {
    public Rook(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(int x_dest, int y_dest) {
        if (Position.outsideOfBoard(x_dest, y_dest)) return false;
        // Check if direction is legal
        Direction dir = Position.getDirection(x_curr, y_curr, x_dest, y_dest);
        switch (dir) {
            case UP: case DOWN: case LEFT: case RIGHT:
                break;
            default:
                return false;
        }
        // TODO: Check if it doesn't cross any pieces
        return true;
    }

    @Override
    public String toString() {
        return "R";
    }
}
