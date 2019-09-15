package main;

import main.model.Game;

public class Main {
    public static void main(String[] args) {
        Game game = new Game("White", "Black");
        game.start();
    }
}
