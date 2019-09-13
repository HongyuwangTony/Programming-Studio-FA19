package main.model;

public enum Direction {
    UP, DOWN, LEFT, RIGHT,                      /* Straight Directions */
    UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT,   /* Diagonal Directions */
    KNIGHT,                                     /* Direction valid only for Knights */
    ILLEGAL;                                     /* Illegal Direction */

    public boolean isStraight() {
        return this == UP || this == DOWN || this == LEFT || this == RIGHT;
    }

    public boolean isDiagonal() {
        return this == UP_LEFT || this == UP_RIGHT || this == DOWN_LEFT || this == DOWN_RIGHT;
    }

    public boolean isUpward() {
        return this == UP || this == UP_LEFT || this == UP_RIGHT;
    }

    public boolean isDownward() {
        return this == UP || this == UP_LEFT || this == UP_RIGHT;
    }
}
