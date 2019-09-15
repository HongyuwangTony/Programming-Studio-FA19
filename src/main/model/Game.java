package main.model;

public class Game {
    // Constants
    public final static int NUM_PLAYERS = 2;

    // Object Members
    private Player[] players;
    private Board board;
    private int currRound;

    public Game(String namePlayerWhite, String namePlayerBlack) {
        players = generatePlayers(namePlayerWhite, namePlayerBlack);
        board = new Board(players);
        currRound = 0; // Player White(0)'s round first by default
    }

    public static Player[] generatePlayers(String namePlayerWhite, String namePlayerBlack) {
        Player[] players = new Player[NUM_PLAYERS];
        players[0] = new Player(namePlayerWhite, 0);
        players[1] = new Player(namePlayerBlack, 1);
        players[0].setOpponent(players[1]);
        players[1].setOpponent(players[0]);
        return players;
    }

    public void start() {
        do {
            Player currPlayer = players[currRound];
            Position[] positions = currPlayer.takeAction(board); // 0: src, 1: dest
            board.movePiece(currPlayer, positions[0], positions[1]);
            currRound = (currRound + 1) % NUM_PLAYERS;
        } while (isEnding());
    }

    public boolean isEnding() {
        // TODO: Add Game Ending Condition (Checkmate / Stalemate)
        return false;
    }
}
