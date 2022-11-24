package game.config;

import game.GameState;
import game.Player;
import game.gameObject.Color;
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
                + ";isHiding:" + gameObject.isHiding() + ";height:" + gameObject.getHeight() + ";width:" + gameObject.getWidth() + ";shape:" + gameObject.getShape() + ";color:" + gameObject.getColor().toString();
        return str + addUniqueParameters(gameObject);
    }

    private static String addUniqueParameters(GameObject gameObject) {
        switch (gameObject.getType()){
            case STARSHIP -> {
                Ship ship = (Ship) gameObject;
                return ";lastBulletShot:" + ship.getLastBulletShot() + ";playerId:" + ship.getPlayerId();
            }
            case BULLET -> {
                Bullet bullet = (Bullet) gameObject;
                return ";ship:" + bullet.getShipId();
            }
            case METEOR -> {
                Meteor meteor = (Meteor) gameObject;
                return ";clockwise:" + meteor.isClockwise();
            }
        };
        return "";
    }

    private static String getStringToWrite(Player player) {
        return "id:" + player.getId() + ";points:" + player.getPoints() + ";lives:" + player.getLives() + ";ship:" + player.getShipId() + ";alive:" + player.isAlive();
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
        List<Player> players = getSavedPlayers(p, gameObjects);
        return new GameState(gameObjects, players);
    }


    private static List<Player> getSavedPlayers(List<String> lines,  List<GameObject> gameObjects) {
        List<Player> players = new ArrayList<>();
        for (String line : lines){
            String[] parts = line.split(";");
            String id = (String) transform(parts[0]);
            int points = (int) transform(parts[1]);
            int lives = (int) transform(parts[2]);
            Ship ship = getShipFromId(parts[3].split(":")[1],gameObjects);
            boolean alive = (boolean) transform(parts[4]);
            Player player = new Player(id, points, lives, ship, alive);
            players.add(player);
        }
        return players;
    }
    private static Object transform(String line){
        String[] str = line.split(":");
        String variable = str[0];
        String value = str[1];
        return switch (variable){
            case "id", "playerId", "type", "color", "shape", "ship" -> value;
            case "points", "lives" -> Integer.parseInt(value);
            case "alive", "isHiding", "clockwise" -> Boolean.parseBoolean(value);
            case "xPosition", "yPosition", "rotation", "direction", "height", "width" -> Double.parseDouble(value);
            case "lastBulletShot" -> Long.parseLong(value);
            default -> "";
        };
    }
    private static Ship getShipFromId(String id, List<GameObject> gameObjects){
        for (GameObject gameObject : gameObjects){
            if (id.equals(gameObject.getId())) return (Ship) gameObject;
        }
        return null;
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
            boolean isHiding = (boolean) transform(parts[6]);
            double height = (double) transform(parts[7]);
            double width = (double) transform(parts[8]);
            String shape = (String) transform(parts[9]);
            String color = (String) transform(parts[10]);
            gameObjects.add(createGameObject(parts, id, type, xPosition, yPosition, rotation, direction, isHiding, height, width, color, gameObjects));
        }
        return gameObjects;
    }

    private static GameObject createGameObject(String[] parts, String id, String type, double xPosition, double yPosition, double rotation, double direction, boolean isHiding, double height, double width, String color, List<GameObject> gameObjects){
        switch (type){
            case "STARSHIP" -> {
                long lastBulletShot = (long) transform(parts[11]);
                String playerId = (String) transform(parts[12]);
                return new Ship(id, xPosition, yPosition, rotation, height, width, playerId, getColor(color), lastBulletShot, isHiding, direction);
            }
            case "METEOR" -> {
                boolean clockwise = (boolean) transform(parts[11]);
                return new Meteor(id, xPosition, yPosition, rotation, height, width, direction, clockwise, isHiding);
            }
            case "BULLET" -> {
                Ship ship = getShipFromId((String) transform(parts[11]), gameObjects);
                return new Bullet(id, xPosition, yPosition, rotation, height, width, ship, direction, getColor(color), isHiding);
            }
        }
        return null;
    }
    private static Color getColor(String color){
        return color.equals("RED") ? Color.RED : Color.BLUE;
    }
}
