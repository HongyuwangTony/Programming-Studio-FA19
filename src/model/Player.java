package model;

import model.pieces.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    // Object Members
    private String name;
    private List<Piece> pieces; // NOTE: Force King to be the head of piecesAlive

    public Player(String name) {
        this.name = name;
        // TODO: Add member to identify the position on the board
        initiatePieces();
    }

    public void initiatePieces() {
        pieces = new ArrayList<>();
        pieces.add(new King());
        pieces.add(new Queen());
        pieces.addAll(Collections.nCopies(2, new Rook()));
        pieces.addAll(Collections.nCopies(2, new Bishop()));
        pieces.addAll(Collections.nCopies(2, new Knight()));
        pieces.addAll(Collections.nCopies(8, new Pawn()));
        // TODO: Initialize Positions of these pieces
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void takeAction(Board board) {

    }
}
