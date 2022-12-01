package game.gameObject.objects;

import game.gameObject.objects.enums.BulletType;
import game.gameObject.objects.enums.Color;
import game.gameObject.GameObject;
import game.gameObject.objects.enums.GameObjectShape;
import game.gameObject.objects.enums.GameObjectType;

public class Ship extends GameObject {
    private final long lastBulletShot;
    private final String playerId;
    private final double boost;
    private final BulletType bulletType;

    public Ship(String id,  double positionX, double positionY, double rotation, double height, double width, String playerId, Color color, long lastBulletShot, double direction, double boost, BulletType bulletType) {
        super(id, GameObjectType.STARSHIP, positionX, positionY, rotation, height, width, GameObjectShape.TRIANGULAR, direction, color);
        this.playerId = playerId;
        this.lastBulletShot = lastBulletShot;
        this.boost = boost;
        this.bulletType = bulletType;
    }

    @Override
    public Ship update() {
        if (boost > 0){
            double newX =  getxPosition() -  1.5 * Math.sin(Math.PI * 2 * getDirection() / 360);
            double newY =  getyPosition() +  1.5 * Math.cos(Math.PI * 2 * getDirection() / 360);
            if (!isInsideLimit(newX, newY)){
                return new Ship(getId(), getxPosition(), getyPosition(), getRotation(), getHeight(),getWidth(),playerId,getColor(),lastBulletShot, getDirection(), 0, bulletType);
            }
            else{
                return new Ship(getId(), newX, newY, getRotation(), getHeight(),getWidth(),playerId,getColor(),lastBulletShot, getDirection(), boost - 5, bulletType);
            }
        }
        return (Ship) getNewGameObject();
    }

    @Override
    public GameObject getNewGameObject() {
        return new Ship(getId(), getxPosition(), getyPosition(), getRotation(), getHeight(),getWidth(),playerId,getColor(),lastBulletShot, getDirection(), boost, bulletType);
    }

    public Ship move(boolean up){
        if (up) {
            return addBoost();
        }
        else return slowDown();
    }
    public Ship addBoost(){
        if (boost < 1000){
            return new Ship(getId(), getxPosition(), getyPosition(), getRotation(), getHeight(),getWidth(),getPlayerId(),getColor(),getLastBulletShot(), getDirection(), boost + 70, bulletType);
        }
        return (Ship) getNewGameObject();
    }

    public Ship slowDown(){
        if (boost > 0){
            return new Ship(getId(), getxPosition(), getyPosition(), getRotation(), getHeight(),getWidth(),getPlayerId(),getColor(),getLastBulletShot(), getDirection(), boost - 170, bulletType);
        }
        return (Ship) getNewGameObject();
    }
    public Ship rotate(double rotation){
        return new Ship(getId(), getxPosition(), getyPosition(), getRotation() + rotation, getHeight(),getWidth(),playerId,getColor(),lastBulletShot, getDirection() + rotation, boost, bulletType);
    }
    public Ship shootsBullet(){
        return new Ship(getId(), getxPosition(), getyPosition(), getRotation(), getHeight(),getWidth(),playerId,getColor(),System.currentTimeMillis(), getDirection(), boost, bulletType);
    }
    public boolean canShoot(){
        return System.currentTimeMillis() - lastBulletShot > 500;
    }
    public String getPlayerId() {
        return playerId;
    }

    public long getLastBulletShot() {
        return lastBulletShot;
    }

    public double getBoost() {
        return boost;
    }

    public BulletType getBulletType() {
        return bulletType;
    }
}
