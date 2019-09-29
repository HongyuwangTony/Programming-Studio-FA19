package main.view;

import main.controller.GameController;
import main.model.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ScoreView extends JPanel {
    private static final Font fontInfo = new Font("Dialog", Font.PLAIN, 30);
    private static final Dimension sizeLight = new Dimension(40, 40);
    public static final Border borderLight =
            BorderFactory.createStrokeBorder(new BasicStroke(3.0f), Color.BLACK);

    public ScoreView(Player[] players, GameController gameController) {
        this.setLayout(new GridBagLayout());
        addLight(players[0], gameController);
        addNameAndScore(players[0], gameController);
        addVersus();
        addLight(players[1], gameController);
        addNameAndScore(players[1], gameController);
    }

    private void addLight(Player player, GameController gameController) {
        int player_no = player.getPlayerNo();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = player_no == 0 ? 2 : 0;
        JPanel light = new JPanel();
        light.setPreferredSize(sizeLight);
        light.setBackground(Color.WHITE);
        light.setBorder(borderLight);
        this.add(light, constraints);
        gameController.setUpLight(player_no, light);
    }

    private void addVersus() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        JLabel versusLabel = new JLabel("vs", SwingConstants.CENTER);
        versusLabel.setPreferredSize(new Dimension(160, 80));
        versusLabel.setFont(fontInfo);
        this.add(versusLabel, constraints);
    }

    private void addNameAndScore(Player player, GameController gameController) {
        // Adds player's name
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = player.getPlayerNo() == 0 ? 2 : 0;
        JLabel nameLabel = new JLabel(player.getPlayerColor(), SwingConstants.CENTER);
        nameLabel.setPreferredSize(new Dimension(110, 40));
        nameLabel.setFont(fontInfo);
        this.add(nameLabel, constraints);

        // Adds player's score
        constraints.gridx = 2;
        JLabel scoreLabel = new JLabel(Integer.toString(player.getScore()), SwingConstants.RIGHT);
        scoreLabel.setPreferredSize(new Dimension(40, 40));
        scoreLabel.setFont(fontInfo);
        this.add(scoreLabel, constraints);
        gameController.setUpScoreLabel(player.getPlayerNo(), scoreLabel);
    }
}

