package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 36;
    private static final int HEIGHT = 36;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    private Position startPosition;
    private TETile[][] world;

    private class Position {
        int xCor;
        int yCor;
        public Position(int x, int y){
            xCor = x;
            yCor = y;

        }
    }
    public HexWorld(int x, int y) {
        startPosition = new Position(x, y);
        world = new TETile[WIDTH][HEIGHT];
    }

    public void addHexagon(Position pos, int side, TETile T) {
        int startPosX = pos.xCor;
        int startPosY = pos.yCor;
        int maxSize = side + 2 * (side-1);
        for(int i = side; i <= maxSize; i+=2) {
                draw(i, startPosX, startPosY, world, T);
                startPosX -= 1;
                startPosY += 1;
        }
        startPosX += 1;
        for(int j = maxSize; j >= side; j-=2){
            draw(j, startPosX, startPosY, world, T);
            startPosY += 1;
            startPosX += 1;
        }
    }
    /*No case for out of bounds errors e.g. only complete hexagons can be built only*/

    private void draw(int size, int startPosX, int startPosY, TETile[][] world, TETile T) {
        int i = size;
        while(i > 0) {
            world[startPosX][startPosY] = T;
            startPosX += 1;
            i -= 1;


        }
    }
    private boolean possible(int tileSize) {
        int height = HEIGHT / (2 * tileSize);
        int width = WIDTH / (tileSize + (2 * (tileSize-1)));
        return (height >= 5 && width >= 4);
    }


    public void catanWorld(int tileSize) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        if (possible(tileSize)) {
            int xCor = (WIDTH / 2) -  (tileSize / 2) - 1;
            int yCor = ((HEIGHT / 2  ) - tileSize) - 1;
            Position centerStart = new Position(xCor, yCor);
            startBuild(centerStart, tileSize);
        }

    }
    private void startBuild(Position startPos, int tileSize) {
        String[] arr = new String[]{"1","2","3","4","5"};
        HashMap<String, Position> posMap= new HashMap<>();

        for(String i: arr) {
            if (i.equals("1")) {
                int newY = startPos.yCor - 2 * tileSize;
                int newX = startPos.xCor - 4 * tileSize + 1;
                Position newPos = new Position(newX, newY);
                buildCol(newPos, 3, tileSize);
            }
            else if (i.equals("2")) {
                int newY = startPos.yCor - 3 * tileSize;
                int newX = startPos.xCor - 2 * tileSize;
                Position newPos = new Position(newX, newY);
                buildCol(newPos, 4, tileSize);
            }
            else if (i.equals("3")) {
                int newY = startPos.yCor - 4 * tileSize;
                int newX = startPos.xCor - 1;
                Position newPos = new Position(newX, newY);
                buildCol(newPos, 5, tileSize);
            }
            else if (i.equals("4")) {
                int newY = startPos.yCor - 3 * tileSize;
                int newX = startPos.xCor + 2 * tileSize - 2;
                Position newPos = new Position(newX, newY);
                buildCol(newPos, 4, tileSize);
            }
            else if (i.equals("5")) {
                int newY = startPos.yCor - 2 * tileSize;
                int newX = startPos.xCor + 4 * tileSize - 3;
                Position newPos = new Position(newX, newY);
                buildCol(newPos, 3, tileSize);
            }
        }
    }
    /*constant to keep tabs of the total num of tiles*/
    private void buildCol(Position startPos, int columnSize, int tilesize) {
        Position currPos = startPos;
        while (columnSize > 0) {
            TETile t = randomTileGen();
            addHexagon(currPos, tilesize, t);
            currPos.yCor = currPos.yCor + tilesize * 2;
            columnSize -= 1;


        }
    }
    private TETile randomTileGen() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.FLOOR;
            case 3: return Tileset.AVATAR;
            case 4: return Tileset.GRASS;
            default: return Tileset.LOCKED_DOOR;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        HexWorld hWorld = new HexWorld(30,30 );
        hWorld.catanWorld(3);
        ter.renderFrame(hWorld.world);



    }
}
