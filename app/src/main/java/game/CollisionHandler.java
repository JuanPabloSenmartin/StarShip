package game;

import game.gameObject.GameObject;
import game.gameObject.objects.enums.GameObjectType;
import game.gameObject.objects.Bullet;
import game.gameObject.objects.Meteor;
import game.gameObject.objects.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollisionHandler {
    public static GameState handleCollision(GameObject first, GameObject second, GameState gameState, Game game){
        List<GameObject> gameObjects = gameState.getGameObjects();
        List<Player> players = gameState.getPlayers();
        GameState newGameState = null;
        if ((first.getType() == GameObjectType.BULLET && second.getType() == GameObjectType.STARSHIP) || (second.getType() == GameObjectType.BULLET && first.getType() == GameObjectType.STARSHIP)){
            //bullet and ship collision
            newGameState = manageBulletShipCollision(first,second,gameObjects, players, game);
        }
        else if ((first.getType() == GameObjectType.BULLET && second.getType() == GameObjectType.METEOR) || (second.getType() == GameObjectType.BULLET && first.getType() == GameObjectType.METEOR)){
            //bullet and meteor collision
            newGameState = manageBulletMeteorCollision(first, second, players, gameObjects, game);
        }
        else if ((first.getType() == GameObjectType.STARSHIP && second.getType() == GameObjectType.METEOR) || (second.getType() == GameObjectType.STARSHIP && first.getType() == GameObjectType.METEOR)){
            //ship and meteor collision
            newGameState = manageShipMeteorCollision(first, second, players, gameObjects, game);
        }
        if (newGameState == null) return new GameState(game.getNewGameObjects(), game.getNewPlayers());
        return newGameState;
    }
    private static GameState manageBulletShipCollision(GameObject first, GameObject second,List<GameObject> gameObjects, List<Player> players, Game game){
        Bullet bullet;
        Ship ship;
        if (first.getType() == GameObjectType.BULLET){
            bullet = (Bullet) first;
            ship = (Ship) second;
        }
        else{
            bullet = (Bullet) second;
            ship = (Ship) first;
        }
        if (Objects.equals(ship.getId(), bullet.getShipId())) return null;

        List<GameObject> newGameObjects = new ArrayList<>();
        List<Player> newPlayers = new ArrayList<>();
        Player shipPlayer = getPlayer(ship.getPlayerId(), players);
        Player newPlayer = null;
        for (GameObject gameObject : gameObjects){
            if (bullet.getId().equals(gameObject.getId())){
                game.addEliminated(gameObject.getId());
            }
            else if (gameObject.getId().equals(ship.getId())) {
                newPlayer = new Player(shipPlayer.getId(),shipPlayer.getPoints(), shipPlayer.getLives()-1, shipPlayer.getShipId());
                if (newPlayer.getLives() > 0){
                    //reset position
                    newGameObjects.add(new Ship(ship.getId(),300,300,180, ship.getHeight(),ship.getWidth(),ship.getPlayerId(),ship.getColor(),ship.getLastBulletShot(),180,0, ship.getBulletType()));
                }
                else{
                    game.addEliminated(ship.getId());
                }
            }
            else{
                newGameObjects.add(gameObject.getNewGameObject());
            }
        }
        for (Player player : players){
            if (player.getId().equals(shipPlayer.getId())){
                if (newPlayer == null) continue;
                newPlayers.add(newPlayer);
            }
            else {
                newPlayers.add(player.getNewPlayer());
            }
        }
        if (newPlayers.isEmpty()) game.finishGame();
        return new GameState(newGameObjects, newPlayers);
    }
    private static GameState manageBulletMeteorCollision(GameObject first, GameObject second, List<Player> players, List<GameObject> gameObjects, Game game){
        Bullet bullet;
        Meteor meteor;
        if (first.getType() == GameObjectType.BULLET){
            bullet = (Bullet) first;
            meteor = (Meteor) second;
        }
        else{
            bullet = (Bullet) second;
            meteor = (Meteor) first;
        }
        List<GameObject> newGameObjects = new ArrayList<>();
        List<Player> newPlayers = new ArrayList<>();
        Player shipPlayer = getPlayer(bullet.getShipId(), players, gameObjects);
        Player newPlayer = null;
        for (GameObject gameObject : gameObjects){
            if (bullet.getId().equals(gameObject.getId())){
                game.addEliminated(gameObject.getId());
            }
            else if (gameObject.getId().equals(meteor.getId())) {
                Meteor newMeteor = new Meteor(meteor.getId(), meteor.getxPosition(), meteor.getyPosition(), meteor.getRotation(), meteor.getHeight(),meteor.getWidth(),meteor.getDirection(), meteor.isClockwise(), meteor.getInitialHealthBar(), meteor.getCurrentHealthBar()-bullet.getDamage());
                if (newMeteor.getCurrentHealthBar() <= 0){
                    newPlayer = new Player(shipPlayer.getId(),shipPlayer.getPoints() + newMeteor.getPoints(), shipPlayer.getLives(), shipPlayer.getShipId());
                    game.sumPoints(newPlayer.getId(), newMeteor.getPoints());
                    game.addEliminated(gameObject.getId());
                }
                else{
                    newGameObjects.add(newMeteor);
                    newPlayer = new Player(shipPlayer.getId(),shipPlayer.getPoints(), shipPlayer.getLives(), shipPlayer.getShipId());
                }
            }
            else{
                newGameObjects.add(gameObject.getNewGameObject());
            }
        }
        for (Player player : players){
            if (player.getId().equals(shipPlayer.getId())){
                if (newPlayer == null) continue;
                newPlayers.add(newPlayer);
            }
            else {
                newPlayers.add(player.getNewPlayer());
            }
        }
        return new GameState(newGameObjects, newPlayers);
    }
    private static GameState manageShipMeteorCollision(GameObject first, GameObject second, List<Player> players, List<GameObject> gameObjects, Game game){
        Ship ship;
        Meteor meteor;
        if (first.getType() == GameObjectType.STARSHIP){
            ship = (Ship) first;
            meteor = (Meteor) second;
        }
        else{
            ship = (Ship) second;
            meteor = (Meteor) first;
        }
        List<GameObject> newGameObjects = new ArrayList<>();
        List<Player> newPlayers = new ArrayList<>();
        Player shipPlayer = getPlayer(ship.getPlayerId(), players);
        Player newPlayer = null;
        for (GameObject gameObject : gameObjects){
            if (ship.getId().equals(gameObject.getId())){
                newPlayer = new Player(shipPlayer.getId(),shipPlayer.getPoints(), shipPlayer.getLives() - 1, shipPlayer.getShipId());
                if (newPlayer.getLives() > 0){
                    newGameObjects.add(new Ship(ship.getId(),300,300,180, ship.getHeight(),ship.getWidth(),ship.getPlayerId(),ship.getColor(),ship.getLastBulletShot(),180,0, ship.getBulletType()));
                }
                else {
                    game.addEliminated(gameObject.getId());
                    newPlayer = null;
                }
            }
            else if (gameObject.getId().equals(meteor.getId())) {
                game.addEliminated(gameObject.getId());
            }
            else{
                newGameObjects.add(gameObject.getNewGameObject());
            }
        }
        for (Player player : players){
            if (player.getId().equals(shipPlayer.getId())){
                if (newPlayer == null) continue;
                newPlayers.add(newPlayer);
            }
            else {
                newPlayers.add(player.getNewPlayer());
            }
        }
        if (newPlayers.isEmpty()) game.finishGame();
        return new GameState(newGameObjects, newPlayers);
    }

    private static Player getPlayer(String playerId, List<Player> players){
        for (Player player : players){
            if (playerId.equals(player.getId())){
                return player;
            }
        }
        return null;
    }
    private static Player getPlayer(String shipId, List<Player> players, List<GameObject> gameObjects){
        String playerId = "";
        for (GameObject gameObject : gameObjects){
            if (gameObject.getType() == GameObjectType.STARSHIP && gameObject.getId().equals(shipId)) {
                Ship ship = (Ship) gameObject;
                playerId = ship.getPlayerId();
            }
        }
        return getPlayer(playerId, players);
    }

}
