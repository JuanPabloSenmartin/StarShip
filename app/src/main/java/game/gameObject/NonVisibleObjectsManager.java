package game.gameObject;

public class NonVisibleObjectsManager {
    static double x = 1000;
    static double y = 0;
    public static double[] getEmptyPlace(){
        if (y==1000){
            x += 50;
            y = 0;
        }
        double[] emptySpace = {x,y};
        y += 50;
        return emptySpace;
    }
}
