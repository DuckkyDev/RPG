package duckky.rpg.entity;

import duckky.rpg.gfx.SpriteSheet;
import duckky.rpg.main.GamePanel;
import duckky.rpg.main.KeyHandler;
import duckky.rpg.tile.map.LoadingZone;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;
    SpriteSheet spriteSheet;

    public int camX;
    public int camY;

    boolean isMoving;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        hitbox = new Rectangle(4,10,7,5);

        setDefaultValue();
    }
    public void setDefaultValue(){
        x = 32;
        y = 32;
        speed = 96; //Pixels per second
        direction = Direction.DOWN;
        spriteSheet = new SpriteSheet("/player/player_sprite_sheet.png",gp);

        moveCamera();
    }
    public void moveCamera(){
        float desiredCamX = x;
        float desiredCamY = y;
        if (gp.editor.isActive()) {
            camX = (int) (desiredCamX + ((gp.editor.paletteWidth / gp.scale) / 2f));
            camY = (int) desiredCamY;
            return;
        }

        // 2) Dimensions in world‐pixels
        int mapPixelW = gp.tileManager.map.width()  * gp.originalTileSize;
        int mapPixelH = gp.tileManager.map.height() * gp.originalTileSize;
        float viewW   = (float) (gp.screenWidth  / gp.scale);
        float viewH   = (float) (gp.screenHeight / gp.scale);
        float halfW   = viewW  / 2f;
        float halfH   = viewH  / 2f;

        // Calculate min and max camera positions with inset
        // The camera's center should be at least (inset + halfW) from world origin
        float minCamXWithInset = gp.originalTileSize + halfW;
        float maxCamXWithInset = mapPixelW - gp.originalTileSize - halfW;
        float minCamYWithInset = gp.originalTileSize + halfH;
        float maxCamYWithInset = mapPixelH - gp.originalTileSize - halfH;

        // Clamp desiredCamX
        if (mapPixelW < viewW) { // If map is narrower than the view, just center it
            // This case handles maps smaller than the screen width.
            // We want the entire map to be visible, so the camera should center the map.
            desiredCamX = mapPixelW / 2.0f;
        } else { // Otherwise, apply regular clamping with inset
            desiredCamX = Math.max(desiredCamX, minCamXWithInset);
            desiredCamX = Math.min(desiredCamX, maxCamXWithInset);
        }

        // Clamp desiredCamY
        if (mapPixelH < viewH) { // If map is shorter than the view, just center it
            // This case handles maps smaller than the screen height.
            // We want the entire map to be visible, so the camera should center the map.
            desiredCamY = mapPixelH / 2.0f;
        } else { // Otherwise, apply regular clamping with inset
            desiredCamY = Math.max(desiredCamY, minCamYWithInset);
            desiredCamY = Math.min(desiredCamY, maxCamYWithInset);
        }

        camX = (int) desiredCamX;
        camY = (int) desiredCamY;
    }
    public void tryMove(){
        double secondsPerUpdate = 1.0 / gp.targetFPS;
        double distance = speed * secondsPerUpdate;

        double moveX = 0;
        double moveY = 0;
        if(gp.editor.isActive()&&gp.keyH.mouseX>gp.editor.paletteX){return;}
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

        if(moveX != 0 || moveY != 0) {
            double diagonalFactor = 1 / Math.sqrt(moveX * moveX + moveY * moveY);
            moveX *= diagonalFactor * distance;
            moveY *= diagonalFactor * distance;

            // Move X
            int nextX = x + (int)(moveX);
            if(canWalk(nextX, y)) {
                x = nextX;
            } else {
                snapBackX(moveX > 0);
            }

            // Move Y
            int nextY = y + (int)(moveY);
            if(canWalk(x, nextY)) {
                y = nextY;
            } else {
                snapBackY(moveY > 0);
            }
        }
    }
    private void snapBackX(boolean movingRight) {
        // step one pixel at a time until you're just outside the wall
        int sign = movingRight ? 1 : -1;
        // as soon as canWalk(x+sign, y) is true, we know x is right against the wall
        while(canWalk(x + sign, y)) {
            x += sign;
        }
    }
    private void snapBackY(boolean movingDown) {
        int sign = movingDown ? 1 : -1;
        while(canWalk(x, y + sign)) {
            y += sign;
        }
    }
    public boolean canWalk(int nextX, int nextY) {
        int tileSize = gp.originalTileSize;

        int spriteTopLeftX = nextX - (tileSize / 2);
        int spriteTopLeftY = nextY - (tileSize / 2);

        int leftX = spriteTopLeftX + hitbox.x;
        int topY = spriteTopLeftY + hitbox.y;
        int rightX = leftX + hitbox.width;
        int bottomY = topY + hitbox.height;

        int leftCol = leftX / tileSize;
        int rightCol = (rightX - 1) / tileSize;
        int topRow = topY / tileSize;
        int bottomRow = (bottomY - 1) / tileSize;

        Rectangle playerBox = new Rectangle(leftX, topY, hitbox.width, hitbox.height);

        for(LoadingZone loadingZone : gp.tileManager.map.getLoadingZones()){
            if(playerBox.intersects(loadingZone.area)){
                if(gp.editor.isActive()){
                    return true;
                }
                gp.editor.saveMap(false);
                gp.tileManager.loadNewMap(
                        loadingZone.targetMap,
                        loadingZone.spawnX,
                        loadingZone.spawnY
                );
            }
        }
        for (int row = topRow; row <= bottomRow; row++) {
            for (int col = leftCol; col <= rightCol; col++) {
                int tileNum = gp.tileManager.map.getTile(col * tileSize, row * tileSize, 2);

                Rectangle tileBox = gp.tileManager.getTile(tileNum).collisionBox;

                if (tileBox != null) {
                    int tileWorldX = col * tileSize;
                    int tileWorldY = row * tileSize;

                    Rectangle worldTileBox = new Rectangle(
                            tileWorldX + tileBox.x,
                            tileWorldY + tileBox.y,
                            tileBox.width,
                            tileBox.height
                    );

                    if (playerBox.intersects(worldTileBox)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public void tick(){
        moveCamera();

        isMoving = false;

        tryMove();

        int animationInterval = gp.targetFPS / 8; // Calculate how many frames should pass per sprite change

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
        gp.drawImage(image,x - 8,y - 8,g2);
        //drawHitbox(g2);
    }
    public void drawHitbox(Graphics2D g2) {
        gp.drawRect(x-8+hitbox.x,y-8+hitbox.y,hitbox.width,hitbox.height,Color.red,g2);
    }
}
