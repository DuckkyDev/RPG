package duckky.rpg.tile.map;

import duckky.rpg.main.GamePanel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;



public class Map {
    String mapName;
    GamePanel gp;
    private Layer layer1;
    private Layer layer2;
    private Layer layer3;

    public Map(String mapName, GamePanel gp) {
        this.mapName = mapName;
        this.gp = gp;
        loadMap();
    }
    public void setTile(int x, int y, int tile, int layerNumber){
        Layer layer = switch (layerNumber) {
            case 1 -> layer1;
            case 2 -> layer2;
            case 3 -> layer3;
            default -> {
                System.err.println("Invalid layer number: " + layerNumber);
                yield null;
            }
        };
        if(layer != null){
            layer.setTile(x, y, tile);
        }
    }
    public int getTile(int x, int y, int layerNumber) {
        Layer layer = switch (layerNumber) {
            case 1 -> layer1;
            case 2 -> layer2;
            case 3 -> layer3;
            default -> {
                System.err.println("Invalid layer number: " + layerNumber);
                yield null;
            }
        };
        if (layer == null) {
            return 0;
        }
        return layer.getTile(x, y);
    }
    private Layer getLayer(int LayerNum){
        return switch (LayerNum) {
            case 1 -> layer1;
            case 2 -> layer2;
            default -> layer3;
        };
    }
    public void loadMap() {
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
                            layer1 = new Layer(map,gp);
                            break;
                        case 1:
                            layer2 = new Layer(map,gp);
                            break;
                        case 2:
                            layer3 = new Layer(map,gp);
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
    public void saveMap(){
        try{
            Path base = Paths.get(System.getProperty("user.dir"),
                    "res", "maps", mapName);
            if (!Files.exists(base)) {
                Files.createDirectories(base);
            }

            // for each layer 1–3
            for (int layerNum = 1; layerNum <= 3; layerNum++) {
                Layer layer = getLayer(layerNum);
                Path out = base.resolve("layer" + layerNum + ".txt");

                try (BufferedWriter bw = Files.newBufferedWriter(out,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING)) {
                    for (int row = 0; row < layer.height(); row++) {
                        for (int col = 0; col < layer.width(); col++) {
                            bw.write(Integer.toString(layer.getTile(col*gp.originalTileSize,row*gp.originalTileSize)));
                            if (col < layer.width() - 1) bw.write(' ');
                        }
                        bw.newLine();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public int height(){
        return layer1.height();
    }
    public int width(){
        return layer1.width();
    }
}
