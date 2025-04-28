package duckky.rpg.editor;

import duckky.rpg.gfx.SpriteSheet;
import duckky.rpg.main.GamePanel;
import duckky.rpg.main.KeyHandler;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Editor {
    GamePanel gp;
    SpriteSheet uiSpriteSheet = new SpriteSheet("/ui/ui.png");
    BufferedImage selectionImage;
    BufferedImage transparentImage;

    BufferedImage saveImage;
    int saveImageX;
    int saveImageY;
    int saveImageSize;
    boolean saveImageClicked = false;

    public boolean isActive = false;
    public int selectedTile = 0;
    public int selectedLayer = 1;

    public int paletteWidth;
    int tilesPerRow = 5;
    int paletteScale = 3;
    int paletteTileSize;

    public int paletteX;
    public final int paletteOffset = 4;

    public int amountMovedX = 0;
    public int amountMovedY = 0;

    KeyHandler keyHandler;

    public Editor(GamePanel gp){
        this.gp = gp;
        setDefaultValues();
    }
    void setDefaultValues(){
        selectionImage = uiSpriteSheet.getSprite(0,0);
        transparentImage = uiSpriteSheet.getSprite(1,0);
        saveImage = uiSpriteSheet.getSprite(2,0);
        paletteTileSize = gp.originalTileSize*paletteScale;
        paletteWidth = paletteTileSize * tilesPerRow + paletteOffset*2;
        paletteX = gp.screenWidth-(paletteWidth);
        keyHandler = gp.keyH;

        saveImageX = 5;
        saveImageY = gp.screenHeight - 32 - 5;
        saveImageSize = 32;
    }
    public void selectTile(){
        if(!keyHandler.leftMousePressed){
            return;
        }

        int mouseX = keyHandler.mouseX;
        int mouseY = keyHandler.mouseY;

        if(isActive && mouseX > paletteX && mouseX < paletteX + paletteWidth) {
            int relX = mouseX - paletteX - paletteOffset;
            int relY = mouseY - paletteOffset;

            if (relX >= 0 && relY >= 0) {
                int col = (relX / paletteTileSize) + amountMovedX;
                int row = (relY / paletteTileSize) + amountMovedY;

                if (col < gp.tileManager.tilesetWidth
                        && row < gp.tileManager.tilesetHeight) {
                    selectedTile = row * gp.tileManager.tilesetWidth + col;
                }
            }
        } else if(isActive){
            editMap();
        }
    }
    public void editMap(){
        int mouseX = keyHandler.mouseX;
        int mouseY = keyHandler.mouseY;

        int worldX = (int) (gp.player.camX + (mouseX - gp.screenWidth  / 2.0) * (1.0 / gp.scale));
        int worldY = (int) (gp.player.camY + (mouseY - gp.screenHeight  / 2.0) * (1.0 / gp.scale));

        if (keyHandler.leftMousePressed){
            gp.tileManager.map.setTile(worldX,worldY,selectedTile,selectedLayer);
        }
    }
    public void saveMap(){
        System.out.println("SAVED!!!");
    }
    public void mouseDown(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();
        if(mouseX>saveImageX
                && mouseX<saveImageX+saveImageSize
                && mouseY>saveImageY
                && mouseY<saveImageY+saveImageSize
                && isActive){
            saveImageClicked = true;
        }
    }
    public void mouseUp(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();
        if(mouseX>saveImageX
                && mouseX<saveImageX+saveImageSize
                && mouseY>saveImageY
                && mouseY<saveImageY+saveImageSize
                && saveImageClicked
                && isActive){
            saveMap();
            saveImageClicked = false;
        }
    }
    public void tick(){
        if(gp.keyH.mouseX>paletteX && isActive && gp.tickCount % 2 == 0){
            if(gp.keyH.upPressed){
                amountMovedY -= 1;
            }
            if(gp.keyH.downPressed){
                amountMovedY += 1;
            }
            if(gp.keyH.rightPressed){
                amountMovedX += 1;
            }
            if(gp.keyH.leftPressed){
                amountMovedX -= 1;
            }
        }
        int maxMoveX = Math.max(0, gp.tileManager.tilesetWidth - tilesPerRow);
        int maxMoveY = Math.max(0, gp.tileManager.tilesetHeight - (gp.screenHeight / paletteTileSize));

        if (amountMovedX < 0) {
            amountMovedX = 0;
        } else if (amountMovedX > maxMoveX) {
            amountMovedX = maxMoveX;
        }

        if (amountMovedY < 0) {
            amountMovedY = 0;
        } else if (amountMovedY > maxMoveY) {
            amountMovedY = maxMoveY;
        }
        selectTile();
    }
    public void render(Graphics2D g2){
        int contentWidth  = paletteWidth  - paletteOffset*2;
        int contentHeight = gp.screenHeight - paletteOffset;

        if(isActive){
            g2.setColor(Color.black);
            g2.fillRect(paletteX,0,paletteWidth,gp.screenHeight);

            g2.drawImage(saveImage,saveImageX,saveImageY,saveImageSize,saveImageSize,null);

            if(selectedLayer >= 1){
                g2.setColor(Color.red);
                g2.fillRect(5,5,20,20);
            }
            if(selectedLayer >= 2){
                g2.setColor(Color.yellow);
                g2.fillRect(30,5,20,20);
            }
            if(selectedLayer >= 3){
                g2.setColor(Color.green);
                g2.fillRect(55,5,20,20);
            }

            for(int i = 0; i < gp.tileManager.amountOfTiles; i++) {
                int x = i % gp.tileManager.tilesetWidth;
                int y = i / gp.tileManager.tilesetHeight;

                x *= paletteTileSize;
                y *= paletteTileSize;

                x -= amountMovedX * paletteTileSize;
                y -= amountMovedY * paletteTileSize;

                if (x + paletteTileSize > 0 && x < contentWidth
                        && y + paletteTileSize > 0 && y < contentHeight) {
                    g2.drawImage(
                            transparentImage,
                            paletteX + paletteOffset + x,
                            paletteOffset + y,
                            paletteTileSize,
                            paletteTileSize,
                            null
                    );
                    g2.drawImage(
                            gp.tileManager.tile[i].image,
                            paletteX + paletteOffset + x,
                            paletteOffset + y,
                            paletteTileSize,
                            paletteTileSize,
                            null
                    );
                    if(i == selectedTile){
                        g2.drawImage(selectionImage,
                                paletteX + paletteOffset + x,
                                paletteOffset + y,
                                paletteTileSize,
                                paletteTileSize,
                                null);
                    }
                }
            }
        }
    }
}
