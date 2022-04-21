package Generators;

import Models.Maze;
import Models.MazeNode;
import Models.StateFrame;

import java.util.ArrayList;
import java.util.Random;

public abstract class Generator {
    protected Maze maze;
    protected ArrayList<StateFrame> states;
    protected Random rnd;

    public Generator(Maze maze){
        states = new ArrayList<>();
        this.maze = maze;
        rnd = new Random();
    }

    public void addFrame(MazeNode node){
        states.add(new StateFrame(node));
    }

    public ArrayList<StateFrame> getFrames(){
        return states;
    }

    public abstract void generate();

    protected ArrayList<MazeNode> getNeighbours(MazeNode node){
        ArrayList<MazeNode> neighbours = new ArrayList<>();
        int row = node.getRow();
        int col = node.getCol();
        if (row > 0) neighbours.add(maze.get( row - 1, col));
        if (row < maze.getSize(0) - 1) neighbours.add(maze.get(row + 1, col));
        if (col > 0) neighbours.add(maze.get(row, col - 1));
        if (col < maze.getSize(1) - 1) neighbours.add(maze.get(row, col + 1));
        return neighbours;
    }
}
