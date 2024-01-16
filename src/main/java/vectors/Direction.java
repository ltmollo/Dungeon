package vectors;

public enum Direction {
    NORTH("N"),
    EAST("E"),
    SOUTH("S"),
    WEST("W");

    private final String displayName;

    Direction(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Direction rotateClockwise() {
        return values()[(ordinal() + 1) % values().length];
    }

    public Direction rotateCounterClockwise() {
        return values()[(ordinal() - 1 + values().length) % values().length];
    }

    public Direction opositeDirection() {
        return values()[(ordinal() + 2) % values().length];
    }
}
