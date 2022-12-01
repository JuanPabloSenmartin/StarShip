package game;

import game.gameObject.GameObject;
import game.gameObject.objects.enums.GameObjectType;
import game.gameObject.objects.Meteor;
import game.gameObject.objects.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MeteorGenerator {
    private static int count = 0;
    static Random random = new Random();
    public static void manageMeteorGeneration(List<Meteor> meteors, List<GameObject> gameObjects){
        if (meteors.size() < 5){
            //adds meteor
            addNewMeteor(gameObjects);
        }
    }
    private static List<Ship> getShipsList(List<GameObject> gameObjects){
        List<Ship> ships = new ArrayList<>();
        for (GameObject gameObject : gameObjects){
            if (gameObject.getType().equals(GameObjectType.STARSHIP)) ships.add((Ship) gameObject);
        }
        return ships;
    }
    private static void addNewMeteor(List<GameObject> gameObjects) {
        List<Ship> ships = getShipsList(gameObjects);
        double x;
        double y;
        Ship targetedShip = getRandomShip(ships);
        if (targetedShip == null) return;
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
        double direction = getDirection(x,y, targetedShip);
        String id = "meteor-" + ++count;
        double height = random.nextDouble(50, 150);
        double width = random.nextDouble(50, 150);
        int healthBar = calculateHealthBar(width,height);
        gameObjects.add(new Meteor(id, x, y, 180, height,width,direction, random.nextBoolean(), healthBar,healthBar));
    }

    private static Ship getRandomShip(List<Ship> ships) {
        return ships.isEmpty() ? null : ships.get(random.nextInt(ships.size()));
    }

    private static double getDirection(double x, double y, Ship target) {
        return Math.toDegrees(Math.atan2(target.getxPosition() - x, target.getyPosition() - y)) + random.nextDouble(20);
    }
    private static int calculateHealthBar(double width, double height){
        //average would be 100 health
        return (int) ((width * height)/100);
    }

}
