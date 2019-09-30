package controller;

import model.*;
import view.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.Color;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    private Game game;
    private GameController gameController;

    @BeforeEach
    public void initialize() {
        game = new Game(false);
        gameController = new GameController(game);
        gameController.debug = true;
        assertDoesNotThrow(() -> new GameView(game, gameController));
    }

    public void multipleClickOnBoard(String positions) {
        for (String pos : positions.split(",")) {
            gameController.clickOnBoard(new Position(pos));
        }
    }

    @Test
    public void testBeforeStart() {
        gameController.clickOnBoard(new Position("A1"));
        for (JPanel light : gameController.getLights()){
            assertEquals(Color.WHITE, light.getBackground());
        }

        gameController.clickOnButton("Forfeit");
        for (JPanel light : gameController.getLights()){
            assertEquals(Color.WHITE, light.getBackground());
        }

        gameController.clickOnButton("Undo");
        for (JPanel light : gameController.getLights()){
            assertEquals(Color.WHITE, light.getBackground());
        }
    }

    @Test
    public void testStartGame() {
        gameController.clickOnButton("Start / Restart");
        assertTrue(game.isStarted());
        assertEquals(Color.GREEN, gameController.getLights()[0].getBackground());
        assertEquals(Color.WHITE, gameController.getLights()[1].getBackground());
    }

    @Test
    public void testRestart() {
        testStartGame();
        game.getCurrPlayer().incScore();
        assertEquals(1, game.getCurrPlayer().getScore());

        gameController.clickOnButton("Start / Restart");
        assertEquals(0, game.getCurrPlayer().getScore());
    }

    @Test
    public void testSelectSrcAndRestart() {
        testStartGame();
        gameController.clickOnBoard(new Position("A2"));

        gameController.clickOnButton("Start / Restart");
        gameController.clickOnBoard(new Position("A3"));
        // Pawn at A2 should not move
        assertNotNull(game.getBoard().getPiece(new Position("A2")));
    }

    @Test
    public void testForfeit() {
        testStartGame();
        gameController.clickOnButton("Forfeit");
        assertEquals("1", gameController.getScoreLabels()[1].getText());
    }

    @Test
    public void testIllegalUndo() {
        testStartGame();
        gameController.clickOnButton("Undo");
    }

    @Test
    public void testLegalUndo() {
        testStartGame();
        multipleClickOnBoard("A2,A3");
        gameController.clickOnButton("Undo");
        assertNotNull(game.getBoard().getPiece(new Position("A2")));
        assertEquals(0, game.getCurrRound());
    }

    @Test
    public void testIllegalMove() {
        testStartGame();
        multipleClickOnBoard("A2,B3");
        assertEquals(0, game.getCurrRound());
    }

    // Uses Fool's Mate
    @Test
    public void testCheckmate() {
        testStartGame();
        multipleClickOnBoard("F2,F3,E7,E5,G2,G4,D8,H4");
        assertEquals("1", gameController.getScoreLabels()[1].getText());
        assertFalse(game.undoLastCommand());
    }

    // Borrowed from the following 10-step stalemate
    // https://www.chess.com/article/view/the-shortest-stalemate-possible
    @Test
    public void testStalemate() {
        testStartGame();
        multipleClickOnBoard("E2,E3,A7,A5," +
                "D1,H5,A8,A6," +
                "H5,A5,H7,H5," +
                "H2,H4,A6,H6," +
                "A5,C7,F7,F6," +
                "C7,D7,E8,F7," +
                "D7,B7,D8,D3," +
                "B7,B8,D3,H7," +
                "B8,C8,F7,G6," +
                "C8,E6");
        assertEquals("0", gameController.getScoreLabels()[0].getText());
        assertEquals("0", gameController.getScoreLabels()[1].getText());
        assertFalse(game.undoLastCommand());
    }
}
