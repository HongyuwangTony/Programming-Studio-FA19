package main.controller;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import main.model.*;
import static main.model.Board.HEIGHT;
import static main.model.Board.WIDTH;
import static main.view.BoardView.*;

public class BoardController {
    private Board board;
    private Position posSrcPiece;
    private List<Position> legalDestList;
    private JButton[][] gridButtons = new JButton[HEIGHT][WIDTH];

    public BoardController(Board board) {
        this.board = board;
    }

    public void setUpButton(Position pos, JButton pieceButton) {
        gridButtons[pos.y][pos.x] = pieceButton;
    }

    private JButton getButton(Position pos) {
        return gridButtons[pos.y][pos.x];
    }

    private void refreshButton(Position pos) {
        Piece piece = board.getPiece(pos);
        ImageIcon updatedIcon = piece == null ? null : new ImageIcon(piece.getImageFileName());
        getButton(pos).setIcon(updatedIcon);
    }

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

    private void unhighlightButtons() {
        getButton(posSrcPiece).setBackground(
                posSrcPiece.x % 2 == posSrcPiece.y % 2 ? lightBackground : darkBackground);
        getButton(posSrcPiece).setBorderPainted(false);

        // Unhighlights legal destinations
        for (Position dest : legalDestList) {
            getButton(dest).setBackground(dest.x % 2 == dest.y % 2 ? lightBackground : darkBackground);
            getButton(dest).setBorderPainted(false);
        }
    }

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
            }
            posSrcPiece = null;
            legalDestList = null;
        }
    }

    public Command removeLastCommand() {
        return board.removeLastCommand();
    }
}
