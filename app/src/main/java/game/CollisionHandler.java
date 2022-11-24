package game;

import game.gameObject.GameObject;
import game.gameObject.GameObjectType;
import game.gameObject.objects.Bullet;
import game.gameObject.objects.Meteor;
import game.gameObject.objects.Ship;

import java.util.List;
import java.util.Objects;

public class CollisionHandler {
    public static void handleCollision(GameObject first, GameObject second, List<GameObject> gameObjects, List<Player> players){
        if ((first.getType() == GameObjectType.BULLET && second.getType() == GameObjectType.STARSHIP) || (second.getType() == GameObjectType.BULLET && first.getType() == GameObjectType.STARSHIP)){
            //bullet and ship collision
            manageBulletShipCollision(first,second,players);
        }
        else if ((first.getType() == GameObjectType.BULLET && second.getType() == GameObjectType.METEOR) || (second.getType() == GameObjectType.BULLET && first.getType() == GameObjectType.METEOR)){
            //bullet and meteor collision
            manageBulletMeteorCollision(first, second, players, gameObjects);
        }
        else if ((first.getType() == GameObjectType.STARSHIP && second.getType() == GameObjectType.METEOR) || (second.getType() == GameObjectType.STARSHIP && first.getType() == GameObjectType.METEOR)){
            //ship and meteor collision
            manageShipMeteorCollision(first, second, players);
        }
    }
    private static void manageBulletShipCollision(GameObject first, GameObject second, List<Player> players){
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
        if (Objects.equals(ship.getId(), bullet.getShipId())) return;
        Player player = getPlayer(ship.getPlayerId(), players);
        bullet.hide();
        player.removeLife();
        if (player.getLives() > 0){
            ship.resetShipPosition();
        }
        else{
            player.setAlive(false);
            ship.hide();
        }
    }
    private static void manageBulletMeteorCollision(GameObject first, GameObject second, List<Player> players, List<GameObject> gameObjects){
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
        Player player = getPlayer(bullet.getShipId(), players, gameObjects);
        bullet.hide();
        meteor.takeDamage(bullet.getDamage());
        if (meteor.getCurrentHealthBar() < 0){
            player.addPoints(meteor.getPoints());
            meteor.hide();
        }
    }
    private static void manageShipMeteorCollision(GameObject first, GameObject second, List<Player> players){
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
        Player player = getPlayer(ship.getPlayerId(), players);
        meteor.hide();
        player.removeLife();
        if (player.getLives() > 0){
            ship.resetShipPosition();
        }
        else{
            player.setAlive(false);
            ship.hide();
        }
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
