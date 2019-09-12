package main.model;

public class Game {
    // Constants
    public final static int NUM_PLAYERS = 2;

    // Object Members
    private Player[] players;
    private Board board;
    private int currRound;

    public Game(String namePlayerWhite, String namePlayerBlack) {
        players = new Player[NUM_PLAYERS];
        players[0] = new Player(namePlayerWhite, 0);
        players[1] = new Player(namePlayerBlack, 1);
        board = new Board(players);
        currRound = 0; // Player White's round first by default
    }

    public void start() {
        do {
            players[currRound].takeAction(board);
            currRound = (currRound + 1) % NUM_PLAYERS;
        } while (isEnding());
    }

    public boolean isEnding() {
        // TODO: Add Game Ending Condition
        return false;
    }
}
