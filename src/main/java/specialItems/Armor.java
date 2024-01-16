package specialItems;

import players.Ability;

public class Armor extends SpecialItem {

    public Armor() {
        specialElement = SpecialElement.ARMOR;
        imagePath = "images/armor.png";

        rewards.put(Ability.ARMOR, 10);
        rewards.put(Ability.EXPERIENCE, 1);
    }
}
