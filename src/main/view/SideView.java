package view;

import controller.GameController;
import model.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SideView extends JPanel {
    public SideView(Player[] players, GameController gameController) {
        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(60, 30, 85, 30));

        ScoreView scoreView = new ScoreView(players, gameController);
        this.add(scoreView, BorderLayout.PAGE_START);

        ButtonView buttonView = new ButtonView(gameController);
        this.add(buttonView, BorderLayout.PAGE_END);
    }
}

