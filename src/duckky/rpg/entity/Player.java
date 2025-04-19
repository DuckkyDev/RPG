package duckky.rpg.entity;

import duckky.rpg.gfx.SpriteSheet;
import duckky.rpg.main.GamePanel;
import duckky.rpg.main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;
    SpriteSheet spriteSheet = new SpriteSheet("/player/player_sprite_sheet.png");

    public double camX;
    public double camY;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValue();
    }
    public void setDefaultValue(){
        x = 2;
        y = 2;
        speed = 6; //Tiles per second
        direction = Direction.DOWN;

        camX = x;
        camY = y;
    }
    public void moveCamera(){
        camX = x;
        camY = y;
    }
    public void tick(){
        boolean isMoving;
        double secondsPerUpdate = 1.0 / gp.FPS;

        // Calculate the distance to move in this single update step
        double distance = speed * secondsPerUpdate;
        moveCamera();
        if (keyH.upPressed) {
            direction = Direction.UP;
            y -= distance;
            isMoving = true;
        } else if (keyH.downPressed) {
            direction = Direction.DOWN;
            y += distance;
            isMoving = true;
        } else if (keyH.leftPressed) {
            direction = Direction.LEFT;
            x -= distance;
            isMoving = true;
        } else if (keyH.rightPressed) {
            direction = Direction.RIGHT;
            x += distance;
            isMoving = true;
        } else {
            isMoving = false;
        }

        int animationInterval = gp.FPS / 8; // Calculate how many frames should pass per sprite change

        spriteCounter++;
        if (spriteCounter >= animationInterval && isMoving) {
            if(spriteNumber<8){
                spriteNumber++;
            } else {
                spriteNumber = 1;
            }
            spriteCounter = 0;
        }
    }
    public void render(Graphics2D g2){
        BufferedImage image = null;

        switch (direction){
            case UP -> image = spriteSheet.getSprite(spriteNumber-1,3);
            case DOWN -> image = spriteSheet.getSprite(spriteNumber-1,2);
            case LEFT -> image = spriteSheet.getSprite(spriteNumber-1,1);
            case RIGHT -> image = spriteSheet.getSprite(spriteNumber-1,0);
        }
        gp.drawImage(image,x,y,g2);
    }
}
