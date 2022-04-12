package Models;

import Views.NodeButton;

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

    public void setLeft(MazeNode node){
        left = node;
        neighbours.put("left", left);
    }

    public void setRight(MazeNode node){
        right = node;
        neighbours.put("right", right);
    }

    public void setTop(MazeNode node){
        top = node;
        neighbours.put("top", top);
    }

    public void setBottom(MazeNode node){
        bottom = node;
        neighbours.put("bottom", bottom);
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

    public void disconnect(MazeNode other){
        if (this.row == other.getRow() && this.col + 1 == other.getCol()) {
            setRight(null);
            other.setLeft(null);
        }
        if (this.row == other.getRow() && this.col - 1 == other.getCol()) {
            setLeft(null);
            other.setRight(null);
        }
        if (this.row + 1 == other.getRow() && this.col == other.getCol()) {
            setBottom(null);
            other.setTop(null);
        }
        if (this.row - 1 == other.getRow() && this.col == other.getCol()) {
            setTop(null);
            other.setBottom(null);
        }
    }

    public void connect(MazeNode other){
        if (this.row == other.getRow() && this.col + 1 == other.getCol()) {
            setRight(other);
            other.setLeft(this);
        }
        if (this.row == other.getRow() && this.col - 1 == other.getCol()) {
            setLeft(other);
            other.setRight(this);
        }
        if (this.row + 1 == other.getRow() && this.col == other.getCol()) {
            setBottom(other);
            other.setTop(this);
        }
        if (this.row - 1 == other.getRow() && this.col == other.getCol()) {
            setTop(other);
            other.setBottom(this);
        }
    }

    public boolean hasNeighbour(MazeNode other){
        return neighbours.containsValue(other);
    }
}
