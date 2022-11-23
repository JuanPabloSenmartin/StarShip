package game;

import game.gameObject.GameObject;

import java.util.List;

public class GameState {
    private final List<GameObject> gameObjects;
    private final List<Player> players;

    public GameState(List<GameObject> gameObjects, List<Player> players) {
        this.gameObjects = gameObjects;
        this.players = players;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
