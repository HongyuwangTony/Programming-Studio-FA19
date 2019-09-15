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
                Position pos = piece.getPosition();
                boardStatus[pos.y][pos.x] = piece;
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

    public void removePiece(Position position) {
        boardStatus[position.y][position.x] = null;
    }

    public boolean movePieceByPosition(Player currPlayer, Position src, Position dest) {
        if (src.outsideOfBoard() || dest.outsideOfBoard()) return false; // Try to move from/to the outside of the board

        Piece pieceSrc = getPiece(src), pieceDest = getPiece(dest);
        boolean destOccupied = false;
        if (getPiece(src) == null) return false; // Try to move an empty block
        if (pieceDest != null) {
            if (pieceDest.getOwner() == currPlayer) return false; // Try to capture his own piece
            else destOccupied = true; // dest is occupied by his opponent
        }

        if (!canMovePiece(pieceSrc, dest, destOccupied)) return false;
        // Check if King is selected and if it will die
        if (pieceSrc == currPlayer.getKing()) {
            if (isKingInDanger(currPlayer)) return false; // King puts itself into danger
        }

        pieceSrc.moveTo(dest);
        if (destOccupied) {
            removePiece(dest); // Remove from board
            currPlayer.getOpponent().removePiece(pieceDest); // Remove from opponent's pieces
        }
        return true;
    }

    public boolean canMovePiece(Piece pieceSrc, Position dest, boolean destOccupied) {
        List<Position> checkUnoccupied = new ArrayList<>();
        if (!pieceSrc.canMoveTo(dest, destOccupied, checkUnoccupied)) return false;
        // Check if the given positions are unoccupied
        for (Position toBeUnoccupied : checkUnoccupied) {
            if (getPiece(toBeUnoccupied) != null) return false;
        }
        return true;
    }

    public boolean isKingInDanger(Player currPlayer) {
        Position posKing = currPlayer.getKing().getPosition();
        for (Piece pieceOpponent : currPlayer.getOpponent().getPieces()) {
            if (canMovePiece(pieceOpponent, posKing, true)) return true;
        }
        return false;
    }

    public Game.Status isCheckmateOrStalemate(Player currPlayer) {
        Player currOpponent = currPlayer.getOpponent();
        boolean isCheckmate = isKingInDanger(currOpponent);
        boolean solved = false;
        for (Piece pieceOpponent : currOpponent.getPieces()) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    Position src = pieceOpponent.getPosition(), dest = new Position(x, y);
                    Piece[][] prevBoardStatus = boardStatus.clone();
                    List<Piece> prevPieces = new ArrayList<>(currPlayer.getPieces());
                    if (!movePieceByPosition(currOpponent, src, dest)) continue;
                    if (isCheckmate) {
                        if (!isKingInDanger(currOpponent)) solved = true; // King escapes from checkmate
                    } else solved = true;

                    // Recover to the previous state
                    getPiece(dest).moveTo(src);
                    boardStatus = prevBoardStatus;
                    currPlayer.setPieces(prevPieces);

                    if (solved) return Game.Status.CONTINUE;
                }
            }
        }
        return isCheckmate ? Game.Status.CHECKMATE : Game.Status.STALEMATE;
    }

    // TODO: A setBoardFromStatus method for testing
}
