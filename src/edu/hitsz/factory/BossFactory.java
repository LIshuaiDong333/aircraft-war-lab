package edu.hitsz.factory;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.application.Main;

public class BossFactory implements EnemyFactory {
    @Override
    public EnemyAircraft createEnemyAircraft() {
        int locationX = Main.WINDOW_WIDTH / 2;
        int locationY = 100;
        int speedX = 2;
        int speedY = 0;
        int hp = 1000;
        return new BossEnemy(locationX, locationY, speedX, speedY, hp);
    }
}