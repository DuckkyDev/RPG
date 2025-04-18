//package ai;
//
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import javax.imageio.ImageIO;
//
//import main.GamePanel; // Assuming GamePanel is in the 'main' package
//import main.KeyHandler; // Assuming KeyHandler is in the 'main' package
//
//public class Player extends Entity {
//
//    GamePanel gp;
//    KeyHandler keyH;
//
//    // Constructor
//    public Player(GamePanel gp, KeyHandler keyH) {
//        this.gp = gp;
//        this.keyH = keyH;
//
//        setDefaultValues();
//        getPlayerImage(); // Load player sprites on creation
//    }
//
//    // Set player's default starting position, speed, and direction
//    public void setDefaultValues() {
//        x = 100; // Default starting X coordinate
//        y = 100; // Default starting Y coordinate
//        speed = 4; // Default movement speed (pixels per frame)
//        direction = "down"; // Initial direction
//    }
//
//    // Load player sprite images from the resource folder
//    public void getPlayerImage() {
//        try {
//            // Use getResourceAsStream to ensure images load correctly, even from a JAR file
//            up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
//            up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
//            down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
//            down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
//            left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
//            left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
//            right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
//            right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
//
//        } catch (IOException e) {
//            System.err.println("Error loading player images!");
//            e.printStackTrace(); // Print detailed error information
//        } catch (IllegalArgumentException e) {
//            System.err.println("Error finding player images! Check resource path.");
//            e.printStackTrace();
//        }
//    }
//
//    // Update player state (position, animation) based on input
//    public void update() {
//        // Check if any movement key is pressed to update position and animation
//        // This prevents the walking animation when standing still
//        if (keyH.upPressed == true || keyH.downPressed == true ||
//                keyH.leftPressed == true || keyH.rightPressed == true)
//        {
//            // Update position based on direction and speed
//            if (keyH.upPressed == true) {
//                direction = "up";
//                y -= speed; // Move up
//            } else if (keyH.downPressed == true) {
//                direction = "down";
//                y += speed; // Move down
//            } else if (keyH.leftPressed == true) {
//                direction = "left";
//                x -= speed; // Move left
//            } else if (keyH.rightPressed == true) {
//                direction = "right";
//                x += speed; // Move right
//            }
//
//            // Update sprite animation counter
//            spriteCounter++;
//            // Change sprite every 12 frames (adjust for desired animation speed)
//            if (spriteCounter > 12) {
//                if (spriteNum == 1) {
//                    spriteNum = 2; // Switch to sprite 2
//                } else if (spriteNum == 2) {
//                    spriteNum = 1; // Switch back to sprite 1
//                }
//                spriteCounter = 0; // Reset the counter
//            }
//        }
//        // Note: Collision detection logic would typically go here or be called from here.
//    }
//
//    // Draw the player character on the screen
//    public void draw(Graphics2D g2) {
//
//        BufferedImage image = null; // The image to draw in this frame
//
//        // Select the correct sprite based on current direction and animation frame (spriteNum)
//        switch (direction) {
//            case "up":
//                if (spriteNum == 1) { image = up1; }
//                if (spriteNum == 2) { image = up2; }
//                break;
//            case "down":
//                if (spriteNum == 1) { image = down1; }
//                if (spriteNum == 2) { image = down2; }
//                break;
//            case "left":
//                if (spriteNum == 1) { image = left1; }
//                if (spriteNum == 2) { image = left2; }
//                break;
//            case "right":
//                if (spriteNum == 1) { image = right1; }
//                if (spriteNum == 2) { image = right2; }
//                break;
//            default:
//                // Optional: Draw a default image or handle unexpected direction
//                image = down1; // Default to down1 if direction is somehow invalid
//                break;
//        }
//
//        // Draw the chosen image at the player's x, y coordinates
//        // Using gp.tileSize for both width and height assumes player sprites are square and fit the tile size
//        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null); // 'null' is for the ImageObserver, often not needed here.
//    }
//}
