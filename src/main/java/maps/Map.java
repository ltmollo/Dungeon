package maps;

import vectors.Direction;
import vectors.Vector2D;

public class Map {
    private final int MAX_X = 4 + 1;
    public final int MAX_y = 10 + 1;

    int[][] northDoors = new int[MAX_X][MAX_y];
    int[][] eastDoors = new int[MAX_X][MAX_y];
    int[][] southDoors = new int[MAX_X][MAX_y];
    int[][] westDoors = new int[MAX_X][MAX_y];

    public Map() {
        initializeNorthMap();
        initializeEastMap();
        initializeSouthMap();
        initializeWestMap();
    }

    private void initializeNorthMap() {
        for (int i = 0; i < 4; i++) {
            northDoors[0][i] = 1;
        }

        for (int i = 2; i < 7; i++) {
            northDoors[2][i] = 1;
        }

        for (int i = 7; i < 9; i++) {
            northDoors[4][i] = 1;
        }

        northDoors[3][4] = 1;
        northDoors[1][9] = 1;
    }

    private void initializeEastMap() {
        for (int i = 0; i < 2; i++) {
            eastDoors[i][3] = 1;
            eastDoors[2][i + 4] = 1;
        }

        for (int i = 1; i < 4; i++) {
            eastDoors[i][7] = 1;
            eastDoors[i][9] = 1;

        }
    }

    private void initializeSouthMap() {
        for (int i = 1; i < 5; i++) {
            southDoors[0][i] = 1;
        }

        for (int i = 3; i < 8; i++) {
            southDoors[2][i] = 1;
        }

        for (int i = 8; i < 10; i++) {
            southDoors[4][i] = 1;
        }

        southDoors[3][5] = 1;
        southDoors[1][10] = 1;
    }

    private void initializeWestMap() {
        for (int i = 1; i < 3; i++) {
            westDoors[i][3] = 1;
            westDoors[3][i + 3] = 1;
        }

        for (int i = 2; i < 5; i++) {
            westDoors[i][7] = 1;
            westDoors[i][9] = 1;

        }
    }

    public Vector2D[] neighboursRooms(Vector2D position){
        int[] neighbours = checkDoorsInRoom(position);
        Vector2D[] neighboursTable = new Vector2D[4];

        if(neighbours[0] == 1){
            neighboursTable[0] = new Vector2D(position.x, position.y+1);
        }
        if(neighbours[1] == 1){
            neighboursTable[1] = new Vector2D(position.x+1, position.y);
        }
        if(neighbours[2] == 1){
            neighboursTable[2] = new Vector2D(position.x, position.y-1);
        }
        if(neighbours[3] == 1){
            neighboursTable[3] = new Vector2D(position.x-1, position.y);
        }
        return neighboursTable;
    }

    public int[] checkDoorsInRoom(Vector2D position) {
        int x = position.x;
        int y = position.y;

        int[] positions = new int[Direction.values().length];
        positions[Direction.NORTH.ordinal()] = northDoors[x][y];
        positions[Direction.EAST.ordinal()] = eastDoors[x][y];
        positions[Direction.SOUTH.ordinal()] = southDoors[x][y];
        positions[Direction.WEST.ordinal()] = westDoors[x][y];

        return positions;
    }

}
