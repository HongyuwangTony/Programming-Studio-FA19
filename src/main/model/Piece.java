package main.model;

public abstract class Piece {
    protected Position currPos;
    protected Player owner;

    public Piece(int x, int y, Player owner) {
        currPos = new Position(x, y);
        this.owner = owner;
    }

    public int getX() {
        return currPos.x;
    }

    public int getY() {
        return currPos.y;
    }

    public Player getOwner() {
        return owner;
    }

    public void moveTo(int x_dest, int y_dest) {
        currPos.x = x_dest;
        currPos.y = y_dest;
    }

    public abstract boolean canMoveTo(Position dest);
}
