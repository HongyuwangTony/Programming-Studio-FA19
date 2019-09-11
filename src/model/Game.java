package model;

public class Game {
    // Constants
    private final static int NUM_PLAYERS = 2;

    // Object Members
    private Player[] players;
    private Board board;
    private int currRound;

    public Game(Player player1, Player player2) {
        players = new Player[NUM_PLAYERS];
        players[0] = player1;
        players[1] = player2;
        board = new Board();
        for (Player player : players) {
            board.addPieces(player.getPieces());
        }
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
