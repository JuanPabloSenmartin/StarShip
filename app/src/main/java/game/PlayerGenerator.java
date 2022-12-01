package game;

import game.config.GameConfiguration;

import java.util.ArrayList;
import java.util.List;

public class PlayerGenerator {
    public static List<Player> generate(GameConfiguration gameConfiguration){
        int amount = gameConfiguration.getAmountOfPlayers();
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            players.add(new Player("player-" + i, gameConfiguration.getAmountOfLives(), "starship-" + i+1));
        }
        return players;
    }
}
