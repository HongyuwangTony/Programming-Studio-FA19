package model.pieces;

import model.*;

public class Bishop extends Piece {
    public Bishop(int x, int y, Player owner) {
        super(x, y, owner);
    }

    public boolean canMoveTo(int x, int y) {
        // TODO: Implement Rook's rules
        return true;
    }
}
