package controller;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import model.*;
import static model.Board.HEIGHT;
import static model.Board.WIDTH;
import static view.BoardView.*;

/**
 * BoardController class that listens and updates the Board view with its corresponding model
 */
public class BoardController {
    private Board board;
    private Position posSrcPiece;
    private List<Position> legalDestList;
    private JButton[][] gridButtons = new JButton[HEIGHT][WIDTH];

    /**
     * Constructor of BoardController
     * @param board The corresponding Board model
     */
    public BoardController(Board board) {
        this.board = board;
    }

    /**
     * Sets up the JButton at the given position
     * @param pos The position of the JButton
     * @param pieceButton The JButton to be set up
     */
    public void setUpButton(Position pos, JButton pieceButton) {
        gridButtons[pos.y][pos.x] = pieceButton;
    }

    /**
     * Gets the JButton for the given position
     * @param pos The position of the JButton
     * @return The JButton object at pos
     */
    private JButton getButton(Position pos) {
        return gridButtons[pos.y][pos.x];
    }

    /**
     * Highlights the JButtons of the source and its legal destinations
     */
    private void highlightButtons() {
        getButton(posSrcPiece).setBackground(highlightBackground);
        getButton(posSrcPiece).setBorder(highlightBorder);
        getButton(posSrcPiece).setBorderPainted(true);

        // Highlights legal destinations
        legalDestList = board.searchLegalDestinations(board.getPiece(posSrcPiece));
        for (Position dest : legalDestList) {
            getButton(dest).setBorder(BorderFactory.createStrokeBorder(new BasicStroke(2.0f), Color.GREEN));
            getButton(dest).setBorderPainted(true);
            getButton(dest).setBackground(highlightBackground);
        }
    }

    /**
     * Undo highlighting the JButtons of the source and its legal destinations
     */
    private void unhighlightButtons() {
        getButton(posSrcPiece).setBackground(
                posSrcPiece.x % 2 == posSrcPiece.y % 2 ? lightBackground : darkBackground);
        getButton(posSrcPiece).setBorderPainted(false);

        // Undo highlighting legal destinations
        for (Position dest : legalDestList) {
            getButton(dest).setBackground(dest.x % 2 == dest.y % 2 ? lightBackground : darkBackground);
            getButton(dest).setBorderPainted(false);
        }
    }

    /**
     * Refreshes the JButton at the given position
     * @param pos The position of the corresponding piece
     */
    private void refreshButton(Position pos) {
        Piece piece = board.getPiece(pos);
        ImageIcon updatedIcon = piece == null ? null : new ImageIcon(piece.getImageFileName());
        getButton(pos).setIcon(updatedIcon);
    }

    /**
     * Refreshes the Board view
     */
    public void refreshBoard() {
        if (posSrcPiece != null) {
            unhighlightButtons();
            posSrcPiece = null;
            legalDestList = null;
        }
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                refreshButton(new Position(x, y));
            }
        }
    }

    /**
     * Removes the last command from the Board model
     * @return The last executed command
     */
    public Command removeLastCommand() {
        return board.removeLastCommand();
    }

    /**
     * Judges if the Board model is in check
     * @param currPlayer The player to be check if he is in check
     * @return True if the given player is in check
     */
    public boolean isInCheck(Player currPlayer) {
        return board.isInCheck(currPlayer);
    }

    /**
     * Responds as a piece is clicked on a BoardView
     * @param currPlayer The player clicks the piece
     * @param posClicked The position of the piece clicked
     */
    public void clickedOn(Player currPlayer, Position posClicked) {
        if (posSrcPiece == null) {
            if (!board.isSourceLegal(currPlayer, posClicked)) return;
            posSrcPiece = posClicked;
            highlightButtons();
        }
        else {
            unhighlightButtons();
            if (board.isDestinationLegal(currPlayer, posClicked) &&
                    board.tryMovePiece(currPlayer, posSrcPiece, posClicked)) {
                refreshButton(posSrcPiece);
                refreshButton(posClicked);
            } else if (!posClicked.equals(posSrcPiece)) { // NOTE: click on the same piece to cancel
                GameController.showMessage("Illegal movement is detected!", "Warning");
            }
            posSrcPiece = null;
            legalDestList = null;
        }
    }
}
