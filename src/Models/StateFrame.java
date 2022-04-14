package Models;

import java.util.AbstractMap;
import java.util.HashMap;

import static javax.swing.UIManager.put;

public class StateFrame {
    private AbstractMap.SimpleEntry<MazeNode, HashMap<String, Boolean>> state;

    public StateFrame(MazeNode node){
        HashMap<String, Boolean> stateMap = new HashMap<>();
        stateMap.put("top", node.getTop() != null);
        stateMap.put("bottom", node.getBottom() != null);
        stateMap.put("left", node.getLeft() != null);
        stateMap.put("right", node.getRight() != null);
        state = new AbstractMap.SimpleEntry<>(node, stateMap);
    }

    public MazeNode getNode(){
        return state.getKey();
    }

    public HashMap getState(){
        return state.getValue();
    }
}
