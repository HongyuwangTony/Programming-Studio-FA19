package model.pieces;

import model.*;

public class Knight extends Piece {
    public Knight(int x, int y, Player owner) {
        super(x, y, owner);
    }

    public boolean canMoveTo(int x, int y) {
        // TODO: Implement Rook's rules
        return true;
    }
}
