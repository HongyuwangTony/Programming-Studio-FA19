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
     * Calculate the direction from source to destination and check if it is legal in Chess Game
     * @param x_src The x coordinate of the given source
     * @param y_src The y coordinate of the given source
     * @param x_dest The x coordinate of the given destination
     * @param y_dest The y coordinate of the given destination
     * @return The direction from source to destination
     */
    public static Direction getDirection(int x_src, int y_src, int x_dest, int y_dest) {
        // Judge if the direction is straight
        if (x_src == x_dest && y_src == y_dest) return Direction.ILLEGAL;
        else if (x_src == x_dest) return x_dest > x_src ? Direction.RIGHT : Direction.LEFT;
        else if (y_src == y_dest) return y_dest > y_src ? Direction.UP : Direction.DOWN;

        // Judge if the direction is diagonal
        int x_displacement = x_dest - x_src, y_displacement = y_dest - y_src;
        if (x_displacement == y_displacement) return x_displacement > 0 ? Direction.UP_RIGHT : Direction.DOWN_LEFT;
        else if (x_displacement == - y_displacement) return x_displacement > 0 ? Direction.UP_LEFT : Direction.DOWN_RIGHT;

        // Judge if the direction is special for Knight
        if (Math.abs(x_displacement) + Math.abs(y_displacement) == 3) return Direction.KNIGHT;

        // Illegal Otherwise
        return Direction.ILLEGAL;
    }
}
