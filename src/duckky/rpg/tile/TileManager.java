package duckky.rpg.tile;

import duckky.rpg.gfx.SpriteSheet;
import duckky.rpg.main.GamePanel;
import duckky.rpg.tile.map.Map;

import java.awt.*;

public class TileManager {
    GamePanel gp;
    private final Tile[] tile;
    private final Tile[] bonusTile;


    public Map map;

    SpriteSheet tileset;
    SpriteSheet bonusTileset;

    public final int amountOfTiles;
    public final int amountOfBonusTiles;

    public TileManager(GamePanel gp){
        this.gp = gp;

        tileset = new SpriteSheet("/tiles/tiles.png",gp);
        bonusTileset = new SpriteSheet("/tiles/bonusTiles.png",gp);
        amountOfTiles = tileset.getWidth()*tileset.getHeight();
        amountOfBonusTiles = bonusTileset.getWidth()*bonusTileset.getHeight();

        tile = new Tile[amountOfTiles];
        bonusTile = new Tile[amountOfBonusTiles];
        createTiles();
        map = new Map("map01",gp);
    }

    public SpriteSheet getTileset() {
        return tileset;
    }

    public Tile getTile(int index) {
        if(index<0){
            return bonusTile[index*-1 - 1];
        }
        return tile[index];
    }

    public void loadNewMap(String mapName, int playerX, int playerY){
        gp.player.x = playerX;
        gp.player.y = playerY;
        System.out.println(playerY + " " + gp.player.y);
        map = new Map(mapName,gp);
    }
    public void createTiles(){
        for(int i = 0; i < amountOfTiles; i++) {
            int x = i % tileset.getWidth();
            int y = i / tileset.getWidth();
            tile[i] = new Tile(tileset,x,y,null);
        }
        for(int i = 0; i < amountOfBonusTiles; i++) {
            int x = i % bonusTileset.getWidth();
            int y = i / bonusTileset.getWidth();
            bonusTile[i] = new Tile(bonusTileset,x,y,null);
        }
        tile[116].collisionBox = new Rectangle(0,0,16,16);
    }
    public void renderLayer(Graphics2D g2, int layerNumber, int layerWidth, int layerHeight) {
        for (int i = 0; i < layerHeight; i++) {
            for (int j = 0; j < layerWidth; j++) {
                int tileIndex = map.getTile(j*gp.originalTileSize,i*gp.originalTileSize,layerNumber);
                if(tileIndex < 0 && !gp.editor.isActive()){
                    continue;
                }
                gp.drawImage(getTile(tileIndex).image, j * gp.originalTileSize, i * gp.originalTileSize, g2);
            }
        }
    }
    public void renderBehind(Graphics2D g2) {
        renderLayer(g2, 1, map.width(), map.height());
        renderLayer(g2, 2, map.width(), map.height());
    }
    public void renderInFront(Graphics2D g2) {
        renderLayer(g2, 3, map.width(), map.height());
    }

}
