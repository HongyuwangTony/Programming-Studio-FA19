package model;

import model.pieces.King;

import java.util.List;
import java.util.ArrayList;

/**
 * Player Class which contains pieces ownership of a particular player
 */
public class Player {
    // Object Members
    private int player_no;
    private int score;
    private Player opponent;
    private King king;
    private List<Piece> pieces;

    /**
     * Constructor of Player by his name and player number
     * @param player_no The player number of this player - White (0) / Black (1)
     */
    public Player(int player_no) {
        this.player_no = player_no;
        pieces = new ArrayList<>();
    }

    /**
     * Getter of player_no
     * @return The player number of this player
     */
    public int getPlayerNo() {
        return player_no;
    }

    /**
     * Getter of player_no
     * @return The player number of this player
     */
    public String getPlayerColor() {
        return player_no == 0 ? "White" : "Black";
    }

    /**
     * Getter of opponent
     * @return The opponent of this player
     */
    public Player getOpponent() {
        return opponent;
    }

    /**
     * Setter of opponent
     * @param opponent The opponent of this player
     */
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    /**
     * Getter of King
     * @return The King piece owned by this player
     */
    public King getKing() {
        return king;
    }

    /**
     * Adds the given piece to this player
     * @param piece The piece going to be owned by this player
     */
    public void addPiece(Piece piece) {
        if (piece instanceof King) this.king = (King)piece;
        pieces.add(piece);
    }

    /**
     * Removes the given piece of this player
     * @param piece The piece going to be removed from the pieces of this player
     */
    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    /**
     * Getter of pieces
     * @return The list of pieces owned by this player
     */
    public List<Piece> getPieces() {
        return pieces;
    }

    /**
     * Clears all pieces owned by this player
     */
    public void clearPieces() {
        pieces.clear();
    }

    /**
     * Getter of score
     * @return The score of this player
     */
    public int getScore() {
        return score;
    }

    /**
     * Increments the score of this player by 1
     */
    public void incScore() {
        score++;
    }

    /**
     * Sets the score of this player back to 0
     */
    public void clearScore() {
        score = 0;
    }
}
