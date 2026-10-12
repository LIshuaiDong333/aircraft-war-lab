package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class HeroAircraftTest {

    private HeroAircraft hero;

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");
        hero = HeroAircraft.getInstance();
    }

    @AfterEach
    void tearDown() {
        System.out.println("**--- Executed after each test method in this class ---**");
    }

    @Test
    public void testGetInstance() {
        System.out.println("**--- Test GetInstance method executed ---**");
        HeroAircraft instance1 = HeroAircraft.getInstance();
        HeroAircraft instance2 = HeroAircraft.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testIncreaseHp() {
        System.out.println("**--- Test IncreaseHp method executed ---**");
        int maxHp = 100000;

        //测试增加后不超过最大值
        hero.increaseHp(500);
        assertEquals(maxHp, hero.getHp());

        hero.decreaseHp(2000);
        hero.increaseHp(1500);
        assertEquals(99500, hero.getHp());
    }

    @Test
    public void testShoot() {
        System.out.println("**--- Test Shoot method executed ---**");
        hero.setLocation(256, 700);

        List<BaseBullet> bullets = hero.shoot();

        assertEquals(1, bullets.size());

        BaseBullet bullet = bullets.get(0);

        assertTrue(bullet instanceof HeroBullet);

        assertEquals(256, bullet.getLocationX());
        assertEquals(698, bullet.getLocationY()); //700 + (-1)*2 = 698

        assertEquals(-5, bullet.getSpeedY());
    }
}
