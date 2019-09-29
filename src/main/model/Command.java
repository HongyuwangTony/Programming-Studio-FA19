package main.model;

public class Command {
    private Position src;
    private Position dest;
    private Piece pieceCaptured;

    public Command(Position src, Position dest, Piece pieceCaptured) {
        this.src = src;
        this.dest = dest;
        this.pieceCaptured = pieceCaptured;
    }

    public Position getSrc() {
        return src;
    }

    public Position getDest() {
        return dest;
    }

    public Piece getPieceCaptured() {
        return pieceCaptured;
    }

}
