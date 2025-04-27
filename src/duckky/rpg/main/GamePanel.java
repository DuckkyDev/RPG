package duckky.rpg.main;

import duckky.rpg.editor.Editor;
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

    public TileManager tileManager = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Thread gameThread;

    public Editor editor = new Editor(this);

    public Player player = new Player(this,keyH);

    public int targetFPS = 30;
    public int FPS = targetFPS;

    public int tick = 0;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.DARK_GRAY);
        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);
        this.addMouseMotionListener(keyH);
        this.addMouseListener(keyH);

        this.setFocusable(true);
        tileManager.loadMap("map01");
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
                //System.out.println("FPS: " + FPS);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    public void tick() {
        tick+=1;
        player.tick();
        editor.tick();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileManager.renderBehind(g2);
        player.render(g2);
        tileManager.renderInFront(g2);
        editor.render(g2);

        g2.dispose();
    }
    public void drawImage(BufferedImage image, int x, int y, Graphics2D g2) {
        // Calculate the screen coordinates
        int screenX = convertToWorldX(x);
        int screenY = convertToWorldY(y);
        // Check if the image is within the screen bounds
        if (screenX + tileSize > 0 &&
                screenX < screenWidth &&
                screenY + tileSize > 0 &&
                screenY < screenHeight) {
            g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
        }
    }
    public void drawRect(int x, int y, int width, int height, Color color, Graphics2D g2) {

        int screenX = convertToWorldX(x);
        int screenY = convertToWorldY(y);

        int scaledWidth = (int) Math.round(width * scale);
        int scaledHeight = (int) Math.round(height * scale);

        scaledWidth = Math.max(1, scaledWidth);
        scaledHeight = Math.max(1, scaledHeight);

        if (screenX + scaledWidth > 0 &&
                screenX < screenWidth &&
                screenY + scaledHeight > 0 &&
                screenY < screenHeight) {
            g2.setColor(color);
            g2.drawRect(screenX, screenY, scaledWidth, scaledHeight);
        }
    }
    public int convertToWorldX(int x){
        return ((int) (Math.round((x - player.camX) * scale))) + (screenWidth / 2);
    }
    public int convertToWorldY(int y){
        return ((int) (Math.round((y - player.camY) * scale))) + (screenHeight / 2);
    }
    public void zoom(int i){
        if (i == 2) {
            scale += 0.25;
        } else if(i == 1){
            scale -= 0.25;
        } else {
            scale = defaultScale;
        }
        if (scale < 1){
            scale = 1;
        }
        tileSize = (int) (originalTileSize * scale);
    }
}
