package main.model;

import main.model.pieces.King;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    // Object Members
    private String name;
    private int player_no;
    private King king;
    private List<Piece> pieces;

    public Player(String name, int player_no) {
        this.name = name;
        this.player_no = player_no;
        pieces = new ArrayList<>();
    }

    public int getPlayerNo() {
        return player_no;
    }

    public void addKing(King king) {
        this.king = king;
        addPiece(king);
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public Position[] takeAction(Board board) {
        // TODO: Optimize input from user
        Scanner scan = new Scanner(System.in);
        Position src = new Position(scan.nextLine());
        Position dest = new Position(scan.nextLine());
        return new Position[]{src, dest};
    }
}
