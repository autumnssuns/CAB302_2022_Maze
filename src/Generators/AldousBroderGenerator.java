package Generators;

import Models.Maze;
import Models.MazeNode;

import java.util.ArrayList;
import java.util.Random;

public class AldousBroderGenerator extends Generator{
    private final ArrayList<MazeNode> visited, unvisited;

    public AldousBroderGenerator(Maze maze) {
        super(maze);
        visited = new ArrayList<>();
        unvisited = new ArrayList<>();
        for (MazeNode node : maze){
            unvisited.add(node);
        }
    }

    @Override
    public void generate() {
        MazeNode current = unvisited.remove(rnd.nextInt(unvisited.size()));
        visited.add(current);
        while (!unvisited.isEmpty()){
            ArrayList<MazeNode> neighbours = getNeighbours(current);
            MazeNode random = neighbours.get(rnd.nextInt(neighbours.size()));
            if (!visited.contains(random)){
                current.connect(random);
                addFrame(random);
                unvisited.remove(random);
                visited.add(random);
            }
            current = random;
        }
    }
}
