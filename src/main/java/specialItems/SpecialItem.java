package specialItems;

import players.Ability;

import java.util.HashMap;

public abstract class SpecialItem {

    protected SpecialElement specialElement;
    protected String imagePath;

    protected HashMap<Ability, Integer> rewards = new HashMap<>();

    int width = 200;
    int height = 200;

    public String getImagePath() {
        return imagePath;
    }

    public HashMap<Ability, Integer> getRewards() {
        return rewards;
    }

    public SpecialElement getSpecialElement() {
        return specialElement;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
