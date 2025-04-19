package duckky.rpg.tile;

import duckky.rpg.gfx.SpriteSheet;

import java.awt.image.BufferedImage;

public class Tile {
    SpriteSheet tileset;
    BufferedImage image;

    int x;
    int y;
    public boolean collision;

    public Tile(SpriteSheet tileset,int x, int y, boolean collision){
        image = tileset.getSprite(x,y);
        this.collision = collision;
    }
}
