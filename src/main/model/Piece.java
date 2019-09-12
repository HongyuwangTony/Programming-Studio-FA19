package main.model;

public abstract class Piece {
    protected int x_curr;
    protected int y_curr;
    protected Player owner;

    public Piece(int x, int y, Player owner) {
        x_curr = x;
        y_curr = y;
        this.owner = owner;
    }

    public int getX() {
        return x_curr;
    }

    public int getY() {
        return y_curr;
    }

    public Player getOwner() {
        return owner;
    }

    public void moveTo(int x_dest, int y_dest) {
        x_curr = x_dest;
        y_curr = y_dest;
    }

    public abstract boolean canMoveTo(int x_dest, int y_dest);
}
