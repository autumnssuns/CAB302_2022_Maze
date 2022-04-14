package Generators;

import Models.Maze;

public class EmptyGenerator extends Generator{
    public EmptyGenerator(Maze maze) {
        super(maze);
    }

    @Override
    public void generate() {
        maze.connectAll();
    }
}
