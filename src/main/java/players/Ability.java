package players;

public enum Ability {
    HEALTH("Health"),
    STRENGTH("Strength"),
    ARMOR("Armor"),
    AGILITY("Agility"),
    LUCK("Luck"),
    EXPERIENCE("Experience");

    private final String displayName;

    Ability(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
