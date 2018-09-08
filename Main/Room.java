package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private int height;
    private int width;
    private int startX;
    private int startY;
    private Map<String, int[][]> map;
    public Room(int height, int width, int startX, int startY) {
        this.height = height;
        this.width = width;
        this.startX = startX;
        this.startY = startY;
    }
    private void fill(TETile[][] matrix) {
        if (startX <= 0 || startX >= matrix.length - 1 || startY <= 0
                || startY >= matrix[0].length - 1
                || this.height <= 0 || this.width <= 0) {
            return;
        }
        for (int i = 1; i < height; i++) {
            for (int j = 1; j < width; j++) {
                matrix[startX - ((width / 2) + (width % 2)) + j][startY + i] = Tileset.FLOOR;
            }
        }
    }

    //Value should be a 2D array, and then simply populate the 2D array during the outline
    private Map<String, int[][]> outline(TETile[][] tiles) {
        adjust(tiles); map = new HashMap<>();
        int[][] top = new int[width][2]; int[][] right = new int[height][2];
        int[][] left = new int[height][2];
        int[][] bottom = new int[width][2]; int xtracker = startX;
        int tracker1 = 0; int tracker2 = 0; int tracker3 = 0; int tracker4 = 0;
        if (startX <= 0 || startX >= tiles.length - 1 || startY <= 0
                || startY >= tiles[0].length - 1 || this.height <= 0 || this.width <= 0) {
            map.put("top", resize(top, tracker3));
            map.put("right", resize(right, tracker2));
            map.put("left", resize(left, tracker4));
            map.put("bottom", resize(bottom, tracker1));
            return map;
        }
        for (int i = 0; i < width / 2; i++) {
            if (!isWall(tiles[i + startX][startY]) && !isFloor(tiles[i + startX][startY])) {
                if (i != width / 2 - 1) {
                    bottom[tracker1][0] = i + startX;
                    bottom[tracker1][1] = startY;
                    tracker1 += 1;
                }
                tiles[i + startX][startY] = Tileset.WALL;
            }
            xtracker += 1;
        }
        startX = xtracker; int ytracker = startY;
        for (int i = 0; i < height; i++) {
            if (!isWall(tiles[startX][i + startY]) && !isFloor(tiles[startX][i  + startY])) {
                tiles[startX][i + startY] = Tileset.WALL;
                if (i != 0 && i != height - 1) {
                    right[tracker2][0] = startX;
                    right[tracker2][1] = i + startY;
                    tracker2 += 1;
                }
            }
            ytracker += 1;
        }
        startY = ytracker;
        for (int i = 0; i < width; i++) {
            if (!isWall(tiles[startX - i][startY]) && !isFloor(tiles[startX - i][startY])) {
                tiles[startX - i][startY] = Tileset.WALL;
                if (i != 0 && i != width - 1) {
                    top[tracker3][0] = startX - i;
                    top[tracker3][1] = startY;
                    tracker3 += 1;
                }
            }
            xtracker -= 1;
        }
        startX = xtracker;
        for (int i = 0; i < height; i++) {
            if (!isWall(tiles[startX][startY - i]) && !isFloor(tiles[startX][startY - i])) {
                tiles[startX][startY - i] = Tileset.WALL;
                if (i != 0 && i != height - 1) {
                    left[tracker4][0] = startX;
                    left[tracker4][1] = startY - i;
                    tracker4 += 1;
                }
            }
            ytracker -= 1;
        }
        startY = ytracker;
        for (int i = 0; i < width / 2 + width % 2; i++) {
            if (!isWall(tiles[i + startX][startY]) && !isFloor(tiles[i + startX][startY])) {
                if (i != 0 && i != width / 2 - 1) {
                    bottom[tracker1][0] = i + startX;
                    bottom[tracker1][1] = startY;
                    tracker1 += 1;
                }
                tiles[i + startX][startY] = Tileset.WALL;
            }
            xtracker += 1;
        }

        startX = xtracker;
        map.put("top", resize(top, tracker3)); map.put("right", resize(right, tracker2));
        map.put("left", resize(left, tracker4));
        map.put("bottom", resize(bottom, tracker1));
        return map;
    }

    public boolean isWall(TETile tile) {
        return tile.description().equals(Tileset.WALL.description());
    }

    public boolean isFloor(TETile tile) {
        return tile.description().equals(Tileset.FLOOR.description());
    }

    public Map<String, int[][]> drawRoom(TETile[][] matrix) {
        Map<String, int[][]> map2 = outline(matrix);
        fill(matrix);
        return map2;
    }

    private boolean isOutW(TETile[][] matrix) {
        return this.width / 2 + width % 2 + startX >= matrix.length
                || startX - this.width / 2 - width % 2 < 0;
    }

    private boolean isOutH(TETile[][] matrix) {
        return this.height + startY >= matrix[0].length || this.height + startY <= 0;
    }

    private void adjust(TETile[][] matrix) {
        while ((isOutH(matrix) || isOutW(matrix)) && this.height > 0 && this.width > 0) {
            if (isOutH(matrix)) {
                this.height -= 1;
            }
            if (isOutW(matrix)) {
                this.width -= 1;
            }
        }
    }

    private int[][] resize(int[][] row, int tracker) {
        int[][] temp = new int[tracker][];
        for (int i = 0; i < tracker; i++) {
            temp[i] = row[i];
        }
        return temp;
    }
}
