package game.config;

import game.GameState;
import game.Player;
import game.gameObject.objects.enums.BulletType;
import game.gameObject.objects.enums.Color;
import game.gameObject.GameObject;
import game.gameObject.objects.Bullet;
import game.gameObject.objects.Meteor;
import game.gameObject.objects.Ship;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager extends Config{

    public static void saveGameState(GameState gameState){
        writeGameStateInFile(gameState.getGameObjects(), gameState.getPlayers());
    }

    private static void writeGameStateInFile(List<GameObject> gameObjects,  List<Player> players){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(getDirectory()))){
            writeGameObjects(gameObjects, writer);
            writePlayers(players, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writePlayers(List<Player> players, BufferedWriter writer) throws IOException {
        for (Player player : players){
            String toWrite = getStringToWrite(player);
            writer.write(toWrite + "\n");
        }
    }

    private static void writeGameObjects(List<GameObject> gameObjects, BufferedWriter writer) throws IOException {
        for (GameObject gameObject : gameObjects){
            String toWrite = getStringToWrite(gameObject);
            writer.write(toWrite + "\n");
        }
        writer.write("%\n"); //separates gameObjects from players
    }

    private static String getStringToWrite(GameObject gameObject) {
        String str = "id:" + gameObject.getId() + ";type:" + gameObject.getType().toString() + ";xPosition:" + gameObject.getxPosition() + ";yPosition:" + gameObject.getyPosition() + ";rotation:" + gameObject.getRotation() + ";direction:" + gameObject.getDirection()
                 + ";height:" + gameObject.getHeight() + ";width:" + gameObject.getWidth() + ";shape:" + gameObject.getShape() + ";color:" + gameObject.getColor().toString();
        return str + addUniqueParameters(gameObject);
    }

    private static String addUniqueParameters(GameObject gameObject) {
        switch (gameObject.getType()){
            case STARSHIP -> {
                Ship ship = (Ship) gameObject;
                return ";lastBulletShot:" + ship.getLastBulletShot() + ";playerId:" + ship.getPlayerId() + ";boost:" + ship.getBoost() + ";bulletType:" + ship.getBulletType();
            }
            case BULLET -> {
                Bullet bullet = (Bullet) gameObject;
                return ";shipId:" + bullet.getShipId() + ";damage:" + bullet.getDamage() + ";bulletType:" + bullet.getBulletType();
            }
            case METEOR -> {
                Meteor meteor = (Meteor) gameObject;
                return ";clockwise:" + meteor.isClockwise() + ";initialHealthBar:" + meteor.getInitialHealthBar() + ";currentHealthBar:" + meteor.getCurrentHealthBar();
            }
        };
        return "";
    }

    private static String getStringToWrite(Player player) {
        return "id:" + player.getId() + ";points:" + player.getPoints() + ";lives:" + player.getLives() + ";shipId:" + player.getShipId();
    }
    private static String getDirectory(){
        return "app\\src\\main\\java\\game\\config\\config";
    }

    public static GameState getSavedGameState() {
        List<String> configLines = getLines(getDirectory());
        List<String> o = null;
        List<String> p = null;
        for (int i = 0; i < configLines.size(); i++) {
            String line = configLines.get(i);
            if (line.equals("%")){
                o = configLines.subList(0, i);
                p = configLines.subList(i+1, configLines.size());
                break;
            }
        }
        List<GameObject> gameObjects = getSavedGameObjects(o);
        List<Player> players = getSavedPlayers(p);
        return new GameState(gameObjects, players);
    }


    private static List<Player> getSavedPlayers(List<String> lines) {
        List<Player> players = new ArrayList<>();
        for (String line : lines){
            String[] parts = line.split(";");
            String id = (String) transform(parts[0]);
            int points = (int) transform(parts[1]);
            int lives = (int) transform(parts[2]);
            String shipId = (String) transform(parts[3]);
            Player player = new Player(id, points, lives, shipId);
            players.add(player);
        }
        return players;
    }
    private static Object transform(String line){
        String[] str = line.split(":");
        String variable = str[0];
        String value = str[1];
        return switch (variable){
            case "id", "playerId", "type", "color", "shape", "shipId", "bulletType" -> value;
            case "points", "lives", "initialHealthBar", "currentHealthBar", "damage" -> Integer.parseInt(value);
            case "clockwise" -> Boolean.parseBoolean(value);
            case "xPosition", "yPosition", "rotation", "direction", "height", "width", "boost" -> Double.parseDouble(value);
            case "lastBulletShot" -> Long.parseLong(value);
            default -> "";
        };
    }

    private static List<GameObject> getSavedGameObjects(List<String> lines) {
        List<GameObject> gameObjects = new ArrayList<>();
        for (String line : lines){
            String[] parts = line.split(";");
            String id = (String) transform(parts[0]);
            String type = (String) transform(parts[1]);
            double xPosition = (double) transform(parts[2]);
            double yPosition = (double) transform(parts[3]);
            double rotation = (double) transform(parts[4]);
            double direction = (double) transform(parts[5]);
            double height = (double) transform(parts[6]);
            double width = (double) transform(parts[7]);
            String shape = (String) transform(parts[8]);
            String color = (String) transform(parts[9]);
            gameObjects.add(createGameObject(parts, id, type, xPosition, yPosition, rotation, direction, height, width, color));
        }
        return gameObjects;
    }

    private static GameObject createGameObject(String[] parts, String id, String type, double xPosition, double yPosition, double rotation, double direction, double height, double width, String color){
        switch (type){
            case "STARSHIP" -> {
                long lastBulletShot = (long) transform(parts[10]);
                String playerId = (String) transform(parts[11]);
                double boost = (double) transform(parts[12]);
                String bulletType = (String) transform(parts[13]);
                return new Ship(id, xPosition, yPosition, rotation, height, width, playerId, getColor(color), lastBulletShot, direction, boost, getBulletType(bulletType));
            }
            case "METEOR" -> {
                boolean clockwise = (boolean) transform(parts[10]);
                int initialHealthBar = (int) transform(parts[11]);
                int currentHealthBar = (int) transform(parts[12]);
                return new Meteor(id, xPosition, yPosition, rotation, height, width, direction, clockwise, initialHealthBar,currentHealthBar);
            }
            case "BULLET" -> {
                String shipId = (String) transform(parts[10]);
                int damage = (int) transform(parts[11]);
                String bulletType = (String) transform(parts[12]);
                return new Bullet(id, xPosition, yPosition, rotation, height, width, direction, getColor(color),shipId, damage, getBulletType(bulletType));
            }
        }
        return null;
    }
    private static Color getColor(String color){
        return color.equals("RED") ? Color.RED : Color.BLUE;
    }
    private static BulletType getBulletType(String s) {
        return switch (s) {
            case "PLASMA" -> BulletType.PLASMA;
            case "LIGHTNING" -> BulletType.LIGHTNING;
            default -> BulletType.LASER;
        };
    }
}
