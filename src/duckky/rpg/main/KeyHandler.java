package duckky.rpg.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    GamePanel gp;
    private boolean ctrlPressed = false;

    KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_CONTROL){
            ctrlPressed = true;
        }

        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }

        // Zoom in (Ctrl + Plus)
        if(ctrlPressed && code == KeyEvent.VK_EQUALS){ // VK_EQUALS is usually the '+' key without shift
            gp.zoom(2); // Assuming zoom(2) zooms in
        }

        // Zoom out (Ctrl + Minus)
        if (ctrlPressed && code == KeyEvent.VK_MINUS) {
            gp.zoom(1); // Assuming zoom(1) zooms out (or resets to default)
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_CONTROL){
            ctrlPressed = false;
        }
        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }
    }
}