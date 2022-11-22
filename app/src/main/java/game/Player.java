package game;

import game.gameObject.objects.Ship;

public class Player {
    private final String id;
    private int points;
    private int lives;
    private Ship ship;
    private boolean alive;

    public Player(String id) {
        this.id = id;
        this.points = 0;
        this.lives = 3;
        this.alive = true;
    }

    public String getId() {
        return id;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public int getPoints() {
        return points;
    }
    public void addPoints(int points){
        this.points += points;
    }
    public void setPoints(int points) {
        this.points = points;
    }

    public int getLives() {
        return lives;
    }

    public void removeLife() {
        this.lives -= 1;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
