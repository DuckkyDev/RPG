package duckky.rpg.tile;

import duckky.rpg.gfx.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    SpriteSheet tileset;
    BufferedImage image;

    int x;
    int y;
    public Rectangle collisionBox;

    public Tile(SpriteSheet tileset,int x, int y, Rectangle collisionBox){
        image = tileset.getSprite(x,y);
        this.collisionBox = collisionBox;
    }
}
