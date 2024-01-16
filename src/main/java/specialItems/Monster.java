package specialItems;

import players.Ability;

public class Monster extends SpecialItem {

    public Monster() {
        specialElement = SpecialElement.MONSTER;
        imagePath = "images/monster.png";

        rewards.put(Ability.STRENGTH, 2);
        rewards.put(Ability.EXPERIENCE, 6);
    }
}
