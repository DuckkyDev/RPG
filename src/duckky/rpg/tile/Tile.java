package duckky.rpg.tile;

import duckky.rpg.gfx.SpriteSheet;

public class Tile {
    SpriteSheet tileset;

    private int x;
    private int y;

    private boolean collision = false;

    public int getX(){return x;}
    public int getY(){return y;}
    public boolean collide(){return collision;}
}
