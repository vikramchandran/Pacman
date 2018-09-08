package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Hallway {
    private int length;
    private int middleX;
    private int middleY;
    public Hallway(int length, int startx, int starty) {
        this.length = length;
        this.middleX = startx;
        this.middleY = starty;
    }

    public int[] drawHorizontalRight(TETile[][] matrix, int adjoin) {
        if (middleX >= matrix.length - 1 || middleY >= matrix[0].length - 1 || middleY <= 0) {
            return new int[] {1, 1};
        }
        for (int x = 0; x < length; x++) {
            if (shouldStop(x, true, matrix)) {
                matrix[middleX + x][middleY] = Tileset.FLOOR;
                return new int[] {0, 0};
            }
            matrix[middleX + x][middleY] = Tileset.FLOOR;
            matrix[middleX + x][middleY + 1] = Tileset.WALL;
            matrix[middleX + x][middleY - 1] = Tileset.WALL;
            if (isEnd(middleX + x, middleY, matrix)) {
                matrix[middleX + x][middleY] = Tileset.WALL;
                return new int[] {0, 0};
            }
        }
        if (adjoin == 0) {
            matrix[middleX + length - 1] [middleY] = Tileset.WALL;
            return new int[] {middleX + length - 2, middleY + 1};
        }
        if (adjoin == 1) {
            matrix[middleX + length - 1] [middleY] = Tileset.WALL;
            return new int[] {middleX + length - 2, middleY - 1};
        }
        return new int[] {middleX + length - 1, middleY};
    }
    public int[] drawHorizontalLeft(TETile[][] matrix, int adjoin) {
        if (middleX >= matrix.length - 1 || middleY >= matrix[0].length - 1 || middleY <= 0) {
            return new int[] {1, 1};
        }
        for (int x = 0; x > -length; x--) {
            if (shouldStop(x, true, matrix)) {
                matrix[middleX + x][middleY] = Tileset.FLOOR;
                return new int[] {0, 0};
            }
            matrix[middleX + x][middleY] = Tileset.FLOOR;
            matrix[middleX + x][middleY + 1] = Tileset.WALL;
            matrix[middleX + x][middleY - 1] = Tileset.WALL;
            if (isEnd(middleX + x, middleY, matrix)) {
                matrix[middleX + x][middleY] = Tileset.WALL;
                return new int[] {0, 0};
            }
        }
        if (adjoin == 0) {
            matrix[middleX - length + 1] [middleY] = Tileset.WALL;
            return new int[] {middleX - length + 2, middleY + 1};
        }
        if (adjoin == 1) {
            matrix[middleX - length + 1] [middleY] = Tileset.WALL;
            return new int[] {middleX - length + 2, middleY - 1};
        }
        return new int[] {middleX - length + 1, middleY};
    }

    public int[] drawHallwayVertUp(TETile[][] matrix, int adjoin) {
        if (middleX >= matrix.length - 1  || middleY >= matrix[0].length - 1 || middleX <= 0) {
            return new int[] {1, 1};
        }
        for (int x = 0; x < length; x++) {
            if (shouldStop(x, false, matrix)) {
                matrix[middleX][middleY + x] = Tileset.FLOOR;
                return new int[] {0, 0};
            }
            matrix[middleX][middleY + x] = Tileset.FLOOR;
            matrix[middleX + 1][middleY + x] = Tileset.WALL;
            matrix[middleX - 1][middleY + x] = Tileset.WALL;
            if (isEnd(middleX, middleY + x, matrix)) {
                matrix[middleX][middleY + x] = Tileset.WALL;
                return new int[] {0, 0};
            }
        }
        if (adjoin == 0) {
            matrix[middleX] [middleY + length - 1] = Tileset.WALL;
            return new int[] {middleX + 1, middleY + length - 2};
        }
        if (adjoin == 1) {
            matrix[middleX] [middleY + length - 1] = Tileset.WALL;
            return new int[] {middleX - 1, middleY + length - 2};
        }
        return new int[] {middleX, middleY + length - 1};
    }

    public int[] drawHallwayVertDown(TETile[][] matrix, int adjoin) {
        if (middleX >= matrix.length - 1  || middleY >= matrix[0].length - 1 || middleX <= 0) {
            return new int[] {1, 1};
        }
        for (int x = 0; x > -length; x--) {
            if (shouldStop(x, false, matrix)) {
                matrix[middleX][middleY + x] = Tileset.FLOOR;
                return new int[] {0, 0};
            }
            matrix[middleX][middleY + x] = Tileset.FLOOR;
            matrix[middleX - 1][middleY + x] = Tileset.WALL;
            matrix[middleX + 1][middleY + x] = Tileset.WALL;
            if (isEnd(middleX, middleY + x, matrix)) {
                matrix[middleX][middleY + x] = Tileset.WALL;
                return new int[] {0, 0};
            }
        }
        if (adjoin == 0) {
            matrix[middleX] [middleY - length + 1] = Tileset.WALL;
            return new int[] {middleX + 1, middleY - length + 2};
        }
        if (adjoin == 1) {
            matrix[middleX] [middleY - length + 1] = Tileset.WALL;
            return new int[] {middleX - 1, middleY - length + 2};
        }
        return new int[] {middleX, middleY - length + 1};
    }

    public boolean isEnd(int x, int y, TETile[][] matrix) {
        int ylength = matrix[0].length - 1;
        int xlength = matrix.length - 1;
        return x == 0 || y == 0 || y == ylength || x == xlength;
    }

    public boolean isWall(TETile tile) {
        return tile.description().equals(Tileset.WALL.description());
    }

    public boolean isFloor(TETile tile) {
        return tile.description().equals(Tileset.FLOOR.description()); }

    public boolean shouldStop(int index, boolean orien, TETile[][] matrix) {
        if (orien) {
            return ((isWall(matrix[middleX + index][middleY + 1])
                    && isWall(matrix[middleX + index][middleY - 1]))
                || (isFloor(matrix[middleX + index][middleY + 1])
                    || isFloor(matrix[middleX + index][middleY - 1])
                    || isFloor(matrix[middleX + index][middleY]))) && (index != 0);
        }
        return ((isWall(matrix[middleX + 1][middleY + index])
                && isWall(matrix[middleX - 1][middleY + index]))
                || (isFloor(matrix[middleX + 1][middleY + index])
                || isFloor(matrix[middleX - 1][middleY + index])
                || isFloor(matrix[middleX][middleY + index]))) && (index != 0);
    }
}
