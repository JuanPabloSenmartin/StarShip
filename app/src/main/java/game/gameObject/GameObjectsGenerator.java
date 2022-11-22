package game.gameObject;

import game.gameObject.objects.Bullet;
import game.gameObject.objects.Meteor;
import game.gameObject.objects.Ship;

import java.util.ArrayList;
import java.util.List;

public class GameObjectsGenerator {
    public static List<GameObject> generate(int amountOfMeteors, int amountOfBullets, int amountOfShips){
        List<GameObject> objects = new ArrayList<>();
        //add ships
        for (int i = 1; i < amountOfShips+1; i++) {
            String id = "starship-" + i;
            double xPosition = ((800 / amountOfShips) * i)/2;
            objects.add(new Ship(id, xPosition, 300, 180, 40, 40));
        }
        //add meteors
        for (int i = 1; i < amountOfMeteors+1; i++) {
            String id = "meteor-" + i;
            objects.add(new Meteor(id, 900, 900, 180, 30, 20));
        }
        //add bullets
        for (int i = 1; i < amountOfBullets+1; i++) {
            String id = "bullet-" + i;
            objects.add(new Bullet(id, 900, 900, 180, 15, 5, assignShip(objects, amountOfBullets, amountOfShips, i)));
        }
        return objects;
    }
    private static Ship assignShip(List<GameObject> objects, int amountOfBullets, int amountOfShips, int i){
        int n = amountOfBullets/amountOfShips;
        for (int j = 0; j < amountOfShips; j++) {
            if (i<n) return (Ship) objects.get(j);
            n *= 2;
        }
        return (Ship) objects.get(amountOfShips-1);
    }
}
