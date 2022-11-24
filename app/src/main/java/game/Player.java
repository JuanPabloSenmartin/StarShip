package game;

import game.gameObject.objects.Ship;

public class Player {
    private final String id;
    private int points;
    private int lives;
    private Ship ship;
    private boolean alive;

    public Player(String id, int amountOfLives) {
        this.id = id;
        this.points = 0;
        this.lives = amountOfLives;
        this.alive = true;
    }

    public Player(String id, int points, int lives, Ship ship, boolean alive) {
        this.id = id;
        this.points = points;
        this.lives = lives;
        this.ship = ship;
        this.alive = alive;
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
    public String getShipId(){
        return ship.getId();
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
