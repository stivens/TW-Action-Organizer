package twao.villages;

import twao.World;
import twao.exceptions.VillageNotFoundException;

import java.lang.Math;

public class Village {
    private final int x;
    private final int y;
    private int id;

    private int relativeDistance;

    public Village(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static int distance(Village v1, Village v2) {
        return (int) (Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2));
    }

    @Override
    public String toString() {
        return String.format("%s|%s", x, y);
    }

    /**
     * -----------------------------------------------------------
     * getters and setters
     * -----------------------------------------------------------
     */

    public int getX() { return x; }

    public int getY() { return y; }

    public int getId() { return id; }

    public void findId(World world) throws VillageNotFoundException {
        id = world.getVillageId(this);
    }

    public int getRelativeDistance() { return relativeDistance; }

    public void setRelativeDistance(Village vil) { relativeDistance = distance(this, vil); }
}
