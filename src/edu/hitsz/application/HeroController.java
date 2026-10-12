package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class HeroController {
    private GameTemplate game; // 修改为 GameTemplate
    private HeroAircraft heroAircraft;
    private MouseAdapter mouseAdapter;
    public HeroController(GameTemplate game, HeroAircraft heroAircraft){ // 修改为 GameTemplate [cite: 450]
        this.game = game;
        this.heroAircraft = heroAircraft;
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getX();
                int y = e.getY();
                if ( x<0 || x>Main.WINDOW_WIDTH || y<0 || y>Main.WINDOW_HEIGHT){
                    // 防止超出边界
                    return;
                }
                heroAircraft.setLocation(x, y);
            }
        };

        game.addMouseListener(mouseAdapter);
        game.addMouseMotionListener(mouseAdapter);
    }


}