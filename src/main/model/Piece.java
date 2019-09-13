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

    public void moveTo(Position dest) {
        currPos.x = dest.x;
        currPos.y = dest.y;
    }

    public abstract boolean canMoveTo(Position dest);
}
