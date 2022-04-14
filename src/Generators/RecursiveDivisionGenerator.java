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
        Random rnd = new Random();
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

//    private void divide(Maze maze, int x, int y, int width, int height){
//        if (width < 2 && height < 2) return;
//
//        boolean horizontal = orientation == HORIZONTAL;
//
//        Random rand = new Random();
//
//        // Wall location
//        int wallX = x + (horizontal ? 0 : rand.nextInt(width - 1));
//        int wallY = y + (horizontal ? rand.nextInt(height - 1) : 0);
//
//        // Passage location
//        int passX = wallX + (horizontal ? rand.nextInt(width) : 0);
//        int passY = wallY + (horizontal ? 0 : rand.nextInt(height));
//
//        // Direction
//        int dx = horizontal ? 1 : 0;
//        int dy = horizontal ? 0 : 1;
//
//        // Length
//        int length = horizontal ? width : height;
//
//        // Draw a wall
//        for (int i = 0; i < length; i++){
//            if ((wallY > 0 && horizontal) || (wallX > 0 && !horizontal) && wallX < maze.getSize(1) && wallY < maze.getSize(0)){
//                MazeNode node = maze.get(wallY, wallX);
//                MazeNode other = horizontal ? maze.get(wallY - 1, wallX) : maze.get(wallY, wallX - 1);
//
////            MazeNode node = maze.get(wallY - 1, wallX - 1);
////            MazeNode other = horizontal ? maze.get(wallY, wallX)
//                if (wallX != passX || wallY != passY) node.disconnect(other);
//            }
//            wallX += dx;
//            wallY += dy;
//        }
//
//        int nx = x;
//        int ny = y;
//        int w = horizontal ? width : wallX - x + 1;
//        int h = horizontal ? wallY + y - 1 : height;
//        divide(maze, nx, ny, w, h, chooseOrientation(w, h));
//
//        nx = horizontal ? x : wallX + 1;
//        ny = horizontal ? wallY + 1 : y;
//        w = horizontal ? width : x + width - wallX - 1;
//        h = horizontal ? y + height - wallY - 1: height;
//        divide(maze, nx, ny, w, h, chooseOrientation(w, h));
//    }
}
