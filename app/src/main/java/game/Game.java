package game;

import game.gameObject.GameObject;
import game.gameObject.GameObjectType;
import game.gameObject.GameObjectsGenerator;
import game.gameObject.objects.Bullet;
import game.gameObject.objects.Ship;

import java.util.List;
import java.util.Objects;

public class Game {
    private List<GameObject> gameObjects;

    public void start(){
        this.gameObjects = GameObjectsGenerator.generate(3, 3, 1);
    }
    public void shoot(int ship){
        if (ship < gameObjects.size() && gameObjects.get(ship).getClass().equals(Ship.class)){
            for (GameObject gameObject : gameObjects){
                if (gameObject.getType() == GameObjectType.BULLET){
                    Bullet bullet = (Bullet) gameObject;
                    if (bullet.isHiding() && Objects.equals(bullet.getShipId(), gameObjects.get(ship).getId())) {
                        bullet.shoot();
                        break;
                    }
                }
            }
        }
    }
    public void moveShip(int shipNum, double shiftX, double shiftY){
        if (shipNum < gameObjects.size() && gameObjects.get(shipNum).getClass().equals(Ship.class)){
            Ship ship = (Ship) gameObjects.get(shipNum);
            ship.move(shiftX, shiftY);
        }
    }
    public void rotateShip(int shipNum, double rotation){
        if (shipNum < gameObjects.size() && gameObjects.get(shipNum).getClass().equals(Ship.class)){
            Ship ship = (Ship) gameObjects.get(shipNum);
            ship.rotate(rotation);
        }
    }

    public void update(){
        for (GameObject gameObject : gameObjects){
            gameObject.update();
        }
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
}
