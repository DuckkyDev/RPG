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

    public int[][] map1;
    public int[][] map2;
    public int[][] map3;

    public int tilesetWidth = 8;
    public int tilesetHeight = 8;
    public final int amountOfTiles = tilesetWidth*tilesetHeight;

    SpriteSheet tileset = new SpriteSheet("/tiles/tiles.png");

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[amountOfTiles];
        createTiles();

    }
    public void createTiles(){
        for(int i = 0; i < amountOfTiles; i++) {
            int x = i % tilesetWidth;
            int y = i / tilesetWidth;
            tile[i] = new Tile(tileset,x,y,null);
        }
        tile[3].collisionBox = new Rectangle(0,0,16,16);
    }
    public int getTileIndexAtWorldPosition(int x, int y, int layerNumber){
        int[][] map = new int[gp.maxScreenRow][gp.maxScreenColumn];
        int column = x / gp.originalTileSize;
        int row    = y / gp.originalTileSize;

        if (row < 0 || row >= map.length || column < 0 || column >= map[0].length) {
            return 0;
        }

        map = switch (layerNumber) {
            case 1 -> map1;
            case 2 -> map2;
            case 3 -> map3;
            default -> map;
        };

        return map[row][column];
    }
    public void setTile(int col,int row,int layerNumber,int newTileIndex){
        switch(layerNumber){
            case 1:
                map1[row][col] = newTileIndex;
                break;
            case 2:
                map2[row][col] = newTileIndex;
                break;
            case 3:
                map3[row][col] = newTileIndex;
                break;
        }
    }
    public void loadMap(String mapName) {
        int[][] map;
        try {
            for (int a = 0; a < 3; a++) {
                InputStream inputStream = getClass().getResourceAsStream("/maps/"+mapName+"/layer"+(a+1)+".txt");
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
                    int numCols = lines.getFirst().split(" ").length;

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

                    switch(a){
                        case 0:
                            map1 = map;
                            break;
                        case 1:
                            map2 = map;
                            break;
                        case 2:
                            map3 = map;
                            break;
                    }
                } else {
                    System.err.println("Warning: Map file is empty.");
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading map: " + e.getMessage());
        }
    }
    public void renderLayer(Graphics2D g2, int[][] map, int layerWidth, int layerHeight) {
        for (int i = 0; i < layerHeight; i++) {
            for (int j = 0; j < layerWidth; j++) {
                gp.drawImage(tile[map[i][j]].image, j * gp.originalTileSize, i * gp.originalTileSize, g2);
            }
        }
    }
    public void renderBehind(Graphics2D g2) {
        renderLayer(g2, map1, map1[0].length, map1.length);
        renderLayer(g2, map2, map2[0].length, map2.length);
    }
    public void renderInFront(Graphics2D g2) {
        renderLayer(g2, map3, map3[0].length, map3.length);
    }

}
