package controller;

import model.*;

import javax.swing.*;

import java.awt.*;

import static model.Game.NUM_PLAYERS;

public class GameController {
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

    public void refreshScore(Player player) {
        scoreLabels[player.getPlayerNo()].setText(Integer.toString(player.getScore()));
    }

    public void alternateRound() {
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
        switch (game.getStatus()) {
            case CHECKMATE:
                Player winner = game.getCurrPlayer().getOpponent();
                JOptionPane.showMessageDialog(null,
                        winner.getPlayerColor() + " Player Checkmates!",
                        "Notice", JOptionPane.INFORMATION_MESSAGE);
                winner.incScore();
                restart(false);
                return true;
            case STALEMATE:
                JOptionPane.showMessageDialog(null,
                        "Game Stalemates!",
                        "Notice", JOptionPane.INFORMATION_MESSAGE);
                restart(false);
                return true;
            default:
                return false;
        }
    }

    public void clickOnBoard(Position posClicked) {
        if (!game.isStarted()) {
            JOptionPane.showMessageDialog(null,
                    "Game is not started.",
                    "Warning", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boardController.clickedOn(game.getCurrPlayer(), posClicked);
        Command lastCmd = boardController.removeLastCommand();
        if (lastCmd != null) {
            if (isEnding()) return;
            game.recordCommand(lastCmd);
            alternateRound();
        }
    }

    public void clickOnButton(String buttonText) {
        if (!game.isStarted()) { // If game is not started
            if (buttonText.equals("Start / Restart")) { // Start
                lights[0].setBackground(Color.GREEN);
                game.start();
                JOptionPane.showMessageDialog(null,
                        "Game Starts!",
                        "Notice", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Game is not started.",
                        "Warning", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(null,
                    "No command can be undone at this time.",
                    "Warning", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
