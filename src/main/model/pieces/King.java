package main.model.pieces;

import main.model.*;

public class King extends Piece {
    public King(int x, int y, Player owner) {
        super(x, y, owner);
    }

    @Override
    public boolean canMoveTo(Position dest) {
        // Check if direction is legal
        Direction dir = currPos.getDirectionTo(dest);
        if (!dir.isDiagonal() && !dir.isStraight()) return false;
        // Check distance
        int dist_x = dest.x - currPos.x, dist_y = dest.y - currPos.y;
        return Math.max(dist_x, dist_y) == 1; // Ensure the displacement is (+/-1, +/-1) or (0, +/-1) or (+/-1, 0)
    }

    @Override
    public String toString() {
        return "K";
    }
}
