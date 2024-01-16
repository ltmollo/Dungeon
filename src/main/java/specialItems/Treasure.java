package specialItems;

import players.Ability;

public class Treasure extends SpecialItem {

    public Treasure() {
        specialElement = SpecialElement.TREASURE;
        imagePath = "images/chest.png";

        rewards.put(Ability.EXPERIENCE, 15);
    }
}
