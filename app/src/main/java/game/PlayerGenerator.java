package game;

import java.util.ArrayList;
import java.util.List;

public class PlayerGenerator {
    public static List<Player> generate(int amount){
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            players.add(new Player("player-"+i));
        }
        return players;
    }
}
