package duckky.rpg.tile.map;

import duckky.rpg.main.GamePanel;

import java.awt.*;

public class LoadingZone {
    GamePanel gp;

    public final int x,y,spawnX,spawnY;
    public final String targetMap;
    public Rectangle area;

    LoadingZone(GamePanel gp, String targetMap, int x, int y, int spawnX, int spawnY ){
        this.gp = gp;
        this.x=x;
        this.y=y;
        this.spawnX=spawnX;
        this.spawnY=spawnY;
        this.targetMap = targetMap;
        this.area = new Rectangle(
                x * gp.originalTileSize,
                y * gp.originalTileSize,
                gp.originalTileSize,
                gp.originalTileSize
        );
    }
}
