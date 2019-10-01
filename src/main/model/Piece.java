package model;

import java.util.List;

/**
 * Piece Class integrating normal behaviors of a piece
 */
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
     * Getter of currPos of this piece
     * @return The current position of this piece
     */
    public Position getPosition() {
        return currPos;
    }

    /**
     * Getter of owner of this piece
     * @return The owner of this piece
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Moves the piece to the given destination
     * @param dest The destination for the piece to move to
     */
    public void moveTo(Position dest) {
        currPos = new Position(dest.x, dest.y);
    }

    /**
     * Judges whether the piece can move to the given destination according to the piece type
     * @param dest The destination for the piece to move to
     * @param destOccupied True if dest is occupied by current player's opponent
     * @param checkUnoccupied A list of positions for callee to check if they are unoccupied
     * @return True if the piece can move to dest
     *         False otherwise
     */
    public abstract boolean canMoveTo(Position dest, boolean destOccupied, List<Position> checkUnoccupied);

    /**
     * Gets the full name of this piece
     * @return The full name of this piece
     */
    public abstract String getFullName();

    /**
     * Gets the corresponding image file name of this piece
     * @return The corresponding image file name of this piece
     */
    public String getImageFileName() {
        String color = owner.getPlayerNo() == 0 ? "White" : "Black";
        return "src/main/images/" + getFullName() + color + ".png";
    }
}
