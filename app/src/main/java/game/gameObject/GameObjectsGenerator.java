package game.gameObject;

import game.Player;
import game.config.GameConfiguration;
import game.gameObject.objects.Ship;
import game.gameObject.objects.enums.BulletType;
import game.gameObject.objects.enums.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameObjectsGenerator {
    public static List<GameObject> generate(int amountOfShips, List<Player> players, GameConfiguration gameConfiguration){
        List<GameObject> objects = new ArrayList<>();
        addShips(objects, amountOfShips, players, gameConfiguration);
        return objects;
    }
    private static void addShips(List<GameObject> objects, int amountOfShips, List<Player> players, GameConfiguration gameConfiguration){
        Map<String, Color> shipColors = gameConfiguration.getShipColors();
        Map<String, BulletType> bulletTypes = gameConfiguration.getBulletTypes();
        for (int i = 1; i < amountOfShips+1; i++) {
            String id = "starship-" + i;
            double xPosition = ((800 / amountOfShips) * i)/2;
            Player player = players.get(i-1);
            Ship ship = new Ship(id, xPosition, 300, 180, 40, 40, player.getId(), shipColors.get("color-"+i), System.currentTimeMillis(), 180, 0, bulletTypes.get("bulletType-"+i));
            objects.add(ship);
        }
    }

}
