package controller;

import javax.swing.*;
import java.awt.*;

import model.*;
import static model.Game.NUM_PLAYERS;

public class GameController {
    public static boolean debug = false;

    private Game game;
    private BoardController boardController;

    private JLabel scoreLabels[];
    private JPanel lights[];

    public GameController(Game game) {
        this.game = game;
        boardController = new BoardController(game.getBoard());
        scoreLabels = new JLabel[NUM_PLAYERS];
        lights = new JPanel[2];
    }

    public void setUpScoreLabel(int player_no, JLabel scoreLabel) {
        scoreLabels[player_no] = scoreLabel;
    }

    public void setUpLight(int player_no, JPanel light) {
        lights[player_no] = light;
    }

    public void setUpPieceButton(Position posPiece, JButton pieceButton) {
        boardController.setUpButton(posPiece, pieceButton);
    }

    public JLabel[] getScoreLabels() {
        return scoreLabels;
    }

    public JPanel[] getLights() {
        return lights;
    }

    private void refreshScore(Player player) {
        scoreLabels[player.getPlayerNo()].setText(Integer.toString(player.getScore()));
    }

    private void alternateRound() {
        lights[game.getCurrPlayer().getPlayerNo()].setBackground(Color.WHITE);
        game.alternateRound();
        lights[game.getCurrPlayer().getPlayerNo()].setBackground(Color.GREEN);
    }

    public void restart(boolean toClearScore) {
        game.restart(false);
        if (toClearScore) {
            for (Player player : game.getPlayers()) player.clearScore();
        }
        refreshView();
    }

    private void refreshView() {
        lights[0].setBackground(Color.GREEN);
        lights[1].setBackground(Color.WHITE);
        for (Player player : game.getPlayers()) refreshScore(player);
        boardController.refreshBoard();
    }

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

    public void clickOnBoard(Position posClicked) {
        if (!game.isStarted()) {
            showMessage("Game is not started.", "Warning");
            return;
        }

        Player currPlayer = game.getCurrPlayer();
        boardController.clickedOn(currPlayer, posClicked);
        Command lastCmd = boardController.removeLastCommand();
        if (lastCmd != null) {
            if (isEnding()) return;
            game.recordCommand(lastCmd);
            alternateRound();
            if (boardController.isInCheck(currPlayer.getOpponent())) {
                showMessage(currPlayer.getOpponent().getPlayerColor() + " Player is in Check!",
                        "Notice");
            }
        }
    }

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

    private void requestRestart() {
        restart(true);
    }

    private void requestForfeit() {
        game.getCurrPlayer().getOpponent().incScore();
        System.out.println(game.getCurrPlayer().getPlayerColor() + " Player Forfeits!");
        restart(false);
    }

    private void requestUndo() {
        if (game.undoLastCommand()) {
            boardController.refreshBoard();
            alternateRound();
        } else {
            showMessage("No command can be undone at this time.", "Warning");
        }
    }

    public static void showMessage(String message, String title) {
        if (!debug) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
