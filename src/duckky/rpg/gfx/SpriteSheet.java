package duckky.rpg.gfx;

import duckky.rpg.main.GamePanel;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    BufferedImage image;
    GamePanel gp;
    public SpriteSheet(String sheetPath, GamePanel gp){
        this.image = ImageLoader.loadImage(sheetPath);
        this.gp = gp;
    }
    public int getWidth(){
        return image.getWidth()/gp.originalTileSize;
    }
    public int getHeight(){
        return image.getHeight()/gp.originalTileSize;
    }
    public BufferedImage getSprite(int x, int y){
        return image.getSubimage(x*16,y*16,16,16);
    }
}
