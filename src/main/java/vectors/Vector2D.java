package vectors;

import java.util.Objects;

public class Vector2D {
    public final int x;
    public final int y;
    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector2D add(Vector2D other){
        int newX = this.x + other.x;
        int newY = this.y + other.y;
        return new Vector2D(newX, newY);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    @Override
    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2D))
            return false;
        Vector2D compared = (Vector2D)other;
        return this.x == compared.x && this.y == compared.y;
    }

    @Override
    public String toString(){
        return "Vector2d: (" + x + ", " + y + ")";
    }
}
