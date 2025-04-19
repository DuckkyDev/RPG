package duckky.rpg.tile;

import duckky.rpg.gfx.SpriteSheet;
import duckky.rpg.main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    Tile[] tile;
    int[][] map;
    final int amountOfTiles = 15;
    SpriteSheet tileset = new SpriteSheet("/tiles/tiles.png");

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[amountOfTiles];
        map = new int[gp.maxScreenRow][gp.maxScreenColumn];
        createTiles();

    }
    public void createTiles(){
        tile[0] = new Tile(tileset,0,0,false); //Grass
        tile[1] = new Tile(tileset,1,0,false); //Dirt
        tile[2] = new Tile(tileset,3,0,false); //Wall TEMPORARY
    }
    public void loadMap(String path){
        try{
            InputStream inputStream = getClass().getResourceAsStream(path);
            assert inputStream != null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            int currentTile;
            for (int i = 0; i < gp.maxScreenRow; i++) {
                String line = bufferedReader.readLine();
                String[] numbers = line.split(" ");
                for (int j = 0; j < gp.maxScreenColumn; j++) {
                    int num = Integer.parseInt(numbers[j]);
                    map[i][j] = num;
                }
            }
            bufferedReader.close();
        } catch (Exception _){
        }
    }
    public void render(Graphics2D g2){
        for (int i = 0; i < gp.maxScreenRow; i++) {
            for (int j = 0; j < gp.maxScreenColumn; j++) {
                gp.drawImage(tile[map[i][j]].image,j,i,g2);
            }
        }
    }
}
