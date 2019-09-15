package main.model;

import java.util.List;

public abstract class Piece {
    protected Position currPos;
    protected Player owner;

    /**
     * Constructor by its position and owner
     * @param x The x coordinate of this piece
     * @param y The y coordinate of this piece
     * @param owner The player who owns this piece
     */
    public Piece(int x, int y, Player owner) {
        currPos = new Position(x, y);
        this.owner = owner;
    }

    /**
     * Getter of x coordinate of this piece
     * @return The x coordinate of this piece
     */
    public int getX() {
        return currPos.x;
    }

    /**
     * Getter of y coordinate of this piece
     * @return The y coordinate of this piece
     */
    public int getY() {
        return currPos.y;
    }

    /**
     * Getter of owner of this piece
     * @return The owner of this piece
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Move the piece to the given destination
     * @param dest The destination for the piece to move to
     */
    public void moveTo(Position dest) {
        currPos.x = dest.x;
        currPos.y = dest.y;
    }

    /**
     * Judge whether the piece can move to the given destination according to the piece type
     * @param dest The destination for the piece to move to
     * @param destOccupied True if dest is occupied by current player's opponent
     * @param checkUnoccupied A list of positions for callee to check if they are unoccupied
     * @return True if the piece can move to dest
     *         False otherwise
     */
    public abstract boolean canMoveTo(Position dest, boolean destOccupied, List<Position> checkUnoccupied);
}
