package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;


public class Game implements java.io.Serializable {
    transient TERenderer ter = new TERenderer();

    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    private Long SEED;
    private transient TETile[][] saved = instSaved(new TETile[WIDTH][HEIGHT - 5]);
    private transient Player player1 = null;
    private transient Player player2 = null;
    private transient TETile[][] plane = new TETile[WIDTH][HEIGHT - 5];
    private Random rand;
    private String commands = "";
    private boolean play = false;
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.clear(Color.black);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering();
        ter.initialize(WIDTH, HEIGHT, 0, 5);
        boolean run = true;
        while (run) {
            char temp = ' ';
            drawMenu();
            if (StdDraw.hasNextKeyTyped()) {
                temp = StdDraw.nextKeyTyped();
            }
            if (temp == 'n' && !play) {
                SEED = inputSeed();
                commands += "n" + SEED + "s";
                rand = new Random(SEED);
                Draw d = new Draw(SEED, saved);
                d.drawWorld();
                Tree t = new Tree();
                t.populate(saved);
                int[] cor = placePlayer(saved);
                int[] cor1 = placePlayer(saved);
                player1 = new Player(cor[0], cor[1], saved);
                player2 = new Player(cor1[0], cor1[1], saved);
                play = true;
                instPlane();
            } else if (temp == 'l') {
                load();
            } else if (temp == 'q') {
                System.exit(0);
            }

            if (play) {
                play();
                serialize();
                StdDraw.pause(5000);
                System.exit(0);
            }
            StdDraw.clear(Color.orange);
            StdDraw.clear(Color.black);
        }
        StdDraw.clear(Color.black);
    }


    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        if (input.length() == 0) {
            return saved;
        }
        if (input.charAt(0) == 'n') {
            commands += 'n';
            String[] temp = convert(input.substring(1));
            SEED = Long.parseLong(temp[0]);
            commands += SEED + "s";
            input = temp[1];
            saved = new TETile[WIDTH][HEIGHT - 5 ];
            Draw d = new Draw(SEED, saved);
            d.drawWorld();
            rand = new Random(SEED);
            int[] cor = placePlayer(saved);
            player1 = new Player(cor[0], cor[1], saved);
        }
        if (input.charAt(0) == 'l') {
            return playWithInputString(deserialize().commands + input.substring(1));
        }
        if (player1 != null) {
            input = playS(input);
        }
        if (input.length() > 0 && input.charAt(0) == 'n') {
            return playWithInputString(input);
        }
        return saved;
    }

    private String playS(String input) {
        boolean quit = false;
        int count = 0;
        for (char c : input.toCharArray()) {
            count++;
            if (c == 'w' || c == 's' || c == 'a' || c == 'd') {
                commands += c;
                player1.move(c);
            }
            if (c == 'q' && quit) {
                serialize();
                break;
            }
            quit = ':' == c;
        }
        if (count <  input.length()) {
            return input.substring(count);
        }
        return " ";
    }

    public String[] convert(String input) {
        int index = 0;
        String inter = "";
        while (input.charAt(index) != 's') {
            inter += input.charAt(index);
            index += 1;
        }
        if (index + 1 > input.length() - 1) {
            return new String[] {inter, " "};
        }
        return new String[]{inter, input.substring(index + 1)};
    }


    public void drawMenu() {
        Font font1 = new Font("Ariel", Font.BOLD, 40);
        Font font2 = new Font("Ariel", Font.BOLD, 20);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(Color.gray);
        StdDraw.text(WIDTH / 2, HEIGHT - 5, "CS61B: The Game");
        StdDraw.setFont(font2);
        StdDraw.text(WIDTH / 2, HEIGHT  / 2, "New Game (N)");
        StdDraw.text(WIDTH  / 2, HEIGHT / 2 - 2,  "Load Game (L)");
        StdDraw.text(WIDTH  / 2, HEIGHT / 2 - 4, "Quit (Q)");
        StdDraw.show();
    }

    public Long inputSeed() {
        String message = "Enter Seed: ";
        String seed = "";
        char input = ' ';
        boolean temp = true;
        Font font1 = new Font("Ariel", Font.BOLD, 50);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(Color.green);
        while (temp) {
            drawFrame(message + seed);
            if (StdDraw.hasNextKeyTyped()) {
                input = StdDraw.nextKeyTyped();
                if (input == 's') {
                    return Long.parseLong(seed);
                }
                seed += input;
            }
            temp = 's' != input;
        }
        return Long.parseLong(seed);
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.black);
        Font font = new Font("Ariel", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }

    public void displayhud(TETile[][] matrix) {
        int xcord = (int) Math.round(StdDraw.mouseX());
        int ycord = (int) Math.round(StdDraw.mouseY()) - 5;
        Font font = new Font("Ariel", Font.PLAIN, 20);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(font);
        if (xcord <= 0 || ycord <= 0 || ycord >= HEIGHT - 6 || xcord >= WIDTH - 1) {
            return;
        }
        if (isWall(matrix[xcord][ycord])) {
            StdDraw.text(20, 3, "Touching Wall");
        } else if (isWater(matrix[xcord][ycord])) {
            StdDraw.text(20, 3, "Touching Water");

        } else if (isFloor(matrix[xcord][ycord])) {
            StdDraw.text(20, 3, "Touching Floor");
        }
    }

    public void displaypointsleft(Player p) {
        Font font = new Font("Ariel", Font.PLAIN, 20);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(font);
        StdDraw.text(20, 1, "Player 1: " + Integer.toString(p.count));
    }

    public void displaypointsright(Player p) {
        Font font = new Font("Ariel", Font.PLAIN, 20);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(font);
        StdDraw.text(60, 1, "Player 2: " + Integer.toString(p.count));
    }



    public boolean isWall(TETile tile) {
        return tile.description().equals(Tileset.WALL.description());
    }

    public boolean isWater(TETile tile) {
        return tile.description().equals(Tileset.WATER.description());
    }

    public boolean isFloor(TETile tile) {
        return tile.description().equals(Tileset.FLOOR.description());
    }
    private int[] placePlayer(TETile[][] matrix) {
        int randHeight = RandomUtils.uniform(rand, 5, HEIGHT - 5);
        int randWidth = RandomUtils.uniform(rand, 5, WIDTH);
        while (!matrix[randWidth][randHeight].description().equals(Tileset.FLOOR.description())) {
            randHeight = RandomUtils.uniform(rand, 5, HEIGHT - 5);
            randWidth = RandomUtils.uniform(rand, 5, WIDTH);
        }
        return new int[] {randWidth, randHeight};
    }

    private void radiance() {
        int xcor1 = player1.startX - 4;
        int ycor1 = player1.startY - 4;
        int xcor2 = player2.startX - 4;
        int ycor2 = player2.startY - 4;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if ((ycor1 + x < HEIGHT - 5 && ycor1 + x > 0)
                        && (xcor1 + y < WIDTH && y + xcor1 > 0)) {
                    plane[xcor1 + y][ycor1 + x] = saved[xcor1 + y][ycor1 + x];
                }
                if ((ycor2 + x < HEIGHT - 5 && ycor2 + x > 0)
                        && (xcor2 + y < WIDTH && y + xcor2 > 0)) {
                    plane[xcor2 + y][ycor2 + x] = saved[xcor2 + y][ycor2 + x];
                }
            }
        }
    }
    private void instPlane() {
        for (int i = 0; i < plane.length; i++) {
            for (int j = 0; j < plane[i].length; j++) {
                plane[i][j] = Tileset.NOTHING;
            }
        }
    }

    private boolean contains(char[] list, char c1) {
        for (char c : list) {
            if (c == c1) {
                return true;
            }
        }
        return false;
    }

    private TETile[][] instSaved(TETile[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = Tileset.WATER;
            }
        }
        return matrix;
    }
    //@Source https://www.tutorialspoint.com/java/java_serialization.htm
    private void serialize() {
        try {
            FileOutputStream fileOut = new FileOutputStream("savedGame.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("File not serialized");
            e.printStackTrace();
        }
    }
    private Game deserialize() {
        Game game = null;
        try {
            FileInputStream fileIn = new FileInputStream("savedGame.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            game = (Game) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.out.println("Intake error");
            i.printStackTrace();
            return game;
        } catch (ClassNotFoundException c) {
            System.out.println("Game object not found");
            c.printStackTrace();
            return game;
        }
        return game;
    }

    private void play() {
        char prev = ' ';
        char temp1 = ' ';
        boolean newChar = false;
        while (play) {
            char[] p2 = new char[]{'i', 'j', 'k', 'l', 'h'};
            char[] p1 = new char[]{'w', 'a', 's', 'd', 'g'};
            if (StdDraw.hasNextKeyTyped()) {
                temp1 = StdDraw.nextKeyTyped();
                newChar = true;
            }
            if (StdDraw.isMousePressed()) {
                plane[player1.startX][player1.startY] = Tileset.NOTHING;
                player1.movewithclick();
            }
            radiance();
            if (contains(p2, temp1) && newChar) {
                commands += temp1;
                player2.move(temp1);
                if (player2.end) {
                    play = false;
                    break;
                }
                if (player2.won) {
                    play = false;
                    break;
                }
                displaypointsleft(player1);
                displaypointsright(player2);
                displayhud(plane);
                StdDraw.show();
                ter.renderFrame(plane);

            }
            if (contains(p1, temp1) && newChar) {
                commands += temp1;
                if (player1.end) {
                    play = false;
                    break;
                }
                if (player1.won) {
                    play = false;
                    break;
                }
                player1.move(temp1);
                displaypointsleft(player1);
                displaypointsright(player2);
                displayhud(plane);
                StdDraw.show();
                ter.renderFrame(plane);

            }
            displaypointsleft(player1);
            displaypointsright(player2);
            displayhud(plane);
            StdDraw.show();
            ter.renderFrame(plane);
            play = !(prev == ':' && temp1 == 'q');
            prev = temp1;
            newChar = false;
        }
        StdDraw.clear(Color.orange);
        if (player1 != null && player2 != null
                && (player2.end || player2.won || player1.end || player1.won)) {
            while (!StdDraw.isMousePressed()) {
                endHow();
            }
            return;
        }
    }

    public void endplay1() {
        Font font1 = new Font("Ariel", Font.BOLD, 40);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(Color.gray);
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "Player 1 Lost!!");
        StdDraw.show();
    }

    public void endplay2() {
        Font font1 = new Font("Ariel", Font.BOLD, 40);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(Color.gray);
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "Player 2 Lost!!");
        StdDraw.show();
    }

    public void wonplay1() {
        Font font1 = new Font("Ariel", Font.BOLD, 40);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(Color.gray);
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "Player 1 Won!!");
        StdDraw.show();
    }

    public void wonplay2() {
        Font font1 = new Font("Ariel", Font.BOLD, 40);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(Color.gray);
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "Player 2 Won!!");
        StdDraw.show();
    }

    private void endHow() {
        if (player1.end) {
            endplay1();
        } else if (player1.won) {
            wonplay1();
        } else if (player2.end) {
            endplay2();
        } else {
            wonplay2();
        }
    }

    public void load() {
        Game prev = deserialize();
        String rest = "";
        char[] p2 = new char[]{'i', 'j', 'k', 'l', 'h'};
        char[] p1 = new char[]{'w', 'a', 's', 'd', 'g'};
        if (prev.commands.charAt(0) == 'n') {
            String[] temp = convert(prev.commands.substring(1));
            SEED = Long.parseLong(temp[0]);
            rest += temp[1];
            rand = new Random(SEED);
            Draw d = new Draw(SEED, saved);
            d.drawWorld();
            Tree t = new Tree();
            t.populate(saved);
            int[] cor = placePlayer(saved);
            int[] cor1 = placePlayer(saved);
            player1 = new Player(cor[0], cor[1], saved);
            player2 = new Player(cor1[0], cor1[1], saved);
            play = true;
            instPlane();
        }
        for (char c : rest.toCharArray()) {
            if (contains(p2, c)) {
                player2.move(c);
            }
            if (contains(p1, c)) {
                player1.move(c);
            }
        }
        play = true;
    }
}
