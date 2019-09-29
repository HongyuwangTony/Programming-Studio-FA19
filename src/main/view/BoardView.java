package view;

import controller.GameController;
import model.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BoardView extends JPanel {
    public static final Color lightBackground = new Color(198, 169, 125);  // #f8cfa4
    public static final Color darkBackground = new Color(115, 45, 40);    // #5a2b26
    public static final Color highlightBackground = new Color(244, 242, 144); // #f4f290
    public static final Border highlightBorder =
            BorderFactory.createStrokeBorder(new BasicStroke(5.0f), Color.GREEN);
    private static final Font fontCoordinates = new Font("Courier", Font.PLAIN, 20);

    private Board board;

    public BoardView(Board board, GameController gameController) {
        this.board = board;
        this.setLayout(new GridBagLayout()); // Uses GridBag Layout for compact structure
        for (int y = 0; y < Board.HEIGHT + 1; y++) {
            if (y < Board.HEIGHT) addYCoordinate(y); // Additional Column at x=0 for y coordinates
            for (int x = 0; x < Board.WIDTH; x++) {
                if (y < Board.HEIGHT) addPiece(new Position(x, 7 - y), gameController);
                else addXCoordinate(x);  // Additional Column at y=8 for x coordinates
            }
        }
        this.setBorder(new EmptyBorder(30, 0, 20, 0));
    }

    private void addXCoordinate(int x) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x + 1;
        constraints.gridy = Board.HEIGHT;
        constraints.fill = GridBagConstraints.VERTICAL;
        // The column is labeled A to H, from left to right
        JLabel xLabel = new JLabel("" + (char)('A' + x), SwingConstants.CENTER);
        xLabel.setPreferredSize(new Dimension(60, 40));
        xLabel.setFont(fontCoordinates);
        this.add(xLabel, constraints);
    }

    private void addYCoordinate(int y) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = y;
        // The row is labeled 8 to 1, from top to bottom
        JLabel yLabel = new JLabel("" + (8 - y), SwingConstants.CENTER);
        yLabel.setPreferredSize(new Dimension(40, 60));
        yLabel.setFont(fontCoordinates);
        this.add(yLabel, constraints);
    }

    /**
     * Initializes piece with given type, player and background color
     */
    private void addPiece(Position pos, GameController gameController) {
        JButton pieceButton = new JButton();
        pieceButton.setPreferredSize(new Dimension(60, 60));
        pieceButton.setVisible(true);

        Piece piece = board.getPiece(pos);
        if (piece != null) { // Fills in image for non-empty piece
            pieceButton.setIcon(new ImageIcon(piece.getImageFileName()));
        }

        // Sets alternating color
        pieceButton.setBackground(pos.x % 2 == pos.y % 2 ? lightBackground : darkBackground);
        pieceButton.setOpaque(true); // Set the button to be transparent
        pieceButton.setBorderPainted(false);

        // Adds constraint for location
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = pos.x + 1; // NOTE: since x=0 is for coordinates
        constraints.gridy = 7 - pos.y;

        // Sets up the connection with controller
        pieceButton.addActionListener((ActionEvent e) -> gameController.clickOnBoard(pos));
        this.add(pieceButton, constraints);
        gameController.setUpPieceButton(pos, pieceButton);
    }
}
