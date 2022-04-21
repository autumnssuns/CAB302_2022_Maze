package Generators;

import Models.Maze;
import Models.MazeNode;

import java.util.Random;

import static java.util.stream.IntStream.range;

// Adapted from https://github.com/armin-reichert/mazes/blob/master/mazes-algorithms/src/main/java/de/amr/maze/alg/others/RecursiveDivision.java
public class RecursiveDivisionGenerator extends Generator{
    private static final int HORIZONTAL = 0, VERTICAL = 1;

    public RecursiveDivisionGenerator(Maze maze) {
        super(maze);
    }

    @Override
    public void generate() {
        maze.connectAll();
        int width = maze.getSize(1);
        int height = maze.getSize(0);
        divide(0, 0, width, height);
    }

    private void divide(int x0, int y0, int width, int height){
        if (width <= 1 && height <= 1) return;
        if (width < height || (width == height && rnd.nextBoolean())) {
            // Build "horizontal wall" at random y from [y0 + 1, y0 + h - 1], keep random door
            int y = y0 + 1 + rnd.nextInt(height - 1);
            int door = x0 + rnd.nextInt(width);

            range(x0, x0 + width).filter(x -> x != door).forEach(x -> {
                MazeNode node = maze.get(y - 1, x);
                MazeNode other = maze.get(y, x);
                if (node != null && other != null) {
                    node.disconnect(other);
                    addFrame(node);
                }
            });

            divide(x0, y0, width, y - y0);
            divide(x0, y, width, height - (y - y0));
        } else {
            // Build "vertical wall" at random x from [x0 + 1, x0 + w - 1], keep random door
            int x = x0 + 1 + rnd.nextInt(width - 1);
            int door = y0 + rnd.nextInt(height);
            range(y0, y0 + height).filter(y -> y != door).forEach(y -> {
                MazeNode node = maze.get(y, x - 1);
                MazeNode other = maze.get(y, x);
                if (node != null && other != null) {
                    node.disconnect(other);
                    addFrame(node);
                }
            });
            divide(x0, y0, x - x0, height);
            divide(x, y0, width - (x - x0), height);
        }
    }
}
