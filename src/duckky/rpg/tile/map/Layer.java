package duckky.rpg.tile.map;

import duckky.rpg.main.GamePanel;

class Layer {
    private final int[][] layer;
    private final GamePanel gp;
    Layer(int[][] layer, GamePanel gp){
        this.layer = layer;
        this.gp = gp;
    }
    int getTile(int x, int y) {
        int row = getRow(y);
        int column = getColumn(x);
        if(row != -1 && column != -1){
            return layer[row][column];
        } else {
            return 0;
        }
    }
    void setTile(int x, int y, int tile){
        int row = getRow(y);
        int column = getColumn(x);
        if(row != -1 && column != -1){
            layer[row][column] = tile;
        }
    }

    private int getRow(int y) {
        int row = y / gp.originalTileSize;

        if (row < 0 || row >= layer.length) {
            return -1;
        }
        return row;
    }
    private int getColumn(int x){
        int column = x / gp.originalTileSize;

        if (column < 0 || column >= layer[0].length) {
            return -1;
        }
        return column;
    }
    public int height(){
        return layer.length;
    }
    public int width(){
        return layer[0].length;
    }
}
