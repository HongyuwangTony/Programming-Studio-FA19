package view;

import controller.GameController;
import model.Game;

import java.awt.*;
import javax.swing.*;

/**
 * GameView Class that initializes the whole view
 */
public class GameView {
    /**
     * Constructor of GameView
     * @param game The corresponding Game model
     * @param gameController The corresponding Game controller
     * @throws Exception
     */
    public GameView(Game game, GameController gameController) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        JFrame window = new JFrame("New Chess Game");
        window.setSize(800, 600);

        // Adds chess board to the left
        BoardView boardView = new BoardView(game.getBoard(), gameController);
        window.add(boardView, BorderLayout.WEST);

        // Adds side view to the right
        SideView sideView = new SideView(game.getPlayers(), gameController);
        window.add(sideView, BorderLayout.EAST);

        window.pack();

        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
