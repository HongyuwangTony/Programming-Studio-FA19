package main.model;

public class Position {
    public int x;
    public int y;

    /**
     * Constructor by given x and y coordinates
     * @param x The x coordinate of the target position
     * @param y The y coordinate of the target position
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor by decoding the given String
     * Assume coord is ranging from A1 to H8
     * @param coord The encoded String of a given position
     */
    public Position(String coord) {
        x = coord.charAt(0) - 'A';
        y = coord.charAt(1) - '1';
    }

    /**
     * Encode the Position object into String
     * @return The String encoded by this object
     */
    @Override
    public String toString() {
        return "" + (char)('A' + x) + (1 + y);
    }

    /**
     * Judging whether the position is outside of the board
     * @return True if this position is outside of the board
     *         False otherwise
     */
    public boolean outsideOfBoard() {
        if (x < 0 || x >= Board.WIDTH) return true;
        if (y < 0 || y >= Board.HEIGHT) return true;
        return false;
    }

    /**
     * Calculate the direction from current position to destination and check if it is legal in Chess Game
     * @param dest The Position object indicating the given destination
     * @return The direction from current position to destination
     */
    public Direction getDirectionTo(Position dest) {
        // Judge if the direction is straight
        if (x == dest.x && y == dest.y) return Direction.ILLEGAL;
        else if (x == dest.x) return dest.x > x ? Direction.RIGHT : Direction.LEFT;
        else if (y == dest.y) return dest.y > y ? Direction.UP : Direction.DOWN;

        // Judge if the direction is diagonal
        int x_displacement = dest.x - x, y_displacement = dest.y - y;
        if (x_displacement == y_displacement) return x_displacement > 0 ? Direction.UP_RIGHT : Direction.DOWN_LEFT;
        else if (x_displacement == - y_displacement) return x_displacement > 0 ? Direction.UP_LEFT : Direction.DOWN_RIGHT;

        // Judge if the direction is special for Knight
        if (Math.abs(x_displacement) + Math.abs(y_displacement) == 3) return Direction.KNIGHT;

        // Illegal Otherwise
        return Direction.ILLEGAL;
    }
}
