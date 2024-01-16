package specialItems;

import players.Ability;

public class Sword extends SpecialItem {

    public Sword() {
        specialElement = SpecialElement.SWORD;
        imagePath = "images/sword.png";
        height = 500;

        rewards.put(Ability.AGILITY, 5);
        rewards.put(Ability.STRENGTH, 10);
        rewards.put(Ability.LUCK, 3);
        rewards.put(Ability.EXPERIENCE, 2);
    }
}
