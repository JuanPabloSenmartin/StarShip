package game.gameObject;

public abstract class GameObject {
    private final String id;
    private final GameObjectType type;
    private double xPosition;
    private double yPosition;
    private double rotation;
    private double direction;
    private boolean isHiding;
    private double height;
    private double width;
    private final GameObjectShape shape;
    private final Color color;

    public GameObject(String id, GameObjectType type, double initialPositionX, double initialPositionY, double initialRotation, boolean isHiding, double height, double width, GameObjectShape shape, double direction, Color color) {
        this.id = id;
        this.type = type;
        this.xPosition = initialPositionX;
        this.yPosition = initialPositionY;
        this.rotation = initialRotation;
        this.height = height;
        this.width = width;
        this.shape = shape;
        this.isHiding = isHiding;
        this.direction = direction;
        this.color = color;
    }

    public Double[] getValues(){
        return new Double[]{xPosition, yPosition, rotation, height, width};
    }
    public void hide(){
        //puts object outside of viewer's scope
        double[] pos = NonVisibleObjectsManager.getEmptyPlace();
        this.xPosition = pos[0];
        this.yPosition = pos[1];
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

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }
    public boolean isInsideLimit(){
        return xPosition > 0 && xPosition < 800 && yPosition > 0 && yPosition < 800;
    }
    public boolean isInsideLimit(double xPosition, double yPosition){
        return xPosition > 0 && xPosition < 725 && yPosition > 0 && yPosition < 700;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public Color getColor() {
        return color;
    }
}
