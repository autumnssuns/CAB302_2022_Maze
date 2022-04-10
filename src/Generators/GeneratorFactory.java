package Generators;

import Models.Maze;

public record GeneratorFactory(Maze maze) {
    public static final int DEPTH_FIRST_TRAVERSAL = 0;
    public static final int ALDOUS_BRODER = 1;
    public static final int RECURSIVE_DIVISION = 2;

    private static final int DEFAULT = DEPTH_FIRST_TRAVERSAL;

    public Generator create(int type) {
        return switch (type) {
            case DEPTH_FIRST_TRAVERSAL -> new DFSGenerator(maze);
            case ALDOUS_BRODER -> new AldousBroderGenerator(maze);
            case RECURSIVE_DIVISION -> new RecursiveDivisionGenerator(maze);
            default -> create(DEFAULT);
        };
    }
}
