import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Generator {
    private Stack<MazeNode> pathStack;
    private ArrayList<MazeNode> visited;
    private Maze maze;

    public Generator(Maze maze){
        this.maze = maze;
        pathStack = new Stack<>();
        visited = new ArrayList<>();
        int startRow = new Random().nextInt(maze.getSize(0));
        int startCol = new Random().nextInt(maze.getSize(1));
        MazeNode startNode = maze.get(startRow, startCol);
        pathStack.add(startNode);
        visited.add(startNode);
    }

    public void generates(){
        for (int i = 0; i < maze.getSize(1) - 1; i++){
            maze.get(0, i).connect(maze.get(0, i + 1));
            System.out.println(maze.get(0,1).getRight());
        }
        for (int i = 0; i < maze.getSize(0) - 1; i++){
            maze.get(i, 0).connect(maze.get(i + 1, 0));
            System.out.println(maze.get(0,1).getBottom());
        }
    }

    public void generate(){

        while (!pathStack.isEmpty()){
            MazeNode node = pathStack.pop();
            int row = node.getRow();
            int col = node.getCol();

            ArrayList<MazeNode> neighbours = new ArrayList<>();

            if (row > 0) neighbours.add(maze.get( row - 1, col));
            if (row < maze.getSize(0) - 1) neighbours.add(maze.get(row + 1, col));
            if (col > 0) neighbours.add(maze.get(row, col - 1));
            if (col < maze.getSize(1) - 1) neighbours.add(maze.get(row, col + 1));

            neighbours.removeIf(n -> visited.contains(n));

            if (!neighbours.isEmpty()){
                pathStack.push(node);
                int randIdx = new Random().nextInt(neighbours.size());
                MazeNode selected = neighbours.get(randIdx);
                node.connect(selected);
                visited.add(selected);
                pathStack.push(selected);

//                int randIdx = new Random().nextInt(neighbours.size());
//                for (int i = 0; i < neighbours.size(); i++){
//                    if (i != randIdx) pathStack.push(neighbours.get(i));
//                }
//                pathStack.push(neighbours.get(randIdx));
//                node.connect(neighbours.get(randIdx));
//                visited.add(neighbours.get(randIdx));
            }
        }
    }
}
