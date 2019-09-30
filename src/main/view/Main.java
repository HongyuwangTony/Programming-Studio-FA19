package view;

import controller.GameController;
import model.Game;

/**
 * Main Class to start the GUI
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Game game = new Game(false);
        GameController gameController = new GameController(game);
        new GameView(game, gameController);
    }
}
