package main.model;

public enum Direction {
    UP, DOWN, LEFT, RIGHT,                      /* Straight Directions */
    UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT,   /* Diagonal Directions */
    KNIGHT,                                     /* Direction valid only for Knights */
    ILLEGAL                                     /* Illegal Direction */
}
