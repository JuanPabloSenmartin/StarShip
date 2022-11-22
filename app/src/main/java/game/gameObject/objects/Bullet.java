package game.gameObject.objects;

import game.gameObject.GameObject;
import game.gameObject.GameObjectType;

public class Bullet extends GameObject {
    Ship ship;
    public Bullet(String id, double initialPositionX, double initialPositionY, double initialRotation, double initialHeight, double initialWidth, Ship ship) {
        super(id, GameObjectType.BULLET, initialPositionX, initialPositionY, initialRotation, true, initialHeight, initialWidth,GameObjectShape.RECTANGULAR);
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
        setRotation(ship.getRotation());
        setHiding(false);
        setxPosition(ship.getxPosition()+18);
        setyPosition(ship.getyPosition());
    }
    private void move(){
        if (colided() || runOutOfLimit()){
            hide();
        }
        else{
            double newX = getxPosition() - 2 * Math.sin(Math.PI * 2 * getRotation() / 360);
            double newY = getyPosition() + 2 * Math.cos(Math.PI * 2 * getRotation() / 360);
            setxPosition(newX);
            setyPosition(newY);
        }
    }

    private boolean runOutOfLimit() {
        return false;
    }

    private boolean colided() {
        return false;
    }
}
