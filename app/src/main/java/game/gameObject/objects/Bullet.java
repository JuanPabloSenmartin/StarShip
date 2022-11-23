package game.gameObject.objects;

import game.gameObject.Color;
import game.gameObject.GameObject;
import game.gameObject.GameObjectShape;
import game.gameObject.GameObjectType;

public class Bullet extends GameObject {
    private final Ship ship;
    public Bullet(String id, double initialPositionX, double initialPositionY, double initialRotation, double initialHeight, double initialWidth, Ship ship, double direction, Color color) {
        super(id, GameObjectType.BULLET, initialPositionX, initialPositionY, initialRotation, true, initialHeight, initialWidth, GameObjectShape.RECTANGULAR, direction, color);
        this.ship = ship;
    }
    public Bullet(String id, double initialPositionX, double initialPositionY, double initialRotation, double initialHeight, double initialWidth, Ship ship, double direction, Color color, boolean isHiding) {
        super(id, GameObjectType.BULLET, initialPositionX, initialPositionY, initialRotation, isHiding, initialHeight, initialWidth, GameObjectShape.RECTANGULAR, direction, color);
        this.ship = ship;
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
        if (ship.canShoot()){
            setDirection(ship.getRotation());
            setRotation(ship.getRotation());
            setHiding(false);
            setxPosition(ship.getxPosition()+18);
            setyPosition(ship.getyPosition());
            ship.shootsBullet();
        }

    }
    private void move(){
        if (colided() || runOutOfLimit()){
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

    private boolean colided() {
        return false;
    }

    public Ship getShip() {
        return ship;
    }

}
