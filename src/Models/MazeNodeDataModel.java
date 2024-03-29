package Models;

import java.util.*;
import java.util.stream.Collectors;

// MazeIndex, RowsCount, ColsCount
// MazeIndex, NodeIndex, Neighbours
public record MazeNodeDataModel(HashMap<Integer, String> neighbours) {
    public ArrayList<Integer> getNeighbours(int index){
        if (neighbours.get(index).isBlank()) return new ArrayList<>();
        return new ArrayList<>(Arrays.stream(neighbours.get(index).split(",")).map(Integer::valueOf).toList());
    }
}
