package duckky.rpg.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    BufferedImage image;

    public SpriteSheet(String sheetPath){
        this.image = ImageLoader.loadImage(sheetPath);
    }
    public BufferedImage getSprite(int x, int y, int width, int height){
        return image.getSubimage(x*width,y*height,width,height);
    }
    public BufferedImage getSprite(int x, int y){
        return image.getSubimage(x*16,y*16,16,16);
    }
}
