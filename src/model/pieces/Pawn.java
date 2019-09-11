package model.pieces;

import model.*;

public class Pawn extends Piece {
    public Pawn(int x, int y, Player owner) {
        super(x, y, owner);
    }

    public boolean canMoveTo(int x, int y) {
        // TODO: Implement Rook's rules
        return true;
    }
}
