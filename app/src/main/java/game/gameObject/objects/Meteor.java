package game.gameObject.objects;

import game.gameObject.objects.enums.Color;
import game.gameObject.GameObject;
import game.gameObject.objects.enums.GameObjectShape;
import game.gameObject.objects.enums.GameObjectType;

public class Meteor extends GameObject {
    private final boolean clockwise;
    private final int initialHealthBar;
    private final int currentHealthBar;

    public Meteor(String id, double positionX, double positionY, double rotation, double height, double width, double direction, boolean clockwise, int initialHealthBar, int currentHealthBar) {
        super(id, GameObjectType.METEOR, positionX, positionY, rotation, height, width, GameObjectShape.ELLIPTICAL, direction, Color.RED);
        this.clockwise = clockwise;
        this.initialHealthBar = initialHealthBar;
        this.currentHealthBar = currentHealthBar;
    }

    @Override
    public Meteor update() {
        if (runOutOfLimit(3,3)){
            return null;
        }
        return move();
    }

    @Override
    public GameObject getNewGameObject() {
        return new Meteor(getId(), getxPosition(), getyPosition(),getRotation(),getHeight(),getWidth(),getDirection(),clockwise,initialHealthBar,currentHealthBar);
    }

    private Meteor move(){
        double newX =  getxPosition() + 0.2 * Math.sin(Math.PI * 2 * getDirection() / 360);
        double newY =  getyPosition() + 0.2 * Math.cos(Math.PI * 2 * getDirection() / 360);
        double newRotation;
        if (clockwise) {
            newRotation = getRotation() + 2;
        } else {
            newRotation = getRotation() - 2;
        }
        return new Meteor(getId(), newX, newY,newRotation,getHeight(),getWidth(),getDirection(),clockwise,initialHealthBar,currentHealthBar);
    }

    private boolean runOutOfLimit(double shiftx, double shifty) {
        return !isInsideLimit(getxPosition()+shiftx, getyPosition()+shifty);
    }

    public boolean isClockwise() {
        return clockwise;
    }


    public int getCurrentHealthBar() {
        return currentHealthBar;
    }
    public int getInitialHealthBar(){
        return initialHealthBar;
    }
    public int getPoints(){
        return initialHealthBar;
    }
}
