package duckky.rpg.editor;

import duckky.rpg.gfx.SpriteSheet;
import duckky.rpg.main.GamePanel;
import duckky.rpg.main.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Editor {
    GamePanel gp;
    SpriteSheet uiSpriteSheet;
    BufferedImage selectionImage;
    BufferedImage transparentImage;

    BufferedImage saveImage;
    int saveImageX;
    int saveImageY;
    int saveImageSize;
    boolean saveImageClicked = false;
    boolean isLoadingZoneTileClicked = false;

    boolean runningFromJar = Editor.class
            .getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .getPath()
            .endsWith(".jar");

    private boolean active = false;
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
        uiSpriteSheet = new SpriteSheet("/ui/ui.png",gp);
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
    public boolean isActive(){
        return active;
    }
    public void setActive(boolean newValue){
        if(!runningFromJar){
            active = newValue;
        }
    }
    public void selectTile(){
        int mouseX = keyHandler.mouseX;
        int mouseY = keyHandler.mouseY;

        int worldX = (int) (gp.player.camX + (mouseX - gp.screenWidth  / 2.0) * (1.0 / gp.scale));
        int worldY = (int) (gp.player.camY + (mouseY - gp.screenHeight  / 2.0) * (1.0 / gp.scale));

        if(active && mouseX > paletteX && mouseX < paletteX + paletteWidth && keyHandler.leftMousePressed) {
            int relX = mouseX - paletteX - paletteOffset;
            int relY = mouseY - paletteOffset;

            if (relX >= 0 && relY >= 0) {
                int col = (relX / paletteTileSize) + amountMovedX;
                int row = (relY / paletteTileSize) + amountMovedY;

                if (col < gp.tileManager.getTileset().getWidth()
                        && row < gp.tileManager.getTileset().getHeight()) {
                    selectedTile = row * gp.tileManager.getTileset().getWidth() + col;
                }
            }
        } else if(active){
            if (keyHandler.ePressed){
                selectedTile = gp.tileManager.map.getTile(worldX,worldY,selectedLayer);
            }
            editMap();
        }
    }
    public void editMap(){
        int mouseX = keyHandler.mouseX;
        int mouseY = keyHandler.mouseY;

        if(mouseX>saveImageX
                && mouseX<saveImageX+saveImageSize
                && mouseY>saveImageY
                && mouseY<saveImageY+saveImageSize
                && active){return;}

        int worldX = (int) (gp.player.camX + (mouseX - gp.screenWidth  / 2.0) * (1.0 / gp.scale));
        int worldY = (int) (gp.player.camY + (mouseY - gp.screenHeight  / 2.0) * (1.0 / gp.scale));

        if(keyHandler.leftMousePressed){
            gp.tileManager.map.setTile(worldX,worldY,selectedTile,selectedLayer);
        }
        if (keyHandler.rightMousePressed && selectedLayer == 2 && !isLoadingZoneTileClicked) {
            if (gp.tileManager.map.getTile(worldX, worldY, 2) != -1) {
                return;
            }
            try{
                String targetMapName = JOptionPane.showInputDialog(null, "Enter the name of the targeted map:");
                if (targetMapName != null && !targetMapName.isBlank()) {
                    System.out.println("Zone set to: " + targetMapName);
                } else {
                    System.out.println("User cancelled or entered nothing.");
                    return;
                }
                String targetPosition = JOptionPane.showInputDialog(gp, "Enter X and Y coordinates (e.g. 10, 20):");
                if (targetPosition != null && !targetPosition.isBlank()) {
                    int targetX;
                    int targetY;
                    try {
                        String[] parts = targetPosition.trim().split("[,\\s]+"); // split by comma or whitespace
                        targetX = Integer.parseInt(parts[0]);
                        targetY = Integer.parseInt(parts[1]);

                        System.out.println("X: " + targetX + ", Y: " + targetY);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(gp, "Invalid input! Please enter two numbers.");
                        return;
                    }
                    int result = JOptionPane.showConfirmDialog(gp,
                            "Is this correct:\n" +
                                    "Targeted Map: " + targetMapName + "\n" +
                                    "Target Coordinates: (" + targetX + ", " + targetY + ")",
                            "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (result != JOptionPane.YES_OPTION) {
                        return;
                    }
                    gp.tileManager.map.addLoadingZone(targetMapName,worldX/gp.originalTileSize,worldY/gp.originalTileSize,targetX,targetY);
                } else {
                    System.out.println("User cancelled or entered nothing.");
                }
            } finally {
                isLoadingZoneTileClicked = false;
                keyHandler.rightMousePressed = false;
            }
        }
    }
    public void saveMap(boolean showConfirmation){
        if(runningFromJar){return;}
        if(showConfirmation){
            int result = JOptionPane.showConfirmDialog(
                    gp,
                    "Are you sure you want to save?",
                    "Confirm Save",
                    JOptionPane.YES_NO_OPTION
            );
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }
        gp.tileManager.map.saveMap();
        gp.tileManager.map.saveLoadingZones();
    }
    public void saveMap(){
        saveMap(true);
    }
    public void mouseDown(MouseEvent e){
        int mouseX = e.getX();
        int mouseY = e.getY();
        if(mouseX>saveImageX
                && mouseX<saveImageX+saveImageSize
                && mouseY>saveImageY
                && mouseY<saveImageY+saveImageSize
                && active){
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
                && active){
            saveMap();
            saveImageClicked = false;
        }
    }
    public void tick(){
        if(gp.keyH.mouseX>paletteX && active && gp.tickCount % 2 == 0){
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
        int maxMoveX = Math.max(0, gp.tileManager.getTileset().getWidth() - tilesPerRow);
        int maxMoveY = Math.max(0, gp.tileManager.getTileset().getHeight() - (gp.screenHeight / paletteTileSize));

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

        if(active){
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
                int x = i % gp.tileManager.getTileset().getWidth();
                int y = i / gp.tileManager.getTileset().getHeight();

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
                            gp.tileManager.getTile(i).image,
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
