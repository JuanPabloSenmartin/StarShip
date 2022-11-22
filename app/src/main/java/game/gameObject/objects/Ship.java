package game.gameObject.objects;

import game.gameObject.GameObject;
import game.gameObject.GameObjectType;

public class Ship extends GameObject {

    public Ship(String id, double initialPositionX, double initialPositionY, double initialRotation, double initialHeight, double initialWidth) {
        super(id, GameObjectType.STARSHIP, initialPositionX, initialPositionY, initialRotation, false, initialHeight, initialWidth, GameObjectShape.TRIANGULAR);
    }

    @Override
    public void update() {

    }
    public void move(double shiftX, double shiftY){
        setxPosition(getxPosition() + shiftX);
        setyPosition(getyPosition() + shiftY);
    }
    public void rotate(double rotation){
        setRotation(getRotation() + rotation);
    }
}
