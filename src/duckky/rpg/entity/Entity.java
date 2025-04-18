package duckky.rpg.entity;

public class Entity {
    public double x;
    public double y;
    public double speed;

    public enum Direction{
            UP,DOWN,LEFT,RIGHT
    }
    public Direction direction;

    public int spriteCounter = 0;
    public int spriteNumber = 1;
}
