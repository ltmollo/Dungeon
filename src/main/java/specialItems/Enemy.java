package specialItems;

public interface Enemy {
    public int strike();

    public int takeDamage(int hit);

    public boolean isDead();
}
