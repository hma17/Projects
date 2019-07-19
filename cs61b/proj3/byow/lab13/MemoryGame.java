package byow.lab13;

import byow.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.StdOut;
import edu.princeton.cs.introcs.Stopwatch;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
        }
        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
        return;
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        rand = new Random(seed);
        //TODO: Initialize random number generator
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        int currIndex;
        String st = "";
        while (n > 0) {
            currIndex = rand.nextInt(CHARACTERS.length);
            st += CHARACTERS[currIndex];
            n -= 1;
        }
        return st;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen;
        StdDraw.clear();
        Font font1 = new Font("Monaco", Font.BOLD, 30) ;
        StdDraw.setFont(font1);
        int centX = width / 2;
        int centY = height / 2;
        StdDraw.text(centX, centY, s);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.show();


    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        String[] str = letters.split("");
        for (int i = 0; i < str.length; i++) {
            Stopwatch sw = new Stopwatch();
            drawFrame(str[i]);
            boolean s = true;
            double time1 = 0.0;
            double time2;
            while (s) {
                time1 = sw.elapsedTime();
                if (time1 > 1.0) {
                    s = false;
                }
            }
            drawFrame("");
            while (!s) {
                time2 = sw.elapsedTime() - time1;
                if (time2 > 1.0) {
                    s = true;
                }
            }


        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String curr = "";
        System.out.println(n);
        while (n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                curr += Character.toString(StdDraw.nextKeyTyped());
                drawFrame(curr);
                n -= 1;
            }
        }
        return curr;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts

        //TODO: Establish Engine loop
//        TERenderer ter = new TERenderer();
//        ter.initialize(width, height);
//        round = 1;
//        boolean correct = true;
//        while (correct) {
//            drawFrame("Round " + round);
//            String currString = generateRandomString(round);
//            flashSequence(currString);
//            String ans = solicitNCharsInput(currString.length());
//            System.out.println(ans);
//            System.out.println(currString);
//            if (ans.equals(currString)) {
//                correct = true;
//                round += 1;
//            }
//            else {
//                drawFrame("Game Over! you made it to Round" + round);
//                correct = false;
//
//            }
//
//        }
        StdDraw.clear();

        StdDraw.filledCircle(20, 20, 2);
        StdDraw.show();

    }

}
