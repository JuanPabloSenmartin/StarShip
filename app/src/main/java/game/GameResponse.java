package game;

import java.util.Map;

public class GameResponse {
    private final Map<String, Double> xPositions;
    private final Map<String, Double> yPositions;
    private final Map<String, Double> rotationDegrees;


    public GameResponse(Map<String, Double> xPositions, Map<String, Double> yPositions, Map<String, Double> rotationDegrees) {
        this.xPositions = xPositions;
        this.yPositions = yPositions;
        this.rotationDegrees = rotationDegrees;
    }

    public Map<String, Double> getxPositions() {
        return xPositions;
    }

    public Map<String, Double> getyPositions() {
        return yPositions;
    }

    public Map<String, Double> getRotationDegrees() {
        return rotationDegrees;
    }
}
