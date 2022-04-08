import java.util.ArrayList;

public class Maze{
    private MazeNode root;
    private MazeNode destination;
    private ArrayList<ArrayList<MazeNode>> nodes;
    private int cols, rows;
    private Generator generator;

    public Maze(){
        this.root = new MazeNode().setValue("Root: 0, 0");
        nodes = new ArrayList<>();
    }

    public Maze(int rows, int cols){
        this();
        this.cols = cols;
        this.rows = rows;
        for (int i = 0; i < rows; i++){
            nodes.add(new ArrayList<>());
            for (int j = 0; j < cols; j++){
                MazeNode node = (i + j) == 0 ? root : new MazeNode();
                node.setCoordinate(i,j);
                nodes.get(i).add(node);
            }
        }
        this.generator = new Generator(this);
        connectAll();
    }

    public void generate(){
        generator.generate();
    }

    public void connectAll(){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                MazeNode current = get(i, j);
                if (i > 0) current.setTop(get(i - 1, j));
                if (i < rows - 1) current.setBottom(get(i + 1, j));
                if (j > 0) current.setLeft(get(i, j - 1));
                if (j < cols - 1) current.setRight(get(i, j + 1));
            }
        }
    }

    public void disconnectAll(){
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                MazeNode current = get(i, j);
                if (i > 0) current.setTop(null);
                if (i < rows - 1) current.setBottom(null);
                if (j > 0) current.setLeft(null);
                if (j < cols - 1) current.setRight(null);
            }
        }
    }

    public int getNodeCount(){
        return cols * rows;
    }

    public int getSize(){
        return cols * rows;
    }

    public int getSize(int dimension){
        switch (dimension){
            case 0:
                return rows;
            case 1:
                return cols;
        }
        return -1;
    }

    public MazeNode getRoot() {
        return root;
    }

    public MazeNode getDestination() {
        return destination;
    }

    public void setDestination(MazeNode destination) {
        this.destination = destination;
    }

    public MazeNode get(int index){
        for (ArrayList<MazeNode> currentRow : nodes) {
            if (index >= currentRow.size()) {
                index -= currentRow.size();
            } else {
                return currentRow.get(index);
            }
        }
        return null;
    }

    public MazeNode get(int row, int col){
        return nodes.get(row).get(col);
    }
}
