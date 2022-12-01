package game.gameObject;

import game.gameObject.objects.enums.Color;
import game.gameObject.objects.enums.GameObjectShape;
import game.gameObject.objects.enums.GameObjectType;

public abstract class GameObject {
    private final String id;
    private final GameObjectType type;
    private final double xPosition;
    private final double yPosition;
    private final double rotation;
    private final double direction;
    private final double height;
    private final double width;
    private final GameObjectShape shape;
    private final Color color;

    public GameObject(String id, GameObjectType type, double initialPositionX, double initialPositionY, double initialRotation, double height, double width, GameObjectShape shape, double direction, Color color) {
        this.id = id;
        this.type = type;
        this.xPosition = initialPositionX;
        this.yPosition = initialPositionY;
        this.rotation = initialRotation;
        this.height = height;
        this.width = width;
        this.shape = shape;
        this.direction = direction;
        this.color = color;
    }


    public abstract GameObject update();
    public abstract GameObject getNewGameObject();
    public String getId() {
        return id;
    }


    public GameObjectType getType() {
        return type;
    }

    public double getxPosition() {
        return xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public double getRotation() {
        return rotation;
    }


    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public GameObjectShape getShape() {
        return shape;
    }

    public double getDirection() {
        return direction;
    }

    public boolean isInsideLimit(){
        return xPosition > 0 && xPosition < 800 && yPosition > 0 && yPosition < 800;
    }
    public boolean isInsideLimit(double xPosition, double yPosition){
        return xPosition > 0 && xPosition < 725 && yPosition > 0 && yPosition < 700;
    }

    public Color getColor() {
        return color;
    }
}
