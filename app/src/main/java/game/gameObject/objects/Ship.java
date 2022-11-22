package game.gameObject.objects;

import game.gameObject.Color;
import game.gameObject.GameObject;
import game.gameObject.GameObjectShape;
import game.gameObject.GameObjectType;

public class Ship extends GameObject {
    long lastBulletShot;
    private final String playerId;

    public Ship(String id, double initialPositionX, double initialPositionY, double initialRotation, double initialHeight, double initialWidth, String playerId, Color color) {
        super(id, GameObjectType.STARSHIP, initialPositionX, initialPositionY, initialRotation, false, initialHeight, initialWidth, GameObjectShape.TRIANGULAR, 0, color);
        this.playerId = playerId;
        this.lastBulletShot = System.currentTimeMillis();
    }

    @Override
    public void update() {

    }
    public void move(double shiftX, double shiftY){
        if (isInsideLimit(getxPosition()+shiftX, getyPosition()+shiftY)){
            setxPosition(getxPosition() + shiftX);
            setyPosition(getyPosition() + shiftY);
        }
    }
    public void rotate(double rotation){
        setRotation(getRotation() + rotation);
    }
    public void shootsBullet(){
        this.lastBulletShot = System.currentTimeMillis();
    }
    public boolean canShoot(){
        return System.currentTimeMillis() - lastBulletShot > 500;
    }
    public void resetShipPosition(){
        setxPosition(300);
        setyPosition(300);
        setRotation(180);
    }
    public String getPlayerId() {
        return playerId;
    }


}
