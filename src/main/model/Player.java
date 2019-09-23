package main.model;

import main.model.pieces.King;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Player Class which contains pieces ownership of a particular player
 */
public class Player {
    // Object Members
    private String name;
    private int player_no;
    private Player opponent;
    private King king;
    private List<Piece> pieces;

    /**
     * Constructor of Player by his name and player number
     * @param name The name of this player
     * @param player_no The player number of this player - White (0) / Black (1)
     */
    public Player(String name, int player_no) {
        this.name = name;
        this.player_no = player_no;
        pieces = new ArrayList<>();
    }

    /**
     * Getter of name
     * @return The name of this player
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of player_no
     * @return The player number of this player
     */
    public int getPlayerNo() {
        return player_no;
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
     * Reads input from the given input stream to get source and destination
     * @param inputStream The input stream that reads user input
     * @return A tuple of positions [src, dest]
     */
    public Position[] takeAction(InputStream inputStream) {
        Scanner scan = new Scanner(inputStream);
        Position src, dest;
        try {
            src = new Position(scan.nextLine());
            dest = new Position(scan.nextLine());
        } catch (NoSuchElementException e) {
            return null;
        }
        return new Position[]{src, dest};
    }
}
