package game.config;

import game.gameObject.objects.enums.BulletType;
import game.gameObject.objects.enums.Color;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameConfiguration extends Config{
    private final int amountOfPlayers;
    private final Map<String, KeyCode> keyboardConfiguration;
    private final int amountOfLives;
    private final Map<String, Color> shipColors;
    private final Map<String, BulletType> bulletTypes;

    public GameConfiguration() {
        List<String> lines = getLines("app\\src\\main\\java\\game\\config\\GameConfiguration");
        Map<String, String> map = getMap(lines);
        this.amountOfPlayers = Integer.parseInt(map.get("amountOfPlayers"));
        this.keyboardConfiguration = getKeyBoardMap(map.get("keyBoardSettings"));
        this.amountOfLives = Integer.parseInt(map.get("amountOfLives"));
        this.shipColors = getShipColorMap(map.get("ships"));
        this.bulletTypes = getBulletTypesMap(map.get("bulletTypes"));
    }

    private Map<String, BulletType> getBulletTypesMap(String bulletTypes) {
        Map<String, BulletType> map = new HashMap<>();
        String[] split = bulletTypes.split(";");
        for (String s : split) {
            String[] innerSplit = s.split("=");
            map.put(innerSplit[0], getBulletType(innerSplit[1]));
        }
        return map;
    }

    private BulletType getBulletType(String s) {
        return switch (s) {
            case "PLASMA" -> BulletType.PLASMA;
            case "LIGHTNING" -> BulletType.LIGHTNING;
            default -> BulletType.LASER;
        };
    }

    public Map<String, BulletType> getBulletTypes() {
        return bulletTypes;
    }

    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public Map<String, KeyCode> getKeyboardConfiguration() {
        return keyboardConfiguration;
    }

    public int getAmountOfLives() {
        return amountOfLives;
    }

    public Map<String, Color> getShipColors() {
        return shipColors;
    }

    private Map<String, Color> getShipColorMap(String ships) {
        Map<String, Color> map = new HashMap<>();
        String[] split = ships.split(";");
        for (String s : split) {
            String[] innerSplit = s.split("=");
            map.put(innerSplit[0], getColor(innerSplit[1]));
        }
        return map;
    }

    private Color getColor(String s) {
        return s.equals("RED") ? Color.RED : Color.BLUE;
    }

    private Map<String, KeyCode> getKeyBoardMap(String keyBoardSettings) {
        Map<String, KeyCode> map = new HashMap<>();
        String[] split = keyBoardSettings.split(";");
        for (String s : split) {
            String[] innerSplit = s.split("=");
            map.put(innerSplit[0], getKeyCode(innerSplit[1]));
        }
        return map;
    }
    private KeyCode getKeyCode(String str){
        return KeyCode.getKeyCode(str);
    }
    private Map<String, String> getMap(List<String> lines) {
        Map<String, String> map = new HashMap<>();
        for (String line : lines){
            String[] split = line.split(":");
            map.put(split[0], split[1]);
        }
        return map;
    }

}
