package main.model;

import main.model.pieces.*;

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

    public String toString() {
        StringBuilder resBuilder = new StringBuilder();
        for (Piece[] row : boardStatus) {
            for (Piece piece : row) {
                if (piece == null) resBuilder.append('_');
                else resBuilder.append(piece.toString());
            }
            resBuilder.append('\n');
        }
        return resBuilder.toString();
    }
}
