package players;

import vectors.Direction;
import vectors.Vector2D;

public class Player {
    private int health;
    private int armor;
    private int strength;
    private int gold;
    private int experience;
    private int level;
    private int room;
    private int luck;
    private int agility;
    private Vector2D position;
    private Direction direction = Direction.NORTH;

    public Player(int armor, int agility, int luck, int strength){
        this.health = 100;
        this.armor = armor;
        this.gold = 7;
        this.experience = 0;
        this.level = 0;
        this.room = 0;
        this.strength = strength;
        this.luck = luck;
        this.agility = agility;
        this.position = new Vector2D(0, 0);
    }

    public Vector2D getPosition(){
        return this.position;
    }
    public void setPosition(Vector2D newPosition) { this.position = newPosition; }

    public Direction getDirection() {
        return direction;
    }
    public void changeDirection(Direction direction) {
        this.direction = direction;
    }
}