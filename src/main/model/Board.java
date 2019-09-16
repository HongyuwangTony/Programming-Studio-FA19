package main.model;

import main.model.pieces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    // Constants
    public final static int WIDTH = 8;
    public final static int HEIGHT = 8;

    // Object Members
    private Piece[][] boardStatus;

    /**
     * Constructor of Board that is set at the initial game state
     * @param players The players who are related to the game of this board
     */
    public Board(Player[] players) {
        boardStatus = new Piece[HEIGHT][WIDTH];

        int arr_y_pawns[] = new int[]{1, 6}, arr_y_others[] = new int[]{0, 7};
        for (int player_no = 0; player_no < players.length; player_no++) {
            Player player = players[player_no];

            // Allocate Rooks, Knights, Bishops, King, Queen to both players
            int y_others = arr_y_others[player_no];
            player.addPiece(new King(4, y_others, player));
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
                player.addPiece(new Pawn(x, y_pawns, player, false));
            }

            // Initialize Board Status
            for (Piece piece : player.getPieces()) {
                Position pos = piece.getPosition();
                setPiece(pos, piece);
            }
        }
    }

    /**
     * Constructor of Board that is set at the given game state
     * @param players The players who are related to the game of this board
     * @param strBoard The game state as a String representation
     */
    public Board(Player[] players, String strBoard) {
        boardStatus = new Piece[HEIGHT][WIDTH];
        int y = HEIGHT - 1;
        for (String row : strBoard.split("\n")) {
            int x = 0;
            for (char c_piece : row.toCharArray()) {
                int player_no = Character.isUpperCase(c_piece) ? 0 : 1;
                Player player = players[player_no];
                Piece piece = null;
                switch (Character.toUpperCase(c_piece)) {
                    case 'K': piece = new King(x, y, player); break;
                    case 'Q': piece = new Queen(x, y, player); break;
                    case 'B': piece = new Bishop(x, y, player); break;
                    case 'N': piece = new Knight(x, y, player); break;
                    case 'R': piece = new Rook(x, y, player); break;
                    case 'P':
                        boolean hasMoved = (player_no == 0 && y != 1) || (player_no == 1 && y != 6);
                        piece = new Pawn(x, y, player, hasMoved);
                }
                if (piece != null) {
                    player.addPiece(piece);
                    setPiece(new Position(x, y), piece);
                }
                x++;
            }
            y--;
        }
    }

    /**
     * Encoder of Board into 8x8 grid String representation
     * '_' indicates empty space
     * Uppercase - White Player(0), Lowercase - Black Player(1)
     * @return A 8x8 grid of String that represents the current status of this board
     */
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

    /**
     * Getter of Piece by the given position
     * @param position The given position of the piece on the board
     * @return The piece at the given position
     */
    public Piece getPiece(Position position) {
        return boardStatus[position.y][position.x];
    }

    /**
     * Remove the Piece from the board at the given position
     * @param position The piece which is going to be removed
     */
    public void removePiece(Position position) {
        boardStatus[position.y][position.x] = null;
    }

    /**
     * Setter of Piece by the given position
     * @param position The given position of the piece on the board
     * @param piece The piece to be set at the given position
     */
    public void setPiece(Position position, Piece piece) {
        boardStatus[position.y][position.x] = piece;
    }

    /**
     * Move a piece from the given position to another given position
     * @param currPlayer The player in the current round
     * @param src The source of the piece to be moved
     * @param dest The destination of the piece to be moved
     * @return True if the current player is able to move the piece by the given positions
     */
    public boolean movePieceByPosition(Player currPlayer, Position src, Position dest) {
        if (src.outsideOfBoard() || dest.outsideOfBoard()) {
            return false; // Try to move from/to the outside of the board
        }

        Piece pieceSrc = getPiece(src), pieceDest = getPiece(dest);
        boolean destOccupied = false;
        if (getPiece(src) == null) {
            return false; // Try to move an empty block
        }
        if (pieceDest != null) {
            if (pieceDest.getOwner() == currPlayer) {
                return false; // Try to capture his own piece
            }
            else destOccupied = true; // dest is occupied by his opponent
        }

        if (!canMovePiece(pieceSrc, dest, destOccupied)) {
            return false;
        }
        // Check if King is selected and if it will die
        if (pieceSrc == currPlayer.getKing()) {
            if (isKingInDanger(currPlayer, dest)) {
                return false; // King puts itself into danger
            }
        }

        pieceSrc.moveTo(dest);
        removePiece(src);
        setPiece(dest, pieceSrc);
        if (destOccupied) {
            currPlayer.getOpponent().removePiece(pieceDest); // Remove from opponent's pieces
        }
        return true;
    }

    /**
     * Check if one player can move a valid piece to the another given destination by the piece's rule
     * NOTE: The logic of whether the King is being put in check is not checked in this method
     * @param pieceSrc The piece to be moved
     * @param dest The destination of the piece
     * @param destOccupied True if the destination is occupied by its
     * @return True if one player can move this piece to dest by its rule
     */
    public boolean canMovePiece(Piece pieceSrc, Position dest, boolean destOccupied) {
        List<Position> checkUnoccupied = new ArrayList<>();
        if (!pieceSrc.canMoveTo(dest, destOccupied, checkUnoccupied)) return false;
        // Check if the given positions are unoccupied
        for (Position toBeUnoccupied : checkUnoccupied) {
            if (getPiece(toBeUnoccupied) != null) return false;
        }
        return true;
    }

    /**
     * Check if the King of the given player is being put in check
     * @param currPlayer The given player that owns the King
     * @param posKing The position that the King is or will be located at
     * @return True if the King is being put in check
     */
    public boolean isKingInDanger(Player currPlayer, Position posKing) {
        for (Piece pieceOpponent : currPlayer.getOpponent().getPieces()) {
            System.out.println(pieceOpponent + " " + pieceOpponent.getPosition());
            if (canMovePiece(pieceOpponent, posKing, true)) return true;
        }
        return false;
    }

    /**
     * Check if the opponent is checkmated or stalemated at the end of the current round
     * @param currPlayer The player in the current round
     * @return The current status of this game
     */
    public Game.Status isCheckmateOrStalemate(Player currPlayer) {
        Player currOpponent = currPlayer.getOpponent();
        boolean isCheckmate = isKingInDanger(currOpponent, currOpponent.getKing().getPosition());
        boolean solved = false;
        for (Piece pieceOpponent : currOpponent.getPieces()) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    Position src = pieceOpponent.getPosition(), dest = new Position(x, y);

                    // Store the previous state
                    Piece[][] prevBoardStatus = new Piece[HEIGHT][WIDTH];
                    for (int i = 0; i < HEIGHT; i++) {
                        prevBoardStatus[i] = Arrays.copyOf(boardStatus[i], boardStatus[i].length);
                    }
                    Piece pieceCaptured = getPiece(dest);

                    if (!movePieceByPosition(currOpponent, src, dest)) continue;
                    if (!isKingInDanger(currOpponent, currOpponent.getKing().getPosition()))
                        solved = true; // King escapes from checkmate/stalemate

                    // Recover to the previous state
                    getPiece(dest).moveTo(src);
                    boardStatus = prevBoardStatus;
                    if (pieceCaptured != null) currPlayer.addPiece(pieceCaptured);

                    if (solved) return Game.Status.CONTINUE;
                }
            }
        }
        return isCheckmate ? Game.Status.CHECKMATE : Game.Status.STALEMATE;
    }
}
