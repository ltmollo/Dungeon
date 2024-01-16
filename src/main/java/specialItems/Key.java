package specialItems;

import players.Ability;

public class Key extends SpecialItem {

    public Key() {
        specialElement = SpecialElement.KEY;
        imagePath = "images/key.png";

        width = 500;

        rewards.put(Ability.EXPERIENCE, 5);
    }
}
