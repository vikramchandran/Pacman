package byog.Core;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Map;
import java.util.Random;

public class Draw {
    private TETile [][] matrix;
    private Random RANDOM;
    public Draw(Long seed, TETile[][] matrix) {
        RANDOM = new Random(seed);
        this.matrix = matrix;
    }
    public void fillWorld() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = Tileset.WATER;
            }
        }
    }
    public void drawWorld() {
        fillWorld();
        randomRoom(40, 15, 5, 5, 40);
    }
    // Create an orientation parameter, and depending on what kind of hallway, chooses corect key
    // Then chooses a random array from that key
    public int[] chooseStart(Map<String, int[][]> map, String orient) {
        for (String s: map.keySet()) {
            if (s.equals(orient)) {
                int[][] temp = map.get(s);
                if (temp.length == 0) {
                    return new int[] {0, 0};
                } else if (temp.length == 1) {
                    return new int[] {temp[0][0], temp[0][1]};
                } else {
                    int random = RandomUtils.uniform(RANDOM, temp.length);
                    return new int[] {temp[random][0], temp[random][1]};
                }
            }
        }
        return new int[] {0, 0};
    }

    public void randomRoom(int x, int y, int height, int width, int iter) {
        if (iter == 0 || x <= 1 || x >= matrix.length || y <= 1 || y >= matrix[0].length) {
            return;
        }
        Map<String, int[][]> map = new Room(height, width, x, y).drawRoom(matrix);
        String side1 = randomSide();
        String side2 = randomSide();
        String side3 = randomSide();
        String side4 = randomSide();
        int[] coordinates = chooseStart(map, side1);
        int[] coordinates2 = chooseStart(map, side2);
        int[] coordinates3 = chooseStart(map, side3);
        if (side1.equals("right")) {
            randomHallH(coordinates[0], coordinates[1],
                    RandomUtils.uniform(RANDOM, 10, 15), 1, iter - 1);
        } else if (side1.equals("left")) {
            randomHallH(coordinates[0], coordinates[1],
                    RandomUtils.uniform(RANDOM, 10, 15), 0, iter - 1);
        } else if (side1.equals("top")) {
            randomHallV(coordinates[0], coordinates[1],
                    RandomUtils.uniform(RANDOM, 10, 15), 0, iter - 1);
        } else {
            randomHallV(coordinates[0], coordinates[1],
                    RandomUtils.uniform(RANDOM, 10, 15), 1, iter - 1);
        }
        if (!side1.equals(side2)) {
            if (side2.equals("right")) {
                randomHallH(coordinates2[0], coordinates2[1],
                        RandomUtils.uniform(RANDOM, 10, 15), 1, iter - 1);
            } else if (side2.equals("left")) {
                randomHallH(coordinates2[0], coordinates2[1],
                        RandomUtils.uniform(RANDOM, 10, 15), 0, iter - 1);
            } else if (side2.equals("top")) {
                randomHallV(coordinates2[0], coordinates2[1],
                        RandomUtils.uniform(RANDOM, 10, 15), 0, iter - 1);
            } else {
                randomHallV(coordinates2[0], coordinates2[1],
                        RandomUtils.uniform(RANDOM, 10, 15), 1, iter - 1);
            }
        }
        if (!side3.equals(side2) && !side3.equals(side1)) {
            if (side3.equals("right")) {
                randomHallH(coordinates3[0], coordinates3[1],
                        RandomUtils.uniform(RANDOM, 10, 15), 1, iter - 1);
            } else if (side3.equals("left")) {
                randomHallH(coordinates3[0], coordinates3[1],
                        RandomUtils.uniform(RANDOM, 10, 15), 0, iter - 1);
            } else if (side3.equals("top")) {
                randomHallV(coordinates3[0], coordinates3[1],
                        RandomUtils.uniform(RANDOM, 10, 15), 0, iter - 1);
            } else {
                randomHallV(coordinates3[0], coordinates3[1],
                        RandomUtils.uniform(RANDOM, 10, 15), 1, iter - 1);
            }
        }
    }

    public void randomHallV(int x, int y, int length, int side, int iter) {
        if (iter == 0 || x <= 1 || y <= 1) {
            return;
        }
        int random = RANDOM.nextInt(3);
        int[] cor;
        Hallway hall = new Hallway(length, x, y);
        if (side == 0) {
            if (random == 0) {
                cor = hall.drawHallwayVertUp(matrix, 1);
            } else if (random == 1) {
                cor = hall.drawHallwayVertUp(matrix, 3);
            } else {
                cor = hall.drawHallwayVertUp(matrix, 0);
            }
        } else {
            if (random == 0) {
                cor = hall.drawHallwayVertDown(matrix, 1);
            } else if (random == 1) {
                cor = hall.drawHallwayVertDown(matrix, 3);
            } else {
                cor = hall.drawHallwayVertDown(matrix, 0);
            }
        }
        switch (random) {
            case 0: randomHallH(cor[0], cor[1], RandomUtils.uniform(RANDOM, 5, 10), 0, iter - 1);
                return;
            case 1: randomRoom(cor[0], cor[1], RandomUtils.uniform(RANDOM, 5, 10),
                    RandomUtils.uniform(RANDOM, 4, 8), iter - 1);
                return;
            case 2: randomHallH(cor[0], cor[1], RandomUtils.uniform(RANDOM, 5, 10), 1, iter - 1);
                return;
            default:  randomHallH(cor[0], cor[1], RandomUtils.uniform(RANDOM, 5, 10), 1, iter - 1);
        }

    }

    public void randomHallH(int x, int y, int length, int side, int iter) {
        if (iter == 0 || x <= 1 || y <= 1) {
            return;
        }
        int random = RANDOM.nextInt(3);
        int[] cor;
        Hallway hall = new Hallway(length, x, y);
        if (side == 0) {
            if (random == 0) {
                cor = hall.drawHorizontalLeft(matrix, 0);
            } else if (random == 1) {
                cor = hall.drawHorizontalLeft(matrix, 3);
            } else {
                cor = hall.drawHorizontalLeft(matrix, 1);
            }
        } else {
            if (random == 0) {
                cor = hall.drawHorizontalRight(matrix, 0);
            } else if (random == 1) {
                cor = hall.drawHorizontalRight(matrix, 3);
            } else {
                cor = hall.drawHorizontalRight(matrix, 1);
            }
        }

        switch (random) {
            case 0: randomHallV(cor[0], cor[1], RandomUtils.uniform(RANDOM, 5, 10), 0, iter - 1);
                return;
            case 1: randomRoom(cor[0], cor[1], RandomUtils.uniform(RANDOM, 5, 10),
                    RandomUtils.uniform(RANDOM, 4, 8), iter - 1);
                return;
            case 2: randomHallV(cor[0], cor[1], RandomUtils.uniform(RANDOM, 5, 10), 1, iter - 1);
                return;
            default: randomHallV(cor[0], cor[1], RandomUtils.uniform(RANDOM, 5, 10), 1, iter - 1);
        }
    }
    public String randomSide() {
        int random = RANDOM.nextInt(4);
        switch (random) {
            case 0: return "right";
            case 1: return "left";
            case 2: return "top";
            case 3: return "bottom";
            default: return "right";
        }
    }
}
