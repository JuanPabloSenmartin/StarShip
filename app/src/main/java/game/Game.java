package game;

import game.config.ConfigManager;
import game.gameObject.GameObject;
import game.gameObject.GameObjectType;
import game.gameObject.GameObjectsGenerator;
import game.gameObject.objects.Bullet;
import game.gameObject.objects.Ship;

import java.util.List;
import java.util.Objects;


public class Game {
    private List<GameObject> gameObjects;
    private List<Player> players;
    private boolean isPaused;

    public void start(boolean initializeFromSavedState){
        if (initializeFromSavedState) loadSavedGame();
        else loadNewGame();
        this.isPaused = false;
    }
    private void loadSavedGame(){
        GameState gameState = ConfigManager.getSavedGameState();
        this.gameObjects = gameState.getGameObjects();
        this.players = gameState.getPlayers();
    }
    private void loadNewGame(){
        this.players = PlayerGenerator.generate(2);
        this.gameObjects = GameObjectsGenerator.generate(10, 20, players.size(), players);
    }
    public void shoot(int ship){
        if (ship < gameObjects.size() && gameObjects.get(ship).getClass().equals(Ship.class)){
            for (GameObject gameObject : gameObjects){
                Ship bulletsShip = (Ship) gameObjects.get(ship);
                if (gameObject.getType() == GameObjectType.BULLET){
                    Bullet bullet = (Bullet) gameObject;
                    if (bullet.isHiding() && Objects.equals(bullet.getShipId(), gameObjects.get(ship).getId()) && bullet.getColor() == bulletsShip.getColor()) {
                        bullet.shoot();
                        break;
                    }
                }
            }
        }
    }
    public void moveShip(int shipNum, double shiftX, double shiftY){
        if (shipNum < gameObjects.size() && gameObjects.get(shipNum).getType() == GameObjectType.STARSHIP && !isPaused){
            Ship ship = (Ship) gameObjects.get(shipNum);
            ship.move(shiftX, shiftY);
        }
    }
    public void rotateShip(int shipNum, double rotation){
        if (shipNum < gameObjects.size() && gameObjects.get(shipNum).getType() == GameObjectType.STARSHIP && !isPaused){
            Ship ship = (Ship) gameObjects.get(shipNum);
            ship.rotate(rotation);
        }
    }
    public void handleCollision(String id1, String id2){
        GameObject first = null;
        GameObject second = null;
        for (GameObject gameObject : gameObjects){
            if (gameObject.getId().equals(id1)) first = gameObject;
            if (gameObject.getId().equals(id2)) second = gameObject;
        }
        if (first != null && second != null && !first.isHiding() && !second.isHiding()){
            CollisionHandler.handleCollision(first, second, gameObjects, players);
        }
    }
    public void update(){
        if (!isPaused){
            MeteorGenerator.manageMeteorGeneration(gameObjects);
            for (GameObject gameObject : gameObjects){
                gameObject.update();
            }
        }
    }
    public boolean hasFinished(){
        for (Player player: players){
            if (player.isAlive()) return false;
        }
        return true;
    }
    public void printLeaderBoard(){
        System.out.println("LEADERBOARD");
        for (Player player : players){
            System.out.println(player.getId() + " = " + player.getPoints() + " points");
            System.out.println("-----------------------------------------------");
        }
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public List<Player> getPlayers() {
        return players;
    }
    public void pauseOrResumeGame(){
        this.isPaused = !isPaused;
    }
    public void resetGame(){
        //this.start(false);
    }

    public boolean isPaused() {
        return isPaused;
    }
    public void saveGame(){
        ConfigManager.saveGameState(new GameState(gameObjects, players));
    }
}
