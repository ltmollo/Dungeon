package players;

import specialItems.SpecialElement;
import specialItems.SpecialItem;
import vectors.Direction;
import vectors.Vector2D;

import java.util.HashMap;

public class Player {
    private int level;

    private final HashMap<SpecialElement, SpecialItem> pickedItems;
    private final HashMap<Ability, Integer> abilities;
    private Vector2D position;
    private Direction direction = Direction.NORTH;


    public Player(int armor, int agility, int luck, int strength) {
        this.level = 0;
        int health = 100;
        int experience = 0;
        this.position = new Vector2D(0, 0);

        pickedItems = new HashMap<>();
        abilities = new HashMap<>();

        abilities.put(Ability.HEALTH, health);
        abilities.put(Ability.STRENGTH, strength);
        abilities.put(Ability.ARMOR, armor);
        abilities.put(Ability.AGILITY, agility);
        abilities.put(Ability.LUCK, luck);
        abilities.put(Ability.EXPERIENCE, experience);
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public void setPosition(Vector2D newPosition) {
        this.position = newPosition;
    }

    public Direction getDirection() {
        return direction;
    }

    public void changeDirection(Direction direction) {
        this.direction = direction;
    }

    public HashMap<Ability, Integer> getAbilities() {
        return abilities;
    }

    public void updateAbilities(HashMap<Ability, Integer> rewards) {
        for (Ability ability : rewards.keySet()) {
            abilities.put(ability, abilities.get(ability) + rewards.get(ability));
        }
    }

    public void pickItem(SpecialItem specialItem) {
        pickedItems.put(specialItem.getSpecialElement(), specialItem);
    }

    public void removeItem(SpecialItem specialItem) {
        pickedItems.remove(specialItem);
    }

    public HashMap<SpecialElement, SpecialItem> getPickedItems() {
        return this.pickedItems;
    }

    public int strike() {
        int strength = abilities.get(Ability.STRENGTH);
        int agility = abilities.get(Ability.AGILITY);
        int luck = abilities.get(Ability.LUCK);

        double random = Math.random() * luck;

        return (int) (strength + agility + random);
    }

    public int takeDamage(int hit) {
        int damageTaken = hit - abilities.get(Ability.ARMOR);
        int health = abilities.get(Ability.HEALTH);

        abilities.put(Ability.HEALTH, health - damageTaken);

        return hit;
    }

    public boolean isDead() {
        return abilities.get(Ability.HEALTH) <= 0;
    }
}