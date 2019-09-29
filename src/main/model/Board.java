package main.model;

import main.model.pieces.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static main.model.Game.Status.CHECKMATE;
import static main.model.Game.Status.CONTINUE;
import static main.model.Game.Status.STALEMATE;

/**
 * Board Class recording all the pieces on the board
 * Controls movement of pieces
 * Includes calculations regards to the relative positions of pieces
 */
public class Board {
    // Constants
    public final static int WIDTH = 8;
    public final static int HEIGHT = 8;

    // Object Members
    private Piece[][] boardStatus;
    private Command lastCommand;

    /**
     * Constructor of Board that is set at the initial game state
     * @param players The players who are related to the game of this board
     */
    public Board(Player[] players, boolean custom) {
        initiate(players, custom);
    }

    public void initiate(Player players[], boolean custom) {
        boardStatus = new Piece[HEIGHT][WIDTH];

        int arr_y_pawns[] = new int[]{1, 6}, arr_y_others[] = new int[]{0, 7};
        for (int player_no = 0; player_no < players.length; player_no++) {
            Player player = players[player_no];

            // Allocates Rooks, Knights, Bishops, King, Queen to both players
            // Allocates custom chess pieces: Dragon, Cannon to both players
            // Dragon replaces Bishop at F1 and F8 if custom is set to true
            // Cannon replaces Knight at G1 and G8 if custom is set to true
            int y_others = arr_y_others[player_no];
            player.addPiece(new Rook(0, y_others, player));
            player.addPiece(new Knight(1, y_others, player));
            player.addPiece(new Bishop(2, y_others, player));
            player.addPiece(new Queen(3, y_others, player));
            player.addPiece(new King(4, y_others, player));
            if (custom) {
                player.addPiece(new Dragon(5, y_others, player));
                player.addPiece(new Cannon(6, y_others, player));
            } else {
                player.addPiece(new Bishop(5, y_others, player));
                player.addPiece(new Knight(6, y_others, player));
            }
            player.addPiece(new Rook(7, y_others, player));

            // Allocates Pawns to both players
            int y_pawns = arr_y_pawns[player_no];
            for (int x = 0; x < WIDTH; x++) {
                player.addPiece(new Pawn(x, y_pawns, player, false));
            }

            // Initializes Board Status
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
                    case 'C': piece = new Cannon(x, y, player); break;
                    case 'D': piece = new Dragon(x, y, player); break;
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
        // Puts the last row (8A-8H) on the top
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

    public Command removeLastCommand() {
        Command ret = lastCommand;
        lastCommand = null;
        return ret;
    }

    public void undoCommand(Command cmd) {
        Position prevSrc = cmd.getSrc(), prevDest = cmd.getDest();
        Piece pieceCaptured = cmd.getPieceCaptured();

        // Recover pieceSrc
        setPiece(prevSrc, getPiece(prevDest));
        getPiece(prevDest).moveTo(prevSrc);
        removePiece(prevDest);

        // Recover pieceDest
        if (pieceCaptured != null) {
            setPiece(prevDest, pieceCaptured);
            pieceCaptured.getOwner().addPiece(pieceCaptured);
        }
    }

    public boolean arePositionsLegal(Player currPlayer, Position src, Position dest) {
        return isSourceLegal(currPlayer, src) && isDestinationLegal(currPlayer, dest);
    }

    public boolean isSourceLegal(Player currPlayer, Position src) {
        if (src.outsideOfBoard()) return false; // Tries to move from the outside of the board
        Piece pieceSrc = getPiece(src);
        if (pieceSrc == null || !currPlayer.getPieces().contains(pieceSrc)) {
            return false; // Tries to move an empty piece or move his opponent's piece
        }
        return true;
    }

    public boolean isDestinationLegal(Player currPlayer, Position dest) {
        if (dest.outsideOfBoard()) return false; // Tries to move to the outside of the board
        Piece pieceDest = getPiece(dest);
        if (pieceDest != null && pieceDest.getOwner() == currPlayer) {
            return false; // Tries to capture his own piece
        }
        return true;
    }

    /**
     * Moves a piece from the given position to another given position
     * @param currPlayer The player in the current round
     * @param src The source of the piece to be moved
     * @param dest The destination of the piece to be moved
     * @return True if the current player is able to move the piece by the given positions
     */
    public boolean tryMovePiece(Player currPlayer, Position src, Position dest) {
        Piece pieceSrc = getPiece(src), pieceDest = getPiece(dest);
        boolean destOccupied = pieceDest != null;

        if (!canMovePiece(pieceSrc, dest, destOccupied)) return false;

        // Tries to move
        pieceSrc.moveTo(dest);
        removePiece(src);
        setPiece(dest, pieceSrc);
        if (destOccupied) {
            currPlayer.getOpponent().removePiece(pieceDest); // Removes it from opponent's pieces
        }

        // Records command
        lastCommand = new Command(src, dest, pieceDest);

        // Checks if the current player's King is in danger
        if (isKingInDanger(currPlayer)) {
            undoCommand(removeLastCommand());
            return false;
        }
        return true;
    }

    /**
     * Checks if the opponent is checkmated or stalemated at the end of the current round
     * @param currPlayer The player in the current round
     * @return The current status of this game
     */
    public Game.Status isCheckmateOrStalemate(Player currPlayer) {
        Player currOpponent = currPlayer.getOpponent();
        boolean isCheckmate = isKingInDanger(currOpponent);
        for (Piece pieceOpponent : currOpponent.getPieces()) {
            if (!searchLegalDestinations(pieceOpponent).isEmpty()) return CONTINUE;
        }
        return isCheckmate ? CHECKMATE : STALEMATE;
    }

    public List<Position> searchLegalDestinations(Piece piece) {
        List<Position> legalDests = new LinkedList<>();
        Position src = piece.getPosition();
        Player owner = piece.getOwner();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Position dest = new Position(x, y);
                if (!arePositionsLegal(owner, src, dest)) continue;
                if (tryMovePiece(owner, src, dest)) { // Upon success, King escapes from checkmate/stalemate
                    undoCommand(removeLastCommand()); // Recovers to the previous state
                    legalDests.add(dest);
                }
            }
        }
        return legalDests;
    }

    /**
     * Checks if one player can move a valid piece to the another given destination by the piece's rule
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
        boolean isCannon = pieceSrc instanceof Cannon, hurdleExists = false;
        for (Position toBeUnoccupied : checkUnoccupied) {
            if (getPiece(toBeUnoccupied) != null) {
                if (isCannon) { // pieceSrc is Cannon
                    if (hurdleExists) return false; // At least two hurdles are found
                    else hurdleExists = true;
                } else { // pieceSrc is not Cannon
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the King of the given player is being put in check
     * @param currPlayer The given player that owns the King
     * @param posKing The position that the King is or will be located at
     * @return True if the King is being put in check
     */
    public boolean isKingInDanger(Player currPlayer) {
        Position posKing = currPlayer.getKing().getPosition();
        for (Piece pieceOpponent : currPlayer.getOpponent().getPieces()) {
            if (canMovePiece(pieceOpponent, posKing, true)) return true;
        }
        return false;
    }
}
