package specialItems;

import players.Ability;

public class Helmet extends SpecialItem {
    public Helmet() {
        specialElement = SpecialElement.HELMET;
        imagePath = "images/helmet.png";

        rewards.put(Ability.ARMOR, 6);
        rewards.put(Ability.EXPERIENCE, 3);
    }
}
