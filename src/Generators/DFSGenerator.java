package Generators;

import Models.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class DFSGenerator extends Generator {
    private Stack<MazeNode> pathStack;
    private ArrayList<MazeNode> visited;

    public DFSGenerator(Maze maze){
        super(maze);
        pathStack = new Stack<>();
        visited = new ArrayList<>();
        int startRow = rnd.nextInt(maze.getSize(0));
        int startCol = rnd.nextInt(maze.getSize(1));
        MazeNode startNode = maze.get(startRow, startCol);
        pathStack.add(startNode);
        visited.add(startNode);
    }

    public void generate(){
        while (!pathStack.isEmpty()){
            MazeNode node = pathStack.pop();

            ArrayList<MazeNode> neighbours = getNeighbours(node);

            neighbours.removeIf(n -> visited.contains(n));

            if (!neighbours.isEmpty()){
                pathStack.push(node);
                int randIdx = rnd.nextInt(neighbours.size());
                MazeNode selected = neighbours.get(randIdx);
                node.connect(selected);
                addFrame(node);
                visited.add(selected);
                pathStack.push(selected);
            }
        }
    }
}
