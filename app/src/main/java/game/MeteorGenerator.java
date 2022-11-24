package game;

import game.gameObject.GameObject;
import game.gameObject.GameObjectType;
import game.gameObject.objects.Meteor;
import game.gameObject.objects.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MeteorGenerator {
    static Random random = new Random();
    public static void manageMeteorGeneration(List<GameObject> gameObjects){
        if (getAmountOfVisibleMeteors(gameObjects) < 5){
            //adds meteor
            for (GameObject gameObject : gameObjects){
                if (gameObject.getType().equals(GameObjectType.METEOR) && gameObject.isHiding()) {
                    putOnScreen((Meteor) gameObject, getShipsList(gameObjects));
                    break;
                }
            }
        }
    }
    private static List<Ship> getShipsList(List<GameObject> gameObjects){
        List<Ship> ships = new ArrayList<>();
        for (GameObject gameObject : gameObjects){
            if (gameObject.getType().equals(GameObjectType.STARSHIP)) ships.add((Ship) gameObject);
        }
        return ships;
    }
    private static void putOnScreen(Meteor meteor, List<Ship> ships) {
        double x;
        double y;
        double direction;
        Ship targetedShip = getRandomShip(ships);
        int side = random.nextInt(4);
        double n = random.nextDouble(800);
        switch (side){
            case 0 -> {
                //meteor comes from top of screen
                x = n;
                y = 0;
            }
            case 1 -> {
                //meteor comes from left of screen
                x = 0;
                y = n;
            }
            case 2 -> {
                //meteor comes from bottom of screen
                x = n;
                y = 794;
            }
            default -> {
                //meteor comes from right of screen
                x = 794;
                y = n;
            }
        }
        direction = getDirection(x,y, targetedShip);
        setMeteorValues(meteor, x, y, direction);
    }

    private static Ship getRandomShip(List<Ship> ships) {
        return ships.get(random.nextInt(ships.size()));
    }

    private static double getDirection(double x, double y, Ship target) {
        return Math.toDegrees(Math.atan2(target.getxPosition() - x, target.getyPosition() - y)) + random.nextDouble(20);
    }

    private static void setMeteorValues(Meteor meteor, double x, double y, double direction) {
        meteor.setHiding(false);
        meteor.setDirection(direction);
        meteor.setxPosition(x);
        meteor.setyPosition(y);
        meteor.setClockwise(random.nextBoolean());
        meteor.setWidth(random.nextDouble(50, 150));
        meteor.setHeight(random.nextDouble(50, 150));
        meteor.setInitialHealthBar(meteor.calculateHealthBar());
    }

    private static int getAmountOfVisibleMeteors(List<GameObject> gameObjects){
        int sum = 0;
        for (GameObject gameObject : gameObjects){
            if (gameObject.getType().equals(GameObjectType.METEOR) && !gameObject.isHiding()) sum++;
        }
        return sum;
    }
}
