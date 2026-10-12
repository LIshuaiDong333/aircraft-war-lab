package edu.hitsz.factory;

import edu.hitsz.aircraft.SuperEliteEnemy;
import edu.hitsz.aircraft.EnemyAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class SuperEliteFactory implements EnemyFactory {
    @Override
    public EnemyAircraft createEnemyAircraft() {
        int locationX = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.SUPER_ELITE_ENEMY_IMAGE.getWidth()));
        int locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        int speedX = 3;
        int speedY = 5;
        int hp = 100;
        return new SuperEliteEnemy(locationX, locationY, speedX, speedY, hp);
    }
}