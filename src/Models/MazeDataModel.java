package Models;

public record MazeDataModel(int idx, String name, String author, String description, int rowsCount, int colsCount, MazeNodeDataModel mazeNodes) {
}