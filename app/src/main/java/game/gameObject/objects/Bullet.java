package game.gameObject.objects;

import game.gameObject.Color;
import game.gameObject.GameObject;
import game.gameObject.GameObjectShape;
import game.gameObject.GameObjectType;

import java.util.Random;

public class Bullet extends GameObject {
    private final Ship ship;
    private int damage;
    public Bullet(String id, double initialPositionX, double initialPositionY, double initialRotation, double initialHeight, double initialWidth, Ship ship, double direction, Color color) {
        super(id, GameObjectType.BULLET, initialPositionX, initialPositionY, initialRotation, true, initialHeight, initialWidth, GameObjectShape.RECTANGULAR, direction, color);
        this.ship = ship;
        this.damage = 0;
    }
    public Bullet(String id, double initialPositionX, double initialPositionY, double initialRotation, double initialHeight, double initialWidth, Ship ship, double direction, Color color, boolean isHiding) {
        super(id, GameObjectType.BULLET, initialPositionX, initialPositionY, initialRotation, isHiding, initialHeight, initialWidth, GameObjectShape.RECTANGULAR, direction, color);
        this.ship = ship;
        this.damage = 0;
    }
    public String getShipId(){
        return ship.getId();
    }

    @Override
    public void update() {
        if (!isHiding()){
            move();
        }
    }
    public void shoot(){
        Random random = new Random();
        if (ship.canShoot()){
            setDirection(ship.getRotation());
            setRotation(ship.getRotation());
            setHiding(false);
            setxPosition(ship.getxPosition()+18);
            setyPosition(ship.getyPosition());
            double n = random.nextDouble(5, 15);
            setHeight(n*3);
            setWidth(n);
            setDamage((int) (n*6));
            ship.shootsBullet();
        }
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    private void move(){
        if (runOutOfLimit()){
            hide();
        }
        else{
            double newX = getxPosition() - 2 * Math.sin(Math.PI * 2 * getDirection() / 360);
            double newY = getyPosition() + 2 * Math.cos(Math.PI * 2 * getDirection() / 360);
            setxPosition(newX);
            setyPosition(newY);
        }
    }

    private boolean runOutOfLimit() {
        return !isInsideLimit();
    }


    public Ship getShip() {
        return ship;
    }

}
