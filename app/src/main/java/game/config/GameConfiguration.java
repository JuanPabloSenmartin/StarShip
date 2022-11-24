package game.config;

import game.gameObject.Color;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameConfiguration extends Config{
    private final int amountOfPlayers;
    private final Map<String, KeyCode> keyboardConfiguration;
    private final int amountOfLives;
    private final Map<String, Color> shipColors;

    public GameConfiguration() {
        List<String> lines = getLines("app\\src\\main\\java\\game\\config\\GameConfiguration");
        Map<String, String> map = getMap(lines);
        this.amountOfPlayers = Integer.parseInt(map.get("amountOfPlayers"));
        this.keyboardConfiguration = getKeyBoardMap(map.get("keyBoardSettings"));
        this.amountOfLives = Integer.parseInt(map.get("amountOfLives"));
        this.shipColors = getShipColorMap(map.get("ships"));
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
        return switch (str){
            case "W" -> KeyCode.W;
            case "S" -> KeyCode.S;
            case "SPACE" -> KeyCode.SPACE;
            case "LEFT" -> KeyCode.LEFT;
            case "RIGHT" -> KeyCode.RIGHT;
            case "T" -> KeyCode.T;
            case "G" -> KeyCode.G;
            case "L" -> KeyCode.L;
            case "J" -> KeyCode.J;
            default -> KeyCode.K;
        };
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
