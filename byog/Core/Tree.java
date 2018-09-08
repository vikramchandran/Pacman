package byog.Core;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.Random;

public class Tree {
    final TETile object = Tileset.TREE;
    int number = 7;
    public static final int WIDTH = Game.WIDTH;
    public static final int HEIGHT = Game.HEIGHT;
    Random rand = new Random(90);

    public int[] position(TETile[][] matrix) {
        int height = HEIGHT - 5;
        int randHeight = RandomUtils.uniform(rand, 5, height);
        int randWidth = RandomUtils.uniform(rand, 5, WIDTH);
        while (!matrix[randWidth][randHeight].description().equals(Tileset.FLOOR.description())) {
            randHeight = RandomUtils.uniform(rand, 5, height);
            randWidth = RandomUtils.uniform(rand, 5, WIDTH);
        }
        return new int[] {randWidth, randHeight};
    }


    public void populate(TETile[][] matrix) {
        while (number > 0) {
            int[] cord = position(matrix);
            matrix[cord[0]][cord[1]] = object;
            number--;
        }
    }



    public boolean isFloor(TETile tile) {
        return tile.description().equals(Tileset.FLOOR.description());
    }


}
