package Generators;

import Models.Maze;

public record GeneratorFactory(Maze maze) {
    public static final String[] ALGORITHMS = {
            "None",
            "Depth first traversal",
            "Aldous-Broder",
            "Recursive Division"
    };

    public static final int NO_ALGORITHM = 0;
    public static final int DEPTH_FIRST_TRAVERSAL = 1;
    public static final int ALDOUS_BRODER = 2;
    public static final int RECURSIVE_DIVISION = 3;

    private static final int DEFAULT = NO_ALGORITHM;

    public static boolean isCreative(int type){
        return type == NO_ALGORITHM
                || type == RECURSIVE_DIVISION;
    };

    public Generator create(int type) {
        return switch (type) {
            case NO_ALGORITHM -> new EmptyGenerator(maze);
            case DEPTH_FIRST_TRAVERSAL -> new DFSGenerator(maze);
            case ALDOUS_BRODER -> new AldousBroderGenerator(maze);
            case RECURSIVE_DIVISION -> new RecursiveDivisionGenerator(maze);
            default -> create(DEFAULT);
        };
    }
}
