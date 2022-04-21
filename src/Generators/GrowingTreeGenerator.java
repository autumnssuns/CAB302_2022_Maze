package Generators;
import Models.Maze;
import Models.MazeNode;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
// Adapted from https://github.com/armin-reichert/mazes/blob/5cfebb6fbacf0bbc7266cd2bf3896d1e17a4c519/mazes-simplified/src/main/java/de/amr/mazes/simple/MazeAlgorithms.java#L158
public class GrowingTreeGenerator extends Generator{

    public GrowingTreeGenerator(Maze maze) {
        super(maze);
    }

    public void createMaze(MazeNode startVertex) {
        Random rnd = new Random();
        ArrayList<MazeNode> visited = new ArrayList<>();
        ArrayList<MazeNode> vertices = new ArrayList<>();
        vertices.add(startVertex);
        do {
            int index = rnd.nextBoolean() ? vertices.size() - 1 : rnd.nextInt(vertices.size());
            MazeNode vertex = vertices.remove(index);

            Stack<Integer> neighborIndex = new Stack<>();
            while (neighborIndex.size() < 4){
                int next = rnd.nextInt(4);
                if (!neighborIndex.contains(next))  neighborIndex.add(next);
            }

            for (int idx : neighborIndex) {
                if (idx >= getNeighbours(vertex).size()) continue;
                MazeNode neighbor = getNeighbours(vertex).get(idx);

                if (neighbor != null && !visited.contains(neighbor)) {
                    vertex.connect(neighbor);
                    addFrame(vertex);
                    vertices.add(neighbor);
                    visited.add(vertex);
                    visited.add(neighbor);
                }
            }
        } while (!vertices.isEmpty());
    }

    @Override
    public void generate() {
        createMaze(maze.get(rnd.nextInt(maze.getSize())));
    }
}
