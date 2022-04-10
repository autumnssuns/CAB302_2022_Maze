package Models;

import java.util.ArrayList;

public class Wall {
    ArrayList<MazeNode> nodes;

    public Wall(){
        this.nodes = new ArrayList<>();
    }

    public void addNode(MazeNode node){
        this.nodes.add(node);
    }

    public void removeNode(MazeNode node){
        nodes.remove(node);
    }
}
