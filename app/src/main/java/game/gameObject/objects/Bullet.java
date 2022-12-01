package game.gameObject.objects;

import game.gameObject.objects.enums.Color;
import game.gameObject.GameObject;
import game.gameObject.objects.enums.GameObjectShape;
import game.gameObject.objects.enums.GameObjectType;
import game.gameObject.objects.enums.BulletType;


public class Bullet extends GameObject {
    private final String shipId;
    private final int damage;
    private final BulletType bulletType;

    public Bullet(String id, double positionX, double positionY, double rotation, double height, double width, double direction, Color color, String shipId, int damage, BulletType bulletType) {
        super(id, GameObjectType.BULLET, positionX, positionY, rotation, height, width, GameObjectShape.RECTANGULAR, direction, color);
        this.shipId = shipId;
        this.damage = damage;
        this.bulletType = bulletType;
    }
    public String getShipId(){
        return shipId;
    }

    @Override
    public Bullet update() {
        if (runOutOfLimit()){
            return null;
        }
        return move();
    }

    @Override
    public GameObject getNewGameObject() {
        return new Bullet(getId(), getxPosition(),getyPosition(),getRotation(),getHeight(),getWidth(),getDirection(),getColor(),shipId, damage, bulletType);
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public int getDamage() {
        return damage;
    }


    private Bullet move(){
        double newX = getxPosition() - 4 * Math.sin(Math.PI * 2 * getDirection() / 360);
        double newY = getyPosition() + 4 * Math.cos(Math.PI * 2 * getDirection() / 360);
        return new Bullet(getId(), newX,newY,getRotation(),getHeight(),getWidth(),getDirection(),getColor(),shipId, damage, bulletType);
    }

    private boolean runOutOfLimit() {
        return !isInsideLimit();
    }
}
