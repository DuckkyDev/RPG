package duckky.rpg.main;

import duckky.rpg.entity.Player;
import duckky.rpg.tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {
    //SCREEN SETTINGS
    public final int originalTileSize = 16;
    public final double scale = 3;

    public final int tileSize = (int) (originalTileSize*scale);
    public final int maxScreenColumn = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = (tileSize * maxScreenColumn);
    public final int screenHeight = (tileSize * maxScreenRow);

    TileManager tileManager = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    Player player = new Player(this,keyH);

    public int targetFPS = 60;
    public int FPS = 60;

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

        g2.dispose();
    }

    public void drawImage(BufferedImage image, double x, double y, Graphics2D g2){
        g2.drawImage(image,(int) (x*tileSize),(int) (y*tileSize),tileSize,tileSize,null);
    }
    public void drawImage(BufferedImage image, double x, double y, double widthMultiplier,double heightMultiplier,Graphics2D g2){
        g2.drawImage(image,(int) (x*tileSize),(int) (y*tileSize), (int) (tileSize*widthMultiplier),(int) (tileSize*widthMultiplier),null);
    }
}
