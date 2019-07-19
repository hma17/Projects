package byow.Core;


import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import edu.princeton.cs.introcs.StdDraw;


import java.awt.Font;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;





public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;



    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
//    public Engine () {
//        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 20);
//        Font font = new Font("Monaco", Font.BOLD, 30);
//        StdDraw.setFont(font);
//        StdDraw.setXscale(0, WIDTH);
//        StdDraw.setYscale(0, HEIGHT);
//        StdDraw.clear(Color.WHITE);
//        StdDraw.enableDoubleBuffering();
//    }
/*  Save the file as world info: the seed number and the additional movements*/
    private void saveFile(GameWorld world) {
        File file;
        try {
            file = new File("C:\\Users"
                    + "\\MohammadLoqman\\cs61b"
                    + "\\sp19-s1547\\proj3\\byow\\Core\\previousGame.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(world.getSaveScript());
            out.close();

        } catch (IOException e) {
            return;
        }
    }


    /*check for walls other stuff that can be added to this, let Q pass through as well
    * Need to handle the case of false input*/
    private boolean validMove(GameWorld world, Character nextKey) {
        if (nextKey.equals('Q') || nextKey.equals('q') || nextKey.equals(':')) {
            return true;
        } else if (nextKey.equals('W') || nextKey.equals('w')) {
            if (world.world[world.avatarPosition.x]
                    [world.avatarPosition.y + 1].equals(Tileset.WALL)) {
                return false;
            }
        } else if (nextKey.equals('S') || nextKey.equals('s')) {
            if (world.world[world.avatarPosition.x]
                    [world.avatarPosition.y - 1].equals(Tileset.WALL)) {
                return false;
            }
        } else if (nextKey.equals('D') || nextKey.equals('d')) {
            if (world.world[world.avatarPosition.x + 1]
                    [world.avatarPosition.y].equals(Tileset.WALL)) {
                return false;
            }
        } else if (nextKey.equals('A') || nextKey.equals('a')) {
            if (world.world[world.avatarPosition.x - 1]
                    [world.avatarPosition.y].equals(Tileset.WALL)) {
                return false;
            }
        } else {
            return true;
        }
        return true;
    }
    /*Avatar Positioning: muve up down, left, right*/
    private void moveUp(GameWorld world) {
        world.world[world.avatarPosition.x][world.avatarPosition.y] = Tileset.FLOOR;
        world.world[world.avatarPosition.x][world.avatarPosition.y + 1] = Tileset.AVATAR;
        world.avatarPosition.y = world.avatarPosition.y + 1;
    }
    private void moveDown(GameWorld world) {
        world.world[world.avatarPosition.x][world.avatarPosition.y] = Tileset.FLOOR;
        world.world[world.avatarPosition.x][world.avatarPosition.y - 1] = Tileset.AVATAR;
        world.avatarPosition.y = world.avatarPosition.y - 1;
    }
    private void moveRight(GameWorld world) {
        world.world[world.avatarPosition.x][world.avatarPosition.y] = Tileset.FLOOR;
        world.world[world.avatarPosition.x + 1][world.avatarPosition.y] = Tileset.AVATAR;
        world.avatarPosition.x = world.avatarPosition.x + 1;
    }
    private void moveLeft(GameWorld world) {
        world.world[world.avatarPosition.x][world.avatarPosition.y] = Tileset.FLOOR;
        world.world[world.avatarPosition.x - 1][world.avatarPosition.y] = Tileset.AVATAR;
        world.avatarPosition.x = world.avatarPosition.x - 1;
    }

    /*Movement detection and guiding theking of movement needed by the avatar*/
    private TETile[][] move(Character nextkey, GameWorld world) {
        if (nextkey.equals('w') || nextkey.equals('W')) {
            moveUp(world);
        } else if (nextkey.equals('s') || nextkey.equals('S')) {
            moveDown(world);
        } else if (nextkey.equals('d') || nextkey.equals('D')) {
            moveRight(world);
        } else if (nextkey.equals('a') || nextkey.equals('A')) {
            moveLeft(world);
        }
        return world.world;
    }
    private String displayMouseTile(GameWorld world) {
        Double valX = StdDraw.mouseX();
        int vX = valX.intValue();
        Double valY = StdDraw.mouseY();
        int vY = valY.intValue();
        if (valX > 0 && valX < WIDTH - 1 && valY > 0 && valY < HEIGHT - 1) {
            return world.world[vX][vY].description();
        }
        return "";

    }
    /*Need to add the commands making a valid move
    * Need to also see to it that the correct moves
    * are being updated in the final variable setup
    * */
    private void playGame(GameWorld world, String loadedGameString) {
        boolean play = true;
        Character prevKey = 'p';
        Character nextKey = null;
        loadedGameString += world.getSaveScript();
        world.setSaveScript(loadedGameString);
        while (play) {
            if (StdDraw.hasNextKeyTyped()) {
                nextKey = StdDraw.nextKeyTyped();
                if (validMove(world, nextKey)) {
                    if (nextKey.equals(':')) {
                        prevKey = ':';
                    }
                    if (nextKey.equals('Q') || nextKey.equals('q') && prevKey.equals(':')) {
                        saveFile(world);
                        return;
                    } else {
                        move(nextKey, world);
                        loadedGameString += Character.toString(nextKey);
                        world.setSaveScript(loadedGameString);
                        prevKey = nextKey;
                    }
                }
                prevKey = nextKey;
            }
            world.setSaveScript(loadedGameString);
            String tile = displayMouseTile(world);
            ter.renderAll(world.world, tile);
        }
//        System.out.println("done");f
//        System.out.println(world.SaveScript);
    }

    /*Read the stored file and return a combined list*/
    private String readFile() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("C:\\Users"
                    + "\\MohammadLoqman\\cs61b\\sp19-s1547"
                    + "\\proj3\\byow\\Core\\previousGame.txt"));
            StringBuilder sb = new StringBuilder();
            String  toLoad;
            try {
                toLoad = in.readLine();
                while (toLoad != null) {
                    sb.append(toLoad);
                    sb.append(System.lineSeparator());
                    toLoad = in.readLine();
                }
                String everything = sb.toString();
                return  everything;
            } catch (IOException E) {
                System.out.println("do nothin");
            }
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
        return null;

    }
    /*Get the seed info from the String read byt he readFile method*/
    private long getSeedFromLoad(String[] all) {
        int x = 0;
        String seedst = "";

        for (String s : all) {
            if (s.equals("N") || all[x].equals("n")) {
                x += 1;
            } else if (s.equals("s") || s.equals("S")) {
                return Long.parseLong(seedst);
            } else {
                seedst += all[x];
                x += 1;
            }
        }
        return Long.parseLong(seedst);
    }

    /*Get the moves string from the String originally achieved by the readFile*/
    private String getMoves(String[] all) {
        boolean addNow = false;
        String curr = "";
        for (String s : all) {
            if (addNow) {
                curr += s;
            }
            if (s.equals("S") || s.equals("s")) {
                addNow = true;
            }
        }
        return curr;
    }

    /*Method loads the pld game up to the  quit stage. PLaces the avatar by itself.*/
    private void loadOldGame() {
        String[] moveList = readFile().split("");
        Long seed = getSeedFromLoad(moveList);
        String[] moves = getMoves(moveList).split("");

        ter.initialize(WIDTH, HEIGHT, 0, -3);
        GameWorld world = new GameWorld(seed);
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world.world[x][y] = Tileset.NOTHING;
            }
        }
        world.buildManyRandomSq();
        GameWorld.Position pos = randomAvatarPosition(world);
        world.world[pos.x][pos.y] = Tileset.AVATAR;
        for (String s: moves) {
            if (validMove(world, s.charAt(0))) {
                move(s.charAt(0), world);
            }
        }
        String senit = "";
        for (int i = 0; i < moveList.length; i++) {
            senit += moveList[i];
        }
        ter.renderFrame(world.world);
        playGame(world, senit);
    }

    /*Load the avatar from the new Game stage.
    * Make call to  playGame that take active moves from
    * user*/
    private void loadAvatar(GameWorld world, GameWorld.Position position) {
        world.world[position.x][position.y] = Tileset.AVATAR;
        world.avatarPosition = position;
        ter.renderFrame(world.world);
        playGame(world, "");
    }

    /*Load a new game with the given settings. Set up new avatar*/
    private void newGame(long seed) {
        ter.initialize(WIDTH, HEIGHT, 0, -3);
        GameWorld world = new GameWorld(seed);
        String toSave = "N" + seed + "S";
        world.setSaveScript(toSave);
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world.world[x][y] = Tileset.NOTHING;
            }
        }
        world.buildManyRandomSq();
        GameWorld.Position avatarPosition = randomAvatarPosition(world);
        ter.renderFrame(world.world);
        loadAvatar(world, avatarPosition);
    }

    /*Randomly places the avatar position in the give map drawn*/
    private GameWorld.Position randomAvatarPosition(GameWorld world) {
        int xVal = world.getRand().nextInt(world.WIDTH - 1);
        int yVal = world.getRand().nextInt(world.HEIGHT - 1);
        if (world.getNumOfSq() > 0) {
            while (!(world.world[xVal][yVal].equals(Tileset.FLOOR))) {
                xVal = world.getRand().nextInt(world.WIDTH - 1);
                yVal = world.getRand().nextInt(world.HEIGHT - 1);
            }
            System.out.println(yVal);
            System.out.println("therethere");
            world.avatarPosition = new GameWorld.Position(xVal, yVal);
            return world.avatarPosition;
        }
        return new GameWorld.Position(xVal, yVal);
    }

    /*Take action method only for inputString
    * Handles all possible situations
    * New Game
    * Load Game
    * save Game
    * returns the play game IS method that in turn returns the tile[][]*/
    private TETile[][] takeActionIS(String input) {
        String[] arrofString = input.split("", input.length());
        String num = "";
        String moves = "";
        boolean seedComplete = false;
        boolean saveOrNot = false;
        String prevKey = null;
        String nextKey = null;

        for (int i = 0; i < arrofString.length; i++) {
            if (!(nextKey == null)) {
                prevKey = nextKey;
            }
            nextKey = arrofString[i];

            if (nextKey.equals("N") || nextKey.equals("n")) {
                System.out.println("");
            } else if (nextKey.equals("S") || nextKey.equals("s") && (!seedComplete)) {
                seedComplete = true;

            } else if (nextKey.equals("L") || nextKey.equals("l")) {
//                load previous game and start listening for next movements
                seedComplete = true;
                String[] all = readFile().split("");

                num = Long.toString(getSeedFromLoad(all));
                moves = getMoves(all);
            } else if (nextKey.equals("q")
                    || nextKey.equals("Q") && prevKey.equals(":")) {
                //save the current game's moves by saveFile
                saveOrNot = true;
                return playGameIS(num, moves, saveOrNot);
            } else if (!seedComplete) {
                num += nextKey;
            } else if (nextKey.equals(":")) {
                continue;
            } else {
                moves += nextKey;
            }
        }
        return playGameIS(num, moves, saveOrNot);
    }


    /*Make the valid moves inherited by the takeAction method*/
    private TETile[][] playGameIS(String num, String moves, boolean saveOrNot) {
        Long seed = Long.parseLong(num);
        GameWorld world = new GameWorld(seed);
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world.world[x][y] = Tileset.NOTHING;
            }
        }
        String[] moves1 = moves.split("");
        world.buildManyRandomSq();
        if (world.getNumOfSq() <= 0) {
            System.out.println("the");
            return  world.world;
        }
        world.avatarPosition = randomAvatarPosition(world);
        world.world[world.avatarPosition.x][world.avatarPosition.y] = Tileset.AVATAR;
        System.out.println("therethere");
        String senit = "N" + num + "S";
        for (String s: moves1) {
            if (s.equals("")) {
                continue;
            } else if (validMove(world, s.charAt(0))) {
                move(s.charAt(0), world);
                senit += s;
            }
        }
        String toSave = senit;
        world.setSaveScript(toSave);
        if (saveOrNot) {
            saveFile(world);
        }
        return world.world;
    }

    /*Decide the next course of action
    * options include loading a new game, quiting and loading a previous
    * game. */
    private void takeAction(int n) {
        Character prevKey = null;
        Character nextKey = null;
        while (n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                if (!(nextKey == null)) {
                    prevKey = nextKey;
                }
                nextKey = StdDraw.nextKeyTyped();
                if (nextKey.equals('N') || nextKey.equals('n')) {
//                    System.out.println("here");
                    Long seed = getSeedSequence();
                    newGame(seed);
                    return;
                } else if (nextKey.equals('Q')
                        || nextKey.equals('q') && prevKey.equals(':')) {
                    return;
                } else if (nextKey.equals('L') || nextKey.equals('l')) {
                    loadOldGame();
                }
//                } else if (nextKey.equals('P') || nextKey.equals('p')) {
////                    naming thing about the avatar
//                }
            }
        }
    }

    /*puts ne display on the screen and takes the new input as the seed*/
    private  Long getSeedSequence() {
        String s1 = "Please enter seed and end with 'S'";
        boolean getin = false;
        drawFrame(20, 20, s1, true);
        String seedstr = "";
        while (!getin) {
            if (StdDraw.hasNextKeyTyped()) {
                drawFrame(20, 20, "", true);
                Character ch = StdDraw.nextKeyTyped();
                if ((ch.equals('S') || ch.equals('s'))) {
                    return Long.parseLong(seedstr);
                }
                seedstr += ch;
                drawFrame(20, 20, seedstr, false);
            }
        }
        return Long.parseLong(seedstr);
    }
    /*Draws frame of current string*/
    public void drawFrame(int x, int y, String s, boolean k) {
        if (!k) {
            Font font1 = new Font("Monaco", Font.BOLD, 15);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setFont(font1);
            int centX = x;
            int centY = y;
            StdDraw.text(centX, centY, s);
            StdDraw.show();
        } else {
            StdDraw.clear(Color.BLACK);
            StdDraw.show();
            drawFrame(x, y, s, false);

        }
    }
    private void startSequence() {

        String s1 = "CS61B: The Game";
        String s2 = "New Game: N";
        String s3 = "Load Game: L";
        String s4 = "Quit : Q";
        Font font1 = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(20, 20, s1);
        StdDraw.text(20, 10, s2);
        StdDraw.text(20, 12, s3);
        StdDraw.text(20, 14, s4);
        StdDraw.show();
//        drawFrame(20, 5 , s4, false);
//        drawFrame(20,6, s2, false);
//        drawFrame(20, 20, s1, false);
//        drawFrame(20, 7, s3, false);


//        HUD still needed
//        while (true) {
//            Double mos = edu.princeton.cs.introcs.StdDraw.mouseX();
//            drawFrame(5,5,Double.toString(mos), true);
//        }
    }

    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT, 0, 0);
        startSequence();
        takeAction(10);
//

    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//
//        Long result = Long.parseLong(input);
//        long seed = result;
//        GameWorld world = new GameWorld(seed);
//        for (int x = 0; x < WIDTH; x += 1) {
//            for (int y = 0; y < HEIGHT; y += 1) {
//                world.world[x][y] = Tileset.NOTHING;
//            }
//        }
//        world.buildManyRandomSq();
////        ter.renderFrame(world.world);
//        return world.world;
        return takeActionIS(input);
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
//        TERenderer ter = new TERenderer();
//        ter.initialize(WIDTH, HEIGHT);
//        String str = input;
//        String[] arrofString = str.split("", input.length());
//        String num = "";
//        for (int i = 0; i < arrofString.length; i++) {
//            if ((i == 0 && arrofString[i].equals("N") || arrofString[i].equals("n"))) {
//                continue;
//            } else if (i == arrofString.length - 1) {
//                if (arrofString[i].equals("S") || arrofString[i].equals("s")) {
//                    continue;
//                }
//            } else {
//                num += arrofString[i];
//            }
//        }
//
//        Long result = Long.parseLong(num);
//        long seed = result;
//        GameWorld world = new GameWorld(seed);
//        for (int x = 0; x < WIDTH; x += 1) {
//            for (int y = 0; y < HEIGHT; y += 1) {
//                world.world[x][y] = Tileset.NOTHING;
//            }
//        }
//        world.buildManyRandomSq();
////        ter.renderFrame(world.world);
//        return world.world;

    }
}
