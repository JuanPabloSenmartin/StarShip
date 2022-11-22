package game.gameObject;

import game.gameObject.objects.GameObjectShape;

public abstract class GameObject {
    private final String id;
    private final GameObjectType type;
    private double xPosition;
    private double yPosition;
    private double rotation;
    private boolean isHiding;
    private final double height;
    private final double width;
    private final GameObjectShape shape;

    public GameObject(String id, GameObjectType type, double initialPositionX, double initialPositionY, double initialRotation, boolean isHiding, double height, double width, GameObjectShape shape) {
        this.id = id;
        this.type = type;
        this.xPosition = initialPositionX;
        this.yPosition = initialPositionY;
        this.rotation = initialRotation;
        this.height = height;
        this.width = width;
        this.shape = shape;
        this.isHiding = isHiding;
    }

    public Double[] getValues(){
        return new Double[]{xPosition, yPosition, rotation};
    }
    public void hide(){
        //puts object outside of viewer's scope
        this.xPosition = 700;
        this.yPosition = 700;
        this.isHiding = true;
    }
    public abstract void update();
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

    public boolean isHiding() {
        return isHiding;
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

    public void setxPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setHiding(boolean hiding) {
        isHiding = hiding;
    }
}
