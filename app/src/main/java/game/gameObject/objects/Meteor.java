package game.gameObject.objects;

import game.gameObject.Color;
import game.gameObject.GameObject;
import game.gameObject.GameObjectShape;
import game.gameObject.GameObjectType;

public class Meteor extends GameObject {
    private boolean clockwise;
    private int initialHealthBar;
    private int currentHealthBar;

    public Meteor(String id, double initialPositionX, double initialPositionY, double initialRotation, double initialHeight, double initialWidth, double direction) {
        super(id, GameObjectType.METEOR, initialPositionX, initialPositionY, initialRotation, true, initialHeight, initialWidth, GameObjectShape.ELLIPTICAL, direction, Color.RED);
        this.clockwise = true;
        this.initialHealthBar = 100;
        this.currentHealthBar = initialHealthBar;
    }
    public Meteor(String id, double initialPositionX, double initialPositionY, double initialRotation, double initialHeight, double initialWidth, double direction, boolean clockwise, boolean isHiding) {
        super(id, GameObjectType.METEOR, initialPositionX, initialPositionY, initialRotation, isHiding, initialHeight, initialWidth, GameObjectShape.ELLIPTICAL, direction, Color.RED);
        this.clockwise = clockwise;
        this.initialHealthBar = 100;//pasarlo por construcator
        this.currentHealthBar = initialHealthBar;
    }

    @Override
    public void update() {
        if (!isHiding()){
            move();
        }
    }
    private void move(){
        if (runOutOfLimit(5,5)){
            hide();
        }
        else{
            double newX =  getxPosition() + 0.8 * Math.sin(Math.PI * 2 * getDirection() / 360);
            double newY =  getyPosition() + 0.8 * Math.cos(Math.PI * 2 * getDirection() / 360);
            setxPosition(newX);
            setyPosition(newY);
            if (clockwise) {
                setRotation(getRotation() + 5);
            } else {
                setRotation(getRotation() - 5);
            }
        }
    }

    private boolean runOutOfLimit(double shiftx, double shifty) {
        return !isInsideLimit(getxPosition()+shiftx, getyPosition()+shifty);
    }

    public void setClockwise(boolean clockwise){
        this.clockwise = clockwise;
    }

    public boolean isClockwise() {
        return clockwise;
    }
    public int calculateHealthBar(){
        //average would be 100 health
        return (int) ((this.getWidth() * this.getHeight())/100);
    }
    public void takeDamage(int amount){
        this.currentHealthBar -= amount;
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
    public void setInitialHealthBar(int healthBar) {
        this.initialHealthBar = healthBar;
        this.currentHealthBar = healthBar;
    }
}
