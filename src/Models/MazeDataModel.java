package Models;

import java.time.LocalDateTime;

public record MazeDataModel(int idx, String name, String author, String description, LocalDateTime createdTime, LocalDateTime modifiedTime, int algorithm, long seed, int rowsCount, int colsCount, MazeNodeDataModel mazeNodes) {
    public Maze unpack(){
        Maze maze = new Maze(rowsCount, colsCount);
        maze.disconnectAll();
        mazeNodes.neighbours().forEach((x,y) -> mazeNodes.getNeighbours(x).forEach(n -> maze.get(x).connect(maze.get(n))));
        return maze;
    }
}