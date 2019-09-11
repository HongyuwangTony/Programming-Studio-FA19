package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    // Constants
    public final static int WIDTH = 8;
    public final static int HEIGHT = 8;

    // Object Members
    private List<Piece> piecesAlive;

    public Board() {
        piecesAlive = new ArrayList<>();
    }

    public void addPieces(List<Piece> pieces) {
        piecesAlive.addAll(pieces);
    }
}
