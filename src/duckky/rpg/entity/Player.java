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

    public int camX;
    public int camY;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValue();
    }
    public void setDefaultValue(){
        x = 32;
        y = 32;
        speed = 96; //Pixels per second
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

        isMoving = false;
        double moveX = 0;
        double moveY = 0;
        if(keyH.upPressed){
            moveY -= 1;
            direction = Direction.UP;
            isMoving = true;
        }
        if(keyH.downPressed){
            moveY += 1;
            direction = Direction.DOWN;
            isMoving = true;
        }
        if(keyH.rightPressed){
            moveX += 1;
            direction = Direction.RIGHT;
            isMoving = true;
        }
        if(keyH.leftPressed){
            moveX -= 1;
            direction = Direction.LEFT;
            isMoving = true;
        }

        if(moveX != 0 && moveY != 0){
            double diagonalFactor = 1 / Math.sqrt(moveX * moveX + moveY * moveY);
            moveX *= diagonalFactor;
            moveY *= diagonalFactor;
        }

        x += (int) (moveX * distance);
        y += (int) (moveY * distance);

        int animationInterval = gp.FPS / 8; // Calculate how many frames should pass per sprite change

        spriteCounter++;
        if (spriteCounter >= animationInterval) {
            if(spriteNumber<8){
                spriteNumber++;
            } else {
                spriteNumber = 1;
            }
            spriteCounter = 0;
        }
        if(!isMoving){
            spriteCounter = 0;
            spriteNumber = 1;
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
