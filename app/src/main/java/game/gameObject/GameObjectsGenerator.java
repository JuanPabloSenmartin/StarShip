package game.gameObject;

import game.Player;
import game.config.GameConfiguration;
import game.gameObject.objects.Bullet;
import game.gameObject.objects.Meteor;
import game.gameObject.objects.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameObjectsGenerator {
    public static List<GameObject> generate(int amountOfMeteors, int amountOfBullets, int amountOfShips, List<Player> players, GameConfiguration gameConfiguration){
        List<GameObject> objects = new ArrayList<>();
        addShips(objects, amountOfShips, players, gameConfiguration);
        addMeteors(objects, amountOfMeteors);
        addBullets(objects, amountOfShips, amountOfBullets);
        return objects;
    }
    private static void addShips(List<GameObject> objects, int amountOfShips, List<Player> players, GameConfiguration gameConfiguration){
        Map<String, Color> shipColors = gameConfiguration.getShipColors();
        for (int i = 1; i < amountOfShips+1; i++) {
            String id = "starship-" + i;
            double xPosition = ((800 / amountOfShips) * i)/2;
            Player player = players.get(i-1);
            Ship ship = new Ship(id, xPosition, 300, 180, 40, 40, player.getId(), shipColors.get("color-"+i));
            player.setShip(ship);
            objects.add(ship);
        }
    }
    private static void addMeteors(List<GameObject> objects, int amountOfMeteors){
        for (int i = 1; i < amountOfMeteors+1; i++) {
            String id = "meteor-" + i;
            double[] pos = NonVisibleObjectsManager.getEmptyPlace();
            objects.add(new Meteor(id, pos[0], pos[1], 180, 30, 20, 180));
        }
    }
    private static void addBullets(List<GameObject> objects, int amountOfShips, int amountOfBullets){
        for (int i = 1; i < amountOfBullets+1; i++) {
            String id = "bullet-" + i;
            double[] pos = NonVisibleObjectsManager.getEmptyPlace();
            Ship ship = assignShip(objects, amountOfBullets, amountOfShips, i);
            objects.add(new Bullet(id, pos[0], pos[1], 180, 15, 5, ship, 180, ship.getColor()));
        }
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
