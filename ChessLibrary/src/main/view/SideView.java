package view;

import controller.GameController;
import model.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * SideView Class that integrates ScoreView and SideView on the right of screen
 */
public class SideView extends JPanel {
    /**
     * Constructor of SideView
     * @param players The players in the corresponding Game model
     * @param gameController The corresponding Game controller
     */
    public SideView(Player[] players, GameController gameController) {
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(60, 30, 85, 30));

        ScoreView scoreView = new ScoreView(players, gameController);
        this.add(scoreView, BorderLayout.PAGE_START);

        ButtonView buttonView = new ButtonView(gameController);
        this.add(buttonView, BorderLayout.PAGE_END);
    }
}

