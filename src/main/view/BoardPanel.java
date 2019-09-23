package main.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BoardPanel extends JPanel {
    public BoardPanel() {
        this.setLayout(new GridBagLayout()); // Uses GridBag Layout for compact structure
        this.setPreferredSize(new Dimension(480, 480));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String type = null, colorPlayer = null;
                Color colorBackground = (i % 2 + j % 2) % 2 == 0 ?
                        new Color(248, 207, 164) :  // #f8cfa4
                        new Color(199, 141, 83);    // #c78d53

                if (i <= 1) colorPlayer = "Black";
                else if (i >= 6) colorPlayer = "White";

                if (i == 1 || i == 6) type = "Pawn";
                else if (i == 0 || i == 7) {
                    switch (j) {
                        case 0: case 7: type = "Rook"; break;
                        case 1: case 6: type = "Knight"; break;
                        case 2: case 5: type = "Bishop"; break;
                        case 3: type = "Queen"; break;
                        case 4: type = "King"; break;
                    }
                }

                // Adds constraint for location
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = j;
                constraints.gridy = i;
                constraints.insets = new Insets(0,0,0,0);
                constraints.fill = GridBagConstraints.LINE_END;
                this.add(initializePiece(type, colorPlayer, colorBackground), constraints);
            }
        }

    }


    /**
     * Initializes piece with given type, player and background color
     */
    private JButton initializePiece(String type, String colorPlayer, Color colorBackground) {
        JButton piece = new JButton();
        piece.setPreferredSize(new Dimension(60, 60));
        piece.setVisible(true);
        if (type != null) { // Fills in image for piece
            ImageIcon imgIcon = new ImageIcon("src/main/images/" + type + colorPlayer + ".png");
            piece.setIcon(imgIcon);
        }
        piece.setBackground(colorBackground);
        piece.setOpaque(true);
        piece.setBorderPainted(false);
        return piece;
    }
}
