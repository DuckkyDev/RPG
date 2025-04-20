package duckky.rpg.main;

import duckky.rpg.entity.Player;
import duckky.rpg.tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {
    //SCREEN SETTINGS
    public final int originalTileSize = 16;
    public final int defaultScale = 3;
    public double scale = defaultScale;

    public int tileSize = (int) (originalTileSize*scale);
    public final int maxScreenColumn = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = (tileSize * maxScreenColumn);
    public final int screenHeight = (tileSize * maxScreenRow);

    TileManager tileManager = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;

    Player player = new Player(this,keyH);

    public int targetFPS = 30;
    public int FPS = targetFPS;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.DARK_GRAY);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        tileManager.loadMap("/maps/map01.txt");
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run(){
        double drawInterval = (double) 1000000000 / targetFPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){
            currentTime = System.nanoTime();

            delta += (currentTime-lastTime) / drawInterval;
            timer += (currentTime-lastTime);

            lastTime = currentTime;

            if(delta >= 1){
                tick();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000){
                FPS = drawCount;
                System.out.println("FPS: " + FPS);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    public void tick() {
        player.tick();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileManager.render(g2);
        player.render(g2);
    }
    public void drawImage(BufferedImage image, double x, double y, Graphics2D g2) {
        // Calculate the screen coordinates
        int screenX = (int) Math.round(((x - player.camX) * tileSize) + (screenWidth / 2.0) - (tileSize / 2.0));
        int screenY = (int) Math.round(((y - player.camY) * tileSize) + (screenHeight / 2.0) - (tileSize / 2.0));
        // Check if the image is within the screen bounds
        if (screenX + tileSize > 0 &&
                screenX < screenWidth &&
                screenY + tileSize > 0 &&
                screenY < screenHeight) {
            g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
        }
    }
    public void zoom(int i){
        if (i == 2) {
            scale += 0.1;
        } else if(i == 1){
            scale -= 0.1;
        } else {
            scale = defaultScale;
        }
        if (scale < 1){
            scale = 1;
        }
        tileSize = (int) (originalTileSize * scale);
    }
}
