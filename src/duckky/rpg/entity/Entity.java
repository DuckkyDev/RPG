package duckky.rpg.entity;

public class Entity {
    public int x;
    public int y;
    public int speed;

    public enum Direction{
            UP,DOWN,LEFT,RIGHT
    }
    public Direction direction;

    public int spriteCounter = 0;
    public int spriteNumber = 1;
}
