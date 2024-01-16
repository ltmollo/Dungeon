package specialItems;

import players.Ability;

import java.util.HashMap;

public class Monster extends SpecialItem implements Enemy {

    private final HashMap<Ability, Integer> abilities;

    public Monster() {
        specialElement = SpecialElement.MONSTER;
        imagePath = "images/monster.png";
        abilities = new HashMap<>();

        width = 300;
        height = 300;

        abilities.put(Ability.HEALTH, 50);
        abilities.put(Ability.STRENGTH, 10);
        abilities.put(Ability.ARMOR, 7);
        abilities.put(Ability.AGILITY, 9);
        abilities.put(Ability.LUCK, 3);
    }


    @Override
    public int strike() {
        int strength = abilities.get(Ability.STRENGTH);
        int agility = abilities.get(Ability.AGILITY);
        int luck = abilities.get(Ability.LUCK);

        double random = Math.random() * luck;

        return (int) (strength + agility + random);
    }

    @Override
    public int takeDamage(int hit) {
        int damageTaken = hit - abilities.get(Ability.ARMOR);
        int health = abilities.get(Ability.HEALTH);
        abilities.put(Ability.HEALTH, health - damageTaken);

        return damageTaken;
    }

    @Override
    public boolean isDead() {
        return abilities.get(Ability.HEALTH) <= 0;
    }
}
