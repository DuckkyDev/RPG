package duckky.rpg.main;

import java.awt.event.*;

public class KeyHandler implements KeyListener, MouseListener,MouseMotionListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean leftMousePressed,middleMousePressed,rightMousePressed;
    public int mouseX,mouseY;
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
        if(ctrlPressed && code == KeyEvent.VK_EQUALS){
            gp.zoom(2);
        }
        // Zoom out (Ctrl + Minus)
        if (ctrlPressed && code == KeyEvent.VK_MINUS) {
            gp.zoom(1);
        }
        if (ctrlPressed && code == KeyEvent.VK_0) {
            gp.zoom(0);
        }
        if(code == KeyEvent.VK_BACK_QUOTE){
            gp.editor.isActive = !gp.editor.isActive;
        }
        if(gp.editor.isActive){
            if(code == KeyEvent.VK_1){
                gp.editor.selectedLayer = 1;
            }
            if(code == KeyEvent.VK_2){
                gp.editor.selectedLayer = 2;
            }
            if(code == KeyEvent.VK_3){
                gp.editor.selectedLayer = 3;
            }
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

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gp.editor.mouseDown(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftMousePressed = true;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            middleMousePressed = true;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightMousePressed = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gp.editor.mouseUp(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftMousePressed = false;
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            middleMousePressed = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightMousePressed = false;
        }
    }


    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}