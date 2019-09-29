package model;

public class GameLogicTest {
//    public void testRoundAlternating() {
//        String userInput = "A2\nA3\n";
//        InputStream inputStream = new ByteArrayInputStream(userInput.getBytes());
//        Game game = new Game("White", "Black", inputStream);
//        assertEquals(0, game.getCurrRound());
//        game.start();
//        assertEquals(1, game.getCurrRound());
//    }
//
//    public void testInvalidInput() {
//        String userInput = "A1\nA2\n";
//        InputStream inputStream = new ByteArrayInputStream(userInput.getBytes());
//        Game game = new Game("White", "Black", inputStream);
//        assertEquals(0, game.getCurrRound());
//        game.start();
//        assertEquals(0, game.getCurrRound()); // Round stays the same after invalid input
//    }
//
//    public void testGameEndsAfterCheckmate() {
//        String currStatus =
//                "________\n" +
//                "________\n" +
//                "________\n" +
//                "_____K_k\n" +
//                "________\n" +
//                "________\n" +
//                "________\n" +
//                "_______R\n";
//        Player[] players = Game.generatePlayers("White", "Black");
//        Board board = new Board(players, currStatus);
//        Game game = new Game("White", "Black", null);
//        game.setBoard(board);
//        game.setPlayers(players);
//        assertTrue(game.isEnding());
//    }
//
//    public void testGameEndsAfterStalemate() {
//        String currStatus =
//                "_______k\n" +
//                "_____K__\n" +
//                "______Q_\n" +
//                "________\n" +
//                "________\n" +
//                "________\n" +
//                "________\n" +
//                "________\n";
//        Player[] players = Game.generatePlayers("White", "Black");
//        Board board = new Board(players, currStatus);
//        Game game = new Game("White", "Black", null);
//        game.setBoard(board);
//        game.setPlayers(players);
//        assertTrue(game.isEnding());
//    }
}
