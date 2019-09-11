package model.pieces;

import model.*;

public class Queen extends Piece {
    public Queen(int x, int y, Player owner) {
        super(x, y, owner);
    }

    public boolean canMoveTo(int x, int y) {
        // TODO: Implement Rook's rules
        return true;
    }
}
