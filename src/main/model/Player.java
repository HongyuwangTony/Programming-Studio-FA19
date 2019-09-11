package main.model;

import java.util.List;

public class Player {
    // Object Members
    private String name;
    private int player_no;
    private List<Piece> pieces; // NOTE: Force King to be the head of piecesAlive

    public Player(String name, int player_no) {
        this.name = name;
        this.player_no = player_no;
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    public void takeAction(Board board) {

    }
}
