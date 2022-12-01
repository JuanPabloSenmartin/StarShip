package game;

import game.config.ConfigManager;
import game.config.GameConfiguration;
import game.gameObject.GameObject;
import game.gameObject.objects.enums.GameObjectType;
import game.gameObject.GameObjectsGenerator;
import game.gameObject.objects.Meteor;
import game.gameObject.objects.Ship;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Game {
    private GameState gameState;
    private boolean isPaused;
    private final GameConfiguration gameConfiguration;
    private final List<String> eliminated;
    private final Map<String, Integer> playerPoints;
    private boolean finished;

    public Game() {
        this.gameConfiguration = new GameConfiguration();
        this.eliminated = new ArrayList<>();
        this.finished = false;
        this.playerPoints = new HashMap<>();
    }
    public void addEliminated(String id){
        eliminated.add(id);
    }

    public void start(boolean initializeFromSavedState){
        if (initializeFromSavedState) loadSavedGame();
        else loadNewGame();
        this.isPaused = false;
        loadPlayerPoints();
    }

    private void loadPlayerPoints() {
        for (Player player : getPlayers()){
            playerPoints.put(player.getId(), 0);
        }
    }

    private void loadSavedGame(){
        this.gameState = ConfigManager.getSavedGameState();
    }
    private void loadNewGame(){
        List<Player> players = PlayerGenerator.generate(gameConfiguration);
        this.gameState = new GameState(GameObjectsGenerator.generate(players.size(), players, gameConfiguration), players);
    }
    public void sumPoints(String playerId, int points){
        int pastPoints = playerPoints.get(playerId);
        playerPoints.put(playerId, pastPoints+points);
    }
    public Map<String, KeyCode> getKeyBoardConfig(){
        return gameConfiguration.getKeyboardConfiguration();
    }
    public void shoot(String shipId){
        List<GameObject> newGameObjects = new ArrayList<>();
        Ship bulletsShip = null;
        for (GameObject obj : getGameObjects()){
            if (obj.getId().equals(shipId)){
                bulletsShip = (Ship) obj;
                if (bulletsShip.canShoot()) {
                    newGameObjects.add(bulletsShip.shootsBullet());
                }
                else {
                    newGameObjects.add(bulletsShip.getNewGameObject());
                }
            }
            else {
                newGameObjects.add(obj.getNewGameObject());
            }
        }
        if (bulletsShip != null && bulletsShip.canShoot()){
            newGameObjects.add(BulletGenerator.generateBullet(bulletsShip));
        }
        refreshGameState(newGameObjects, getNewPlayers());
    }
    public void moveShip(String shipId, boolean up){
        List<GameObject> newGameObjects = new ArrayList<>();
        for (GameObject obj : getGameObjects()){
            if (obj.getId().equals(shipId) && !isPaused){
                Ship ship = (Ship) obj;
                newGameObjects.add(ship.move(up));
            }
            else {
                newGameObjects.add(obj.getNewGameObject());
            }
        }
        refreshGameState(newGameObjects, getNewPlayers());
    }
    public List<Player> getNewPlayers(){
        List<Player> players = new ArrayList<>();
        for (Player player : getPlayers()){
            players.add(player.getNewPlayer());
        }
        return players;
    }
    public List<GameObject> getNewGameObjects(){
        List<GameObject> gameObjects = new ArrayList<>();
        for (GameObject gameObject : getGameObjects()){
            gameObjects.add(gameObject.getNewGameObject());
        }
        return gameObjects;
    }
    public void refreshGameState(List<GameObject> gameObjects, List<Player> players){
        this.gameState = new GameState(gameObjects, players);
    }
    public void rotateShip(String shipId, double rotation){
        List<GameObject> newGameObjects = new ArrayList<>();
        for (GameObject obj : getGameObjects()){
            if (obj.getId().equals(shipId) && !isPaused){
                Ship ship = (Ship) obj;
                newGameObjects.add(ship.rotate(rotation));

            }
            else {
                newGameObjects.add(obj.getNewGameObject());
            }
        }
        refreshGameState(newGameObjects, getNewPlayers());
    }
    public void handleCollision(String id1, String id2){
        GameObject first = null;
        GameObject second = null;
        for (GameObject gameObject : getGameObjects()){
            if (gameObject.getId().equals(id1)) first = gameObject;
            if (gameObject.getId().equals(id2)) second = gameObject;
        }
        if (first != null && second != null){
            this.gameState = CollisionHandler.handleCollision(first, second, gameState, this);
        }
        else {
            refreshGameState(getNewGameObjects(),getNewPlayers());
        }
    }

    public void update(){
        if (!isPaused && gameState != null){
            boolean hasShip = false;
            List<GameObject> newGameObjects = new ArrayList<>();
            boolean entered = false;
            for (GameObject gameObject : getGameObjects()){
                if (gameObject.getType() == GameObjectType.STARSHIP) hasShip = true;
                if (gameObject.getType() != GameObjectType.STARSHIP && !entered){
                    manageMeteorSpawn(newGameObjects);
                    entered = true;
                }
                GameObject newGameObject = gameObject.update();
                if (newGameObject != null) newGameObjects.add(newGameObject);
                else{
                    addEliminated(gameObject.getId());
                }
            }
            if (!hasShip) finishGame();
            if (getGameObjects().size() == getPlayers().size()){
                manageMeteorSpawn(newGameObjects);
            }
            this.gameState = new GameState(newGameObjects, getNewPlayers());
        }
    }
    public List<String> getEliminated(){
        return eliminated;
    }
    private void manageMeteorSpawn(List<GameObject> newGameObjects) {
        List<Meteor> meteors = new ArrayList<>();
        for (GameObject gameObject : getGameObjects()){
            if (gameObject.getType() == GameObjectType.METEOR){
                meteors.add((Meteor) gameObject);
            }
        }
        MeteorGenerator.manageMeteorGeneration(meteors, newGameObjects);
    }

    public boolean hasFinished(){
        return finished;
    }
    public void finishGame(){
        this.finished = true;
    }
    public void printLeaderBoard(){
        if (getPlayers() != null){
            System.out.println("LEADERBOARD");
            playerPoints.forEach((key, value) -> System.out.println(key + " = " + value + " points"));
        }
    }

    public List<GameObject> getGameObjects() {
        return gameState ==null ? null : gameState.getGameObjects();
    }

    public List<Player> getPlayers() {
        return gameState ==null ? null : gameState.getPlayers();
    }
    public void pauseOrResumeGame(){
        this.isPaused = !isPaused;
    }


    public boolean isPaused() {
        return isPaused;
    }
    public void saveGame(){
        ConfigManager.saveGameState(gameState);
    }
}
