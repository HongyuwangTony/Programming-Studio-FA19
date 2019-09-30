package model;

/**
 * Command Class for storing executed commands from players
 */
public class Command {
    private Position src;
    private Position dest;
    private Piece pieceCaptured;

    /**
     * Constructor of Command Object
     * @param src The source of the piece in the command
     * @param dest The destination of the piece in the command
     * @param pieceCaptured A non-null Piece object if there is such a piece captured
     */
    public Command(Position src, Position dest, Piece pieceCaptured) {
        this.src = src;
        this.dest = dest;
        this.pieceCaptured = pieceCaptured;
    }

    /**
     * Getter of src
     * @return The source of the piece in the command
     */
    public Position getSrc() {
        return src;
    }

    /**
     * Getter of dest
     * @return The destination of the piece in the command
     */
    public Position getDest() {
        return dest;
    }

    /**
     * Getter of pieceCaptured
     * @return Null if no piece is captured
     *         A Piece object that the piece in the command captures otherwise
     */
    public Piece getPieceCaptured() {
        return pieceCaptured;
    }
}
