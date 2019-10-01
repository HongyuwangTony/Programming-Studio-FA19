package controller;

import javax.swing.*;
import java.awt.*;

import model.*;
import static model.Game.NUM_PLAYERS;

/**
 * GameController Class that listens on all the user actions and updates on the model and the view
 */
public class GameController {
    public static boolean debug = false; // Set true for testing, closing the pop window

    private Game game;
    private BoardController boardController;

    private JLabel scoreLabels[];
    private JPanel lights[];

    /**
     * Constructor of GameController
     * @param game The corresponding Game model
     */
    public GameController(Game game) {
        this.game = game;
        boardController = new BoardController(game.getBoard());
        scoreLabels = new JLabel[NUM_PLAYERS];
        lights = new JPanel[2];
    }

    /**
     * Sets up the JLabels of score for the given player in the ScoreView
     * @param player_no The player number of the player
     * @param scoreLabel The JLabel of score
     */
    public void setUpScoreLabel(int player_no, JLabel scoreLabel) {
        scoreLabels[player_no] = scoreLabel;
    }

    /**
     * Sets up the JPanel of round light for the given player in the ScoreView
     * @param player_no The player number of the player
     * @param light The JPanel of round light
     */
    public void setUpLight(int player_no, JPanel light) {
        lights[player_no] = light;
    }

    /**
     * Sets up the JButton of the given piece in the BoardView
     * @param posPiece The corresponding position in the Board model
     * @param pieceButton The JButton of the given piece
     */
    public void setUpPieceButton(Position posPiece, JButton pieceButton) {
        boardController.setUpButton(posPiece, pieceButton);
    }

    /**
     * Getter of scoreLabels, for the sake of testing
     * @return The list of JLabels of scores
     */
    public JLabel[] getScoreLabels() {
        return scoreLabels;
    }

    /**
     * Getter of lights, for the sake of testing
     * @return The list of JPanels of lights
     */
    public JPanel[] getLights() {
        return lights;
    }

    /**
     * Alternates the round, updating the Game model and ScoreView
     */
    private void alternateRound() {
        lights[game.getCurrPlayer().getPlayerNo()].setBackground(Color.WHITE);
        game.alternateRound();
        lights[game.getCurrPlayer().getPlayerNo()].setBackground(Color.GREEN);
    }

    /**
     * Judges if the current round is ending
     * @return True if the game is in checkmate or stalemate
     */
    private boolean isEnding() {
        boolean result;
        switch (game.getStatus()) {
            case CHECKMATE:
                Player winner = game.getCurrPlayer();
                showMessage(winner.getPlayerColor() + " Player Checkmates!", "Notice");
                winner.incScore();
                restart(false);
                result = true;
                break;
            case STALEMATE:
                showMessage("Game stalemates!", "Notice");
                restart(false);
                result = true;
                break;
            default:
                result = false;
        }
        return result;
    }

    /**
     * Refreshes the score of the given player on the view
     * @param player The player that owns the score
     */
    private void refreshScore(Player player) {
        scoreLabels[player.getPlayerNo()].setText(Integer.toString(player.getScore()));
    }

    /**
     * Refreshes the whole Game view
     */
    private void refreshView() {
        lights[0].setBackground(Color.GREEN);
        lights[1].setBackground(Color.WHITE);
        for (Player player : game.getPlayers()) refreshScore(player);
        boardController.refreshBoard();
    }

    /**
     * Restarts the game, resetting the Board model and view
     * @param toClearScore True if scores are required to be cleared
     */
    private void restart(boolean toClearScore) {
        game.restart(false);
        if (toClearScore) {
            for (Player player : game.getPlayers()) player.clearScore();
        }
        refreshView();
    }

    /**
     * Requests a restart
     * Accepts by default
     */
    private void requestRestart() {
        restart(true);
    }

    /**
     * Requests a forfeit
     */
    private void requestForfeit() {
        game.getCurrPlayer().getOpponent().incScore();
        System.out.println(game.getCurrPlayer().getPlayerColor() + " Player Forfeits!");
        restart(false);
    }

    /**
     * Requests a undo whenever
     */
    private void requestUndo() {
        if (game.undoLastCommand()) {
            boardController.refreshBoard();
            alternateRound();
        } else {
            showMessage("No command can be undone at this time.", "Warning");
        }
    }

    /**
     * Clicks on board for the given position, called by the ActionEvent
     * @param posClicked The clicked position on the board
     */
    public void clickOnBoard(Position posClicked) {
        // Checks if game is started
        if (!game.isStarted()) {
            showMessage("Game is not started.", "Warning");
            return;
        }

        Player currPlayer = game.getCurrPlayer();
        boardController.clickedOn(currPlayer, posClicked);
        Command lastCmd = boardController.removeLastCommand();

        // Reacts to a board change
        if (lastCmd != null) {
            // Checks ending condition
            if (isEnding()) return;

            game.recordCommand(lastCmd);
            alternateRound();

            // Notices if in check
            if (boardController.isInCheck(currPlayer.getOpponent())) {
                showMessage(currPlayer.getOpponent().getPlayerColor() + " Player is in Check!",
                        "Notice");
            }
        }
    }

    /**
     * Clicks on button in the ButtonView, called by the ActionEvent
     * @param buttonText The text on the clicked button
     */
    public void clickOnButton(String buttonText) {
        if (!game.isStarted()) { // If game is not started
            if (buttonText.equals("Start / Restart")) { // Start
                lights[0].setBackground(Color.GREEN);
                game.start();
                showMessage("Game starts!", "Notice");
            } else {
                showMessage("Game is not started.", "Warning");
            }
        } else { // If game is started
            if (buttonText.equals("Start / Restart")) requestRestart();
            else if (buttonText.equals("Forfeit")) requestForfeit();
            else if (buttonText.equals("Undo")) requestUndo();
        }
    }

    /**
     * Shows a pop up window with the given message
     * @param message The message shown on the pop up window
     * @param title The title of the pop up window
     */
    public static void showMessage(String message, String title) {
        if (!debug) JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
