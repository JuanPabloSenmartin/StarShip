package game.gameObject.objects;

import game.gameObject.GameObject;
import game.gameObject.GameObjectType;

public class Meteor extends GameObject {
    public Meteor(String id, double initialPositionX, double initialPositionY, double initialRotation, double initialHeight, double initialWidth) {
        super(id, GameObjectType.METEOR, initialPositionX, initialPositionY, initialRotation, true, initialHeight, initialWidth,GameObjectShape.ELLIPTICAL);
    }

    @Override
    public void update() {
        if (!isHiding()){
            move();
        }
    }
    private void move(){
        if (colided() || runOutOfLimit()){
            hide();
        }
        else{
            double newX = getxPosition() + 0.25 * Math.sin(Math.PI * 2 * getRotation() / 360);
            double newY = getyPosition() + 0.25 * Math.cos(Math.PI * 2 * getRotation() / 360);
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
