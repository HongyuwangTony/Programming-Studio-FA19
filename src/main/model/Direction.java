package model;

/**
 * Direction Class
 * Helper class for the direction from one Position object to another
 */
public enum Direction {
    UP, DOWN, LEFT, RIGHT,                      /* Straight Directions */
    UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT,   /* Diagonal Directions */
    KNIGHT,                                     /* Direction valid only for Knights */
    ILLEGAL;                                     /* Illegal Direction */

    /**
     * Category of Straight Direction along either x-axis or y-axis
     * @return True if it is a straight direction
     */
    public boolean isStraight() {
        return this == UP || this == DOWN || this == LEFT || this == RIGHT;
    }

    /**
     * Category of Diagonal Direction whose x and y distance are the same
     * @return True if it is a diagonal direction
     */
    public boolean isDiagonal() {
        return this == UP_LEFT || this == UP_RIGHT || this == DOWN_LEFT || this == DOWN_RIGHT;
    }

    /**
     * Category of Upward Direction which is straight or diagonal and has increasing y-coordinate
     * @return True if it is a upward direction
     */
    public boolean isUpward() {
        return this == UP || this == UP_LEFT || this == UP_RIGHT;
    }

    /**
     * Category of Downward Direction which is straight or diagonal and has decreasing y-coordinate
     * @return True if it is a downward direction
     */
    public boolean isDownward() {
        return this == DOWN || this == DOWN_LEFT || this == DOWN_RIGHT;
    }

    /**
     * Category of Leftward Direction which is straight or diagonal and has decreasing x-coordinate
     * @return True if it is a leftward direction
     */
    public boolean isLeftWard() {
        return this == LEFT || this == UP_LEFT || this == DOWN_LEFT;
    }

    /**
     * Category of Leftward Direction which is straight or diagonal and has increasing x-coordinate
     * @return True if it is a rightward direction
     */
    public boolean isRightWard() {
        return this == RIGHT || this == UP_RIGHT || this == DOWN_RIGHT;
    }
}
