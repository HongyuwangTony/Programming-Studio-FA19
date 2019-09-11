package main.model;

public abstract class Piece {
    private int x;
    private int y;
    private Player owner;

    public Piece(int x, int y, Player owner) {
        this.x = x;
        this.y = y;
        this.owner = owner;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player getOwner() {
        return owner;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract boolean canMoveTo(int x, int y);
}
