package duckky.rpg.tile;

import duckky.rpg.gfx.SpriteSheet;
import duckky.rpg.main.GamePanel;
import duckky.rpg.tile.map.Map;

import java.awt.*;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;

    public Map map;

    public int tilesetWidth = 8;
    public int tilesetHeight = 8;
    public final int amountOfTiles = tilesetWidth*tilesetHeight;

    SpriteSheet tileset = new SpriteSheet("/tiles/tiles.png");

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[amountOfTiles];
        createTiles();
        map = new Map("map01",gp);
    }
    public void createTiles(){
        for(int i = 0; i < amountOfTiles; i++) {
            int x = i % tilesetWidth;
            int y = i / tilesetWidth;
            tile[i] = new Tile(tileset,x,y,null);
        }
        tile[3].collisionBox = new Rectangle(0,0,16,16);
    }
    public void renderLayer(Graphics2D g2, int layerNumber, int layerWidth, int layerHeight) {
        for (int i = 0; i < layerHeight; i++) {
            for (int j = 0; j < layerWidth; j++) {
                int tileIndex = map.getTile(j*gp.originalTileSize,i*gp.originalTileSize,layerNumber);
                gp.drawImage(tile[tileIndex].image, j * gp.originalTileSize, i * gp.originalTileSize, g2);
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
