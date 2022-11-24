package game.gameObject.objects;

import game.gameObject.Color;
import game.gameObject.GameObject;
import game.gameObject.GameObjectShape;
import game.gameObject.GameObjectType;

public class Ship extends GameObject {
    private long lastBulletShot;
    private final String playerId;
    private double boost;

    public Ship(String id, double initialPositionX, double initialPositionY, double initialRotation, double initialHeight, double initialWidth, String playerId, Color color) {
        super(id, GameObjectType.STARSHIP, initialPositionX, initialPositionY, initialRotation, false, initialHeight, initialWidth, GameObjectShape.TRIANGULAR, initialRotation, color);
        this.playerId = playerId;
        this.lastBulletShot = System.currentTimeMillis();
        this.boost = 0;
    }
    public Ship(String id, double initialPositionX, double initialPositionY, double initialRotation, double initialHeight, double initialWidth, String playerId, Color color, long lastBulletShot, boolean isHiding, double direction) {
        super(id, GameObjectType.STARSHIP, initialPositionX, initialPositionY, initialRotation, isHiding, initialHeight, initialWidth, GameObjectShape.TRIANGULAR, direction, color);
        this.playerId = playerId;
        this.lastBulletShot = lastBulletShot;
        this.boost = 0;
    }

    @Override
    public void update() {
        if (boost > 0){
            double newX =  getxPosition() -  1.5 * Math.sin(Math.PI * 2 * getDirection() / 360);
            double newY =  getyPosition() +  1.5 * Math.cos(Math.PI * 2 * getDirection() / 360);
            if (!isInsideLimit(newX, newY)){
                this.boost = 0;
            }
            else{
                setxPosition(newX);
                setyPosition(newY);
                this.boost -= 5;
            }
        }
    }
    public void move(boolean up){
        if (up) addBoost();
        else slowDown();

    }
    public void addBoost(){
        if (boost < 1000){
            this.boost += 70;
        }
    }
    public void slowDown(){
        if (boost > 0){
            this.boost -= 170;
        }
    }
    public void rotate(double rotation){
        setRotation(getRotation() + rotation);
        setDirection(getRotation());
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
        setDirection(180);
        this.boost = 0;
    }
    public String getPlayerId() {
        return playerId;
    }

    public long getLastBulletShot() {
        return lastBulletShot;
    }
}
