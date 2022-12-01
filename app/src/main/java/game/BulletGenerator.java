package game;

import game.gameObject.objects.Bullet;
import game.gameObject.objects.Ship;

import java.util.Random;

public class BulletGenerator {
    private static int count = 0;

    public static Bullet generateBullet(Ship ship){
        String id = "bullet-" + ++count;
        Random random = new Random();
        double n = random.nextDouble(2, 5);
        double[] values = getSpecificValues(ship, n);
        return new Bullet(id, ship.getxPosition()+16, ship.getyPosition(), values[0], values[1], values[2], ship.getRotation(), ship.getColor(), ship.getId(), (int) (n*13), ship.getBulletType());
    }
    private static double[] getSpecificValues(Ship ship, double n){
        //first is rotation, second is height, third is width

        return switch (ship.getBulletType()){
            case LIGHTNING -> new double[]{ship.getRotation()-20, n*12, n*4};
            case PLASMA -> new double[]{ship.getRotation(), n*7, n*7};
            case LASER -> new double[]{ship.getRotation(), n*5, n*2};
        };
    }
}
