package duckky.rpg.gfx;

import java.awt.image.BufferedImage;

public class SpriteSheet {
    private final String path;

    public SpriteSheet(String sheetPath){
        this.path = sheetPath;
    }
    public BufferedImage getSprite(int x, int y, int width, int height){
        BufferedImage image = ImageLoader.loadImage(path);
        return image.getSubimage(x*width,y*height,width,height);
    }
    public BufferedImage getSprite(int x, int y){
        BufferedImage image = ImageLoader.loadImage(path);
        return image.getSubimage(x*16,y*16,16,16);
    }
}
