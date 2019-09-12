package main.model;

public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static boolean outsideOfBoard(int x, int y) {
        if (x < 0 || x >= Board.WIDTH) return true;
        if (y < 0 || y >= Board.HEIGHT) return true;
        return false;
    }

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
