package duckky.rpg.tile;

import duckky.rpg.gfx.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image;
    public Rectangle collisionBox;

    public Tile(SpriteSheet tileset,int x, int y, Rectangle collisionBox){
        image = tileset.getSprite(x,y);
        this.collisionBox = collisionBox;
    }
}
