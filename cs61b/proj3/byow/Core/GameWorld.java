package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameWorld {
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    private String saveScript =  "";
    private long seed;
    private Random rand;
    TETile[][] world;
    private HashMap<String, ArrayList<Position>> allSquares;
    private int numOfSq;
    HashMap<String, ArrayList<Position>> doorMap;
    ArrayList<String> unconnected;
    Position avatarPosition;
    String avatarName;

    /*Position class in the wrapper class for the  coordinate system in place*/
    public static class Position {
        int x;
        int y;

        public Position(int xCor, int yCor) {
            x = xCor;
            y = yCor;
        }
    }
    public Random getRand() {
        return this.rand;
    }
    public int getNumOfSq() {
        return this.numOfSq;
    }
    public String getSaveScript() {
        return this.saveScript;
    }
    public void setSaveScript(String s) {
        saveScript = s;
    }
    public static boolean isInsideCircle(GameWorld.Position center,
                                         int rad, GameWorld.Position point) {
        // Compare radius of circle with
        // distance of its center from
        // given point
        return (point.x - center.x) * (point.x - center.x)
                + (point.y - center.y) * (point.y - center.y)
                <= rad * rad;
    }


    /*Constructor for gameWorld initializes the @world @allSquares HashMap
     * @Counter for the number of squares in the world*/
    public GameWorld(long sd) {
        seed = sd;
        rand = new Random(seed);
        world = new TETile[WIDTH][HEIGHT];
        allSquares = new HashMap<>();
        doorMap = new HashMap<>();
        unconnected = new ArrayList<>();
        numOfSq = 0;
        saveScript = "";
        avatarName = "";

    }

    /*checks if the current position produces a conflict with all the present squares*/
    private boolean inSquare(HashMap<String, ArrayList<Position>> map, Position point) {
        for (ArrayList<Position> p : map.values()) {
            Position p1 = p.get(0);
            Position p2 = p.get(1);
            if (p1.x <= point.x && p2.x >= point.x
                    && p1.y <= point.y && p2.y >= point.y) {
                return true;
            }
        }
        return false;
    }

    /*Creates a HashMap that is provided to the buildSquare method itself.*/
    private HashMap<String, Position> squarePoints() {
        HashMap<String, Position> pointMap = new HashMap<>();
        int x1 = rand.nextInt(WIDTH);
        int y1 = rand.nextInt(HEIGHT);
        int x3 = rand.nextInt(WIDTH);
        int y3 = rand.nextInt(HEIGHT);

        while ((Math.abs(x1 - x3) <= 1) || Math.abs(x1 - x3) > 5) {
            x3 = rand.nextInt(WIDTH);
        }
        while (Math.abs(y1 - y3) <= 1 || Math.abs(y1 - y3) > 7) {
            y3 = rand.nextInt(HEIGHT);
        }
        Position p1 = new Position(x1, y1);
        Position p3 = new Position(x3, y3);

        while (inSquare(allSquares, p1) || inSquare(allSquares, p3)) {
            return squarePoints();
        }
        if (x1 > x3) {
            int temp = x3;
            x3 = x1;
            x1 = temp;
        }
        if (y1 > y3) {
            int temp2 = y3;
            y3 = y1;
            y1 = temp2;
        }
        p1 = new Position(x1, y1);
        p3 = new Position(x3, y3);
        ArrayList<Position> currArrayList = new ArrayList<>();
        currArrayList.add(p1);
        currArrayList.add(p3);
        String currString = Integer.toString(numOfSq);
        numOfSq += 1;
        allSquares.put(currString, currArrayList);

        int x4 = x3;
        int y4 = y1;
        int x2 = x1;
        int y2 = y3;

        Position p2 = new Position(x2, y2);
        Position p4 = new Position(x4, y4);
        pointMap.put("p3", p3);
        pointMap.put("p1", p1);
        pointMap.put("p2", p2);
        pointMap.put("p4", p4);

        return pointMap;
    }

    /*makes a square in the world, checks for the conflicts
     * Need to add doors to the given square*/
    public void buildRandomSquare() {

        HashMap<String, Position> map = squarePoints();
        int y1 = map.get("p1").y;
        int x1 = map.get("p1").x;
        int y2 = map.get("p2").y;
        int x2 = map.get("p2").x;
        int y3 = map.get("p3").y;
        int x3 = map.get("p3").x;
        int y4 = map.get("p4").y;
        int x4 = map.get("p4").x;

        for (int i = y1; i < y2; i++) {
            world[x1][i] = Tileset.WALL;
        }
        for (int j = x2; j < x3; j++) {
            world[j][y2] = Tileset.WALL;
        }
        for (int k = y3; k > y4; k--) {
            world[x4][k] = Tileset.WALL;
        }

        for (int l = x4; l > x1; l--) {
            world[l][y4] = Tileset.WALL;
        }
        unconnected.add(Integer.toString(numOfSq));
        layFloor(new Position(x1, y1), new Position(x3, y3));

    }

    public void buildManyRandomSq() {
        System.out.println("here");
        int sqNum = 0;
        int t = rand.nextInt(19) + rand.nextInt(6);
        System.out.println(t);

        while (sqNum < t) {
            buildRandomSquare();
            sqNum += 1;
        }
        for (String s: allSquares.keySet()) {
            generateHallwayfromSquare(s); }
//        generateHallwayfromSquare("0");
    }

    private void generateHallwayfromSquare(String square) {
        Random numDoor = new Random(seed);
        int num = numDoor.nextInt(1) + 1;
        while (num > 0) {
            Random ran = new Random(seed);
            if (allSquares.size() == 1) {
                num -= 1;
                continue;

            } else {
                int s = ran.nextInt(allSquares.size() - 1);
                String key = unconnected.get(s);
                connect(square, key);
                num -= 1;
            }
        }
    }

    private void connect(String from, String to) {
        Position fromSq = centerPosition(allSquares.get(from));
        Position toSq = centerPosition(allSquares.get(to));
        if (fromSq.x < toSq.x) {
            int startx = allSquares.get(from).get(1).x;
            int endX = toSq.x;
            int y = fromSq.y;
            connectWithHorizontal(startx, endX, y);
            addCorner(endX, y, endX, toSq.y);
            connectWithVertical(y, toSq.y, endX);

        } else {
            int startx = allSquares.get(to).get(1).x;
            int endX = fromSq.x;
            int y = toSq.y;
            connectWithVertical(y, fromSq.y, endX);
            addCorner(endX, y, endX, fromSq.y);
            connectWithHorizontal(startx, endX, y);
        }
    }
    private void addCorner(int currX, int currY, int goalx, int goaly) {
        Position currCood = new Position(currX, currY);

        if (goaly > currY) {
            turnLeftUp(currCood); //horizontal left first
        } else if (goaly < currY) {
            turnRightDown(currCood); //horizontal right
        } else if (goalx > currX) {
            turnRightSide(currCood); //vertical right
        } else if (goalx < currX) {
            turnleftSide(currCood); //vertical left
        }
    }
    private void turnRightSide(Position p) {
        drawAllFloor(p.x, p.y);
        drawAlleyWall(p.x - 1, p.y + 1);
        drawAlleyWall(p.x, p.y + 1);
        drawAlleyWall(p.x - 1, p.y);
    }
    private void turnleftSide(Position p) {
        drawAllFloor(p.x, p.y);
        drawAlleyWall(p.x + 1, p.y + 1);
        drawAlleyWall(p.x, p.y + 1);
        drawAlleyWall(p.x + 1, p.y);
    }
    private void turnRightDown(Position p) {
        drawAllFloor(p.x, p.y);
        drawAlleyWall(p.x, p.y + 1);
        drawAlleyWall(p.x + 1, p.y + 1);
        drawAlleyWall(p.x + 1, p.y);
    }
    private void turnLeftUp(Position p) {
        drawAllFloor(p.x, p.y);
        drawAlleyWall(p.x + 1, p.y);
        drawAlleyWall(p.x, p.y - 1);
        drawAlleyWall(p.x + 1, p.y - 1);
    }
    private void connectWithHorizontal(int startx, int endx, int y) {
        int startX = startx;
        int endX = endx;
        int y1 = y;

        while (startX < endX) {
            drawAlleyWall(startX, y1 + 1);
            drawAllFloor(startX, y);
            drawAlleyWall(startX, y1 - 1);
            startX += 1;
        }
    }
    private void connectWithVertical(int Y1, int Y2, int x1) {
        int starty;
        int endy;
        int x = x1;
        if (Y1 < Y2) {
            starty = Y1;
            endy = Y2;

        } else {
            starty = Y2;
            endy = Y1;
        }
        while (starty < endy) {
            drawAlleyWall(x - 1, starty);
            drawAllFloor(x, starty);
            drawAlleyWall(x + 1, starty);
            starty += 1;
        }
    }

    private void drawAlleyWall(int x, int y) {
        if (y < 0 || x < 0) {
            return;
        }
        if (!(world[x][y].equals(Tileset.FLOOR))) {
            world[x][y] = Tileset.WALL;
        }
    }
    private void drawAllFloor(int x, int y) {
        if (x < 0 || y < 0) {
            return;
        }
        world[x][y] = Tileset.FLOOR;
    }
    private Position centerPosition(ArrayList<Position> position) {
        Position p1 = position.get(0);
        Position p2 = position.get(1);
        int x = p1.x + Math.abs(p1.x - p2.x) / 2;
        int y = p1.y + Math.abs(p1.y - p2.y) / 2;
        Position p = new Position(x, y);
        return p;
    }
    private void  layFloor(Position p1, Position p2) {
        for (int i = p1.x + 1; i < p2.x; i++) {
            for (int j = p1.y + 1; j < p2.y; j++) {
                world[i][j] = Tileset.GRASS;
            }
        }
    }
    public static void main(String[] args) {
////        N23SDDDDDDWWWW:q
//        ter.renderFrame(world);
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
        Engine end = new Engine();
//        TETile[][] num = end.interactWithInputString("n5985216251998126237swwddasaaaaawdsws");
//        ter.renderFrame(num);

        end.interactWithKeyboard();

    }
//


}
