import game.gameObject.objects.Bullet;
import game.gameObject.objects.Meteor;
import game.gameObject.objects.Ship;
import game.gameObject.objects.enums.BulletType;
import game.gameObject.objects.enums.Color;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
public class MovementTests {
    static Meteor meteor;
    static Bullet bullet;
    static Ship ship;

    @BeforeClass
    public static void setupBeforeClass() {
        ship = new Ship("starship-1", 400, 400, 180, 30, 30, "player-1", Color.RED, System.currentTimeMillis(), 180, 50, BulletType.LASER);
        meteor = new Meteor("meteor-1", 300, 300, 180, 30, 30, 180, true, 100, 100);
        bullet = new Bullet("bullet-1", 200, 200, 180, 30, 30, 180, Color.RED, "starship-1", 100, BulletType.LASER);
    }

    @Test
    public void testMeteorMovement(){
        Meteor updated = meteor.update();
        assertEquals(updated.getxPosition(), meteor.getxPosition() + 0.7 * Math.sin(Math.PI * 2 * meteor.getDirection() / 360), 0);
        assertEquals(updated.getyPosition(), meteor.getyPosition() + 0.7 * Math.cos(Math.PI * 2 * meteor.getDirection() / 360), 0);
    }
    @Test
    public void testBulletMovement(){
        Bullet updated = bullet.update();
        assertEquals(updated.getxPosition(), bullet.getxPosition() + 4 * Math.sin(Math.PI * 2 * bullet.getDirection() / 360), 0);
        assertEquals(updated.getyPosition(), bullet.getyPosition() + 4 * Math.cos(Math.PI * 2 * bullet.getDirection() / 360), 0);
    }
    @Test
    public void testShipMovement(){
        Ship updated = ship.update();
        assertEquals(updated.getxPosition(), ship.getxPosition() + 3.5 * Math.sin(Math.PI * 2 * ship.getDirection() / 360), 0);
        assertEquals(updated.getyPosition(), ship.getyPosition() + 3.5 * Math.cos(Math.PI * 2 * ship.getDirection() / 360), 0);
    }
    @Test
    public void testShipBoost(){
        Ship updated = ship.move(true);
        assertEquals(updated.getBoost(), ship.getBoost()+70, 0);
        updated = ship.move(false);
        assertTrue(updated.getBoost() < 0);
    }
}

