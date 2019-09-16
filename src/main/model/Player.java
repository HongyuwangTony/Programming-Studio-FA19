package main.model;

import main.model.pieces.King;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Player {
    // Object Members
    private String name;
    private int player_no;
    private Player opponent;
    private King king;
    private List<Piece> pieces;

    public Player(String name, int player_no) {
        this.name = name;
        this.player_no = player_no;
        pieces = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getPlayerNo() {
        return player_no;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public King getKing() {
        return king;
    }

    public void addPiece(Piece piece) {
        if (piece instanceof King) this.king = (King)piece;
        pieces.add(piece);
    }

    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    public List<Piece> getPieces() {
        return pieces;
    }

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
