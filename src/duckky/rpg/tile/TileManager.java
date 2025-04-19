package duckky.rpg.tile;

import duckky.rpg.gfx.SpriteSheet;
import duckky.rpg.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class TileManager {
    GamePanel gp;
    Tile[] tile;
    int[][] map;
    final int amountOfTiles = 15;
    SpriteSheet tileset = new SpriteSheet("/tiles/tiles.png");

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[amountOfTiles];
        createTiles();

    }
    public void createTiles(){
        tile[0] = new Tile(tileset,0,0,false); //Grass
        tile[1] = new Tile(tileset,1,0,false); //Dirt
    }
    public void loadMap(Graphics2D g2){


//        map = new int[][]{
//                {0,0,1,1,0,0,0,0,0,0},
//                {0,0,1,1,0,0,0,0,0,0},
//                {0,0,1,1,0,0,0,0,0,0},
//                {0,0,1,1,0,0,0,0,0,0},
//                {0,0,1,1,0,0,0,0,0,0},
//                {0,0,1,1,0,0,0,0,0,0},
//                {0,0,1,1,0,0,0,0,0,0}
//        };
//        int currentTile;
//        for (int i = 0; i < 7; i++) {
//            for (int j = 0; j < 10; j++) {
//                currentTile = map[i][j];
//                gp.drawImage(tile[currentTile].image,j,i,g2);
//            }
//        }
    }
}
