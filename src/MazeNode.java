import java.util.ArrayList;
import java.util.HashMap;

public class MazeNode {
    private MazeNode left, right, top, bottom;
    private HashMap<String, MazeNode> neighbours;
    private int row, col;
    private String value;

    private NodeButton attachedButton;

    public MazeNode(){
        left = null;
        right = null;
        top = null;
        bottom = null;
        neighbours = new HashMap<>();
    }

    public void attachButton(NodeButton button){
        this.attachedButton = button;
    }

    public NodeButton getAttachedButton(){
        return attachedButton;
    }

    public void setCoordinate(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public MazeNode goTo(String command){
        return neighbours.getOrDefault(command, null);
    }

    public ArrayList<MazeNode> getNeighbours(){
        ArrayList<MazeNode> neighbours = new ArrayList(this.neighbours.values());
        return neighbours;
    }

    public String getValue() {
        return value;
    }

    public MazeNode setValue(String value) {
        this.value = value;
        return this;
    }

    public MazeNode setLeft(MazeNode node){
        left = node;
        neighbours.put("left", left);
        return this;
    }

    public MazeNode setRight(MazeNode node){
        right = node;
        neighbours.put("right", right);
        return this;
    }

    public MazeNode setTop(MazeNode node){
        top = node;
        neighbours.put("top", top);
        return this;
    }

    public MazeNode setBottom(MazeNode node){
        bottom = node;
        neighbours.put("bottom", bottom);
        return this;
    }

    public MazeNode getLeft() {
        return left;
    }

    public MazeNode getRight() {
        return right;
    }

    public MazeNode getTop() {
        return top;
    }

    public MazeNode getBottom() {
        return bottom;
    }

    public void connect(MazeNode other){
        if (this.row == other.getRow() && this.col + 1 == other.getCol()) {
            setRight(other);
            other.setLeft(this);
            this.getAttachedButton().setText("L");
            other.getAttachedButton().setText("R");
        }
        if (this.row == other.getRow() && this.col - 1 == other.getCol()) {
            setLeft(other);
            other.setRight(this);
            this.getAttachedButton().setText("R");
            other.getAttachedButton().setText("L");
        }
        if (this.row + 1 == other.getRow() && this.col == other.getCol()) {
            setBottom(other);
            other.setTop(this);
            this.getAttachedButton().setText("T");
            other.getAttachedButton().setText("B");
        }
        if (this.row - 1 == other.getRow() && this.col == other.getCol()) {
            setTop(other);
            other.setBottom(this);
            this.getAttachedButton().setText("B");
            other.getAttachedButton().setText("T");
        }
    }
}
