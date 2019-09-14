package main.model;

import main.model.pieces.*;

import java.util.ArrayList;
import java.util.List;

public class Board {
    // Constants
    public final static int WIDTH = 8;
    public final static int HEIGHT = 8;

    // Object Members
    private Piece[][] boardStatus;

    public Board(Player[] players) {
        boardStatus = new Piece[HEIGHT][WIDTH];

        int arr_y_pawns[] = new int[]{1, 6}, arr_y_others[] = new int[]{0, 7};
        for (int player_no = 0; player_no < players.length; player_no++) {
            Player player = players[player_no];

            // Allocate Rooks, Knights, Bishops, King, Queen to both players
            int y_others = arr_y_others[player_no];
            player.addKing(new King(4, y_others, player));
            player.addPiece(new Queen(3, y_others, player));
            player.addPiece(new Bishop(2, y_others, player));
            player.addPiece(new Bishop(5, y_others, player));
            player.addPiece(new Knight(1, y_others, player));
            player.addPiece(new Knight(6, y_others, player));
            player.addPiece(new Rook(0, y_others, player));
            player.addPiece(new Rook(7, y_others, player));

            // Allocate Pawns to both players
            int y_pawns = arr_y_pawns[player_no];
            for (int x = 0; x < WIDTH; x++) {
                player.addPiece(new Pawn(x, y_pawns, player));
            }

            // Initialize Board Status
            for (Piece piece : player.getPieces()) {
                int x = piece.getX();
                int y = piece.getY();
                boardStatus[y][x] = piece;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder resBuilder = new StringBuilder();
        // Last row (8A-8H) on the top
        for (int i = boardStatus.length - 1; i >= 0; i--) {
            Piece[] row = boardStatus[i];
            for (Piece piece : row) {
                if (piece == null) resBuilder.append('_');
                else resBuilder.append(piece.toString());
            }
            resBuilder.append('\n');
        }
        return resBuilder.toString();
    }

    public Piece getPiece(Position position) {
        return boardStatus[position.y][position.x];
    }

    public void movePiece(Position src, Position dest) {
        if (src.outsideOfBoard() || dest.outsideOfBoard()) return; // Try to move from/to the outside of the board

        Piece pieceSelected = getPiece(src);
        if (pieceSelected == null) return; // Try to move an empty block
        // TODO: check owner of src and dest

        List<Position> checkOccupied = new ArrayList<>(), checkUnoccupied = new ArrayList<>();
        if (!pieceSelected.canMoveTo(dest, checkOccupied, checkUnoccupied)) return; // Invalid movement of the given piece
        // Check if the given positions are occupied/unoccupied
        for (Position toBeOccupied : checkOccupied) {
            if (getPiece(toBeOccupied) == null) return;
        }
        for (Position toBeUnoccupied : checkUnoccupied) {
            if (getPiece(toBeUnoccupied) != null) return;
        }

        // TODO: Check if King is selected and if it will die
        pieceSelected.moveTo(dest);
    }

    // TODO: A setBoardFromStatus method for testing
}
