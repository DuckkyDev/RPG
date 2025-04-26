package duckky.rpg.tile;

import duckky.rpg.gfx.SpriteSheet;
import duckky.rpg.main.GamePanel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] map;
    final int amountOfTiles = 15;
    SpriteSheet tileset = new SpriteSheet("/tiles/tiles.png");

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[amountOfTiles];
        map = new int[gp.maxScreenRow][gp.maxScreenColumn];
        createTiles();

    }
    public void createTiles(){
        tile[0] = new Tile(tileset,7,7,false); //Empty
        tile[1] = new Tile(tileset,0,0,false); //Grass
        tile[2] = new Tile(tileset,1,0,false); //Dirt
        tile[3] = new Tile(tileset,3,0,true); //Wall TEMPORARY
    }
    public void loadMap(String path) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(path);
            assert inputStream != null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            List<String> lines = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();

            if (!lines.isEmpty()) {
                int numRows = lines.size();
                int numCols = lines.getFirst().split(" ").length; // Assuming all rows have the same number of columns

                map = new int[numRows][numCols];

                for (int i = 0; i < numRows; i++) {
                    String[] numbers = lines.get(i).split(" ");
                    if (numbers.length != numCols) {
                        System.err.println("Warning: Row " + (i + 1) + " has a different number of columns.");
                        continue;
                    }
                    for (int j = 0; j < numCols; j++) {
                        map[i][j] = Integer.parseInt(numbers[j]);
                    }
                }

                System.out.println("Map loaded with dimensions: " + numRows + " rows x " + numCols + " columns.");

            } else {
                System.err.println("Warning: Map file is empty.");
            }

        } catch (Exception e) {
            System.err.println("Error loading map: " + e.getMessage());
        }
    }
    public void render(Graphics2D g2){
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                gp.drawImage(tile[map[i][j]].image,j*gp.originalTileSize,i*gp.originalTileSize,g2);
            }
        }
    }
}
