package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;


public class Player {
    int startX;
    int startY;
    final TETile player = Tileset.FLOWER;
    public static final int WIDTH = Game.WIDTH;
    public static final int HEIGHT = Game.HEIGHT;
    static TETile[][] matrix;
    int count;
    char prev = ' ';
    boolean won = false;
    boolean end = false;
    public Player(int x, int y, TETile[][] matrix) {
        startX = x;
        startY = y;
        this.matrix = matrix;
        count = 0;
    }

    //Rendering will be all done in game.



    //Player should only be able to move within the Tileset.Floor,
    // basically not going past hte walls
    //Different if's/switch's.
    //Update should return a Tilset
    public int[] update(char c) {
        if (!isWall(matrix[startX][startY])) {
            switch (c) {
                case 'w':
                    prev = c;
                    return new int[]{startX, startY + 1};
                case 's':
                    prev = c;
                    return new int[]{startX, startY - 1};
                case 'a':
                    prev = c;
                    return new int[]{startX - 1, startY};
                case 'd':
                    prev = c;
                    return new int[]{startX + 1, startY};
                case 'i':
                    prev = 'w';
                    return new int[]{startX, startY + 1};
                case 'k':
                    prev = 's';
                    return new int[]{startX, startY - 1};
                case 'j':
                    prev = 'a';
                    return new int[]{startX - 1, startY};
                case 'l':
                    prev = 'd';
                    return new int[]{startX + 1, startY};
                case 'g':
                    return jump();
                case 'h':
                    return jump();
                default: return new int[]{startX, startY};
            }
        }
        return new int[]{startX, startY};
    }


    public boolean move(char c) {
        int[] temp = update(c);
        if (temp[0] < 0 || temp[0] > matrix.length - 1
                || temp[1] < 0 || temp[1] > matrix[0].length - 1) {
            return true;
        }
        boolean tile = temp[0] == startX && temp[1] == startY;
        TETile tile1 = matrix[temp[0]][temp[1]];
        if (!isWall(tile1) && !isotherTile(tile1)) {
            matrix[startX][startY] = Tileset.NOTHING;
            matrix[temp[0]][temp[1]] = player;
            end = tile1.description().equals(Tileset.WATER.description());
            startX = temp[0];
            startY = temp[1];
        }
        if (isTree(tile1)) {
            count++;
        }
        won = count == 4;
        return tile;
    }

    public boolean isWall(TETile tile) {
        return tile.description().equals(Tileset.WALL.description());
    }

    public boolean isFloor(TETile tile) {
        return tile.description().equals(Tileset.FLOOR.description());
    }

    public void movewithclick() {
        if (StdDraw.isMousePressed()) {
            int xcord = (int) Math.round(StdDraw.mouseX());
            int ycord = (int) Math.round(StdDraw.mouseY()) - 5;
            if (xcord < 0 || xcord > matrix.length - 1
                    || ycord < 0 || ycord > matrix[0].length - 1) {
                return;
            }
            if (!isWall(matrix[xcord][ycord])) {
                matrix[startX][startY] = Tileset.NOTHING;
                matrix[xcord][ycord] = player;
                startX = xcord;
                startY = ycord;
            }
        }
    }

    public boolean isotherTile(TETile tile) {
        return tile.description().equals(Tileset.FLOWER.description());
    }
    public int[] jump() {
        if (prev == 'w') {
            if (isWall(matrix[startX][startY + 1]) && !isWall(matrix[startX][startY + 2])) {
                return new int[]{startX, startY + 2};
            }
        }
        if (prev == 'a') {
            if (isWall(matrix[startX - 1][startY]) && !isWall(matrix[startX - 2][startY])) {
                return new int[]{startX - 2, startY};
            }
        }
        if (prev == 's') {
            if (isWall(matrix[startX][startY - 1]) && !isWall(matrix[startX][startY - 2])) {
                return new int[]{startX, startY - 2};
            }
        }
        if (prev == 'd') {
            if (isWall(matrix[startX + 1][startY]) && !isWall(matrix[startX + 2][startY])) {
                return new int[]{startX + 2, startY};
            }
        }
        return new int[]{startX, startY};
    }
    private boolean isNothing(TETile tile) {
        return tile.description().equals(Tileset.NOTHING.description());
    }
    private boolean isTree(TETile tile) {
        return tile.description().equals(Tileset.TREE.description());
    }
}
