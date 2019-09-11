package main.model;

public class Game {
    // Constants
    public final static int NUM_PLAYERS = 2;

    // Object Members
    private Player[] players;
    private Board board;
    private int currRound;

    public Game(String name_player1, String name_player2) {
        players = new Player[NUM_PLAYERS];
        players[0] = new Player(name_player1, 0);
        players[1] = new Player(name_player2, 1);
        board = new Board(players);
        currRound = 0; // player1's round first by default
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
