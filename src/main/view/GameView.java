package view;

import controller.GameController;
import model.Game;

import java.awt.*;
import javax.swing.*;


public class GameView {
    public GameView(Game game, GameController gameController) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        JFrame window = new JFrame("New Chess Game");
        window.setSize(800, 600);

        // Adds chess board to the center
        BoardView boardView = new BoardView(game.getBoard(), gameController);
        window.add(boardView, BorderLayout.WEST);

        SideView sideView = new SideView(game.getPlayers(), gameController);
        window.add(sideView, BorderLayout.EAST);

        window.pack();

        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
