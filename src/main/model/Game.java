package main.model;

public class Game {
    public enum Status {
        CHECKMATE, STALEMATE, CONTINUE
    }

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
        System.out.println(board.toString());

        Player currPlayer;
        do {
            currPlayer = players[currRound];
            Position[] positions = currPlayer.takeAction(board); // 0: src, 1: dest
            if (!board.movePieceByPosition(currPlayer, positions[0], positions[1])) {
                System.out.println("Please try again.");
                continue;
            }
            currRound = (currRound + 1) % NUM_PLAYERS;
            System.out.println(board.toString());
        } while (!isEnding());
    }

    public boolean isEnding() {
        switch (board.isCheckmateOrStalemate(players[currRound])) {
            case CHECKMATE:
                System.out.println("Checkmate!");
                return true;
            case STALEMATE:
                System.out.println("Stalemate!");
                return true;
            default:
                return false;
        }
    }
}
