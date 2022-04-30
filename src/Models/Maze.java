package Models;

import Generators.Generator;
import Generators.GeneratorFactory;

import java.util.*;
import java.util.function.Consumer;

public class Maze implements Iterable<MazeNode>{
    private MazeNode root;
    private MazeNode destination;
    private final ArrayList<ArrayList<MazeNode>> nodes;
    private int cols, rows;
    private Generator generator;
    private int generatorType;
    private boolean locked;

    public Maze(){
        nodes = new ArrayList<>();
    }

    public void setGenerator(int generatorType, long seed){
        GeneratorFactory factory = new GeneratorFactory(this);
        this.generatorType = generatorType;
        this.generator = factory.create(generatorType);
        this.generator.setSeed(seed);
    }

    public long getSeed(){
        return generator.getSeed();
    }

    public int getGeneratorType(){
        return this.generatorType;
    }

    public Maze(int rows, int cols){
        this();
        this.cols = cols;
        this.rows = rows;
        for (int i = 0; i < rows; i++){
            nodes.add(new ArrayList<>());
            for (int j = 0; j < cols; j++){
                MazeNode node = new MazeNode();
                node.setCoordinate(i,j);
                nodes.get(i).add(node);
            }
        }//        connectAll();
        setRoot(this.get(0));
        setDestination(this.get(getSize() - 1));
    }

    public void generate(){
        generator.generate();
    }

    public ArrayList<StateFrame> getFrames(){
        return generator.getFrames();
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

    public int getSize(){
        return cols * rows;
    }

    public int getSize(int dimension){
        return switch (dimension) {
            case 0 -> rows;
            case 1 -> cols;
            default -> -1;
        };
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

    @Override
    public Iterator<MazeNode> iterator() {
        return new Iterator<>() {
            private int idx = 0;

            @Override
            public boolean hasNext() {
                return idx < getSize() && get(idx) != null;
            }

            @Override
            public MazeNode next() {
                return get(idx++);
            }
        };
    }

    @Override
    public void forEach(Consumer action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator spliterator() {
        return Iterable.super.spliterator();
    }

    public ArrayList<MazeNode> getSolution(){
        return getSolution(root, destination);
    }

    public ArrayList<MazeNode> getSolution(MazeNode source, MazeNode dest){
        ArrayList<MazeNode> directions = new ArrayList<>();
        HashMap<MazeNode, Boolean> visited = new HashMap<>();
        HashMap<MazeNode, MazeNode> prev = new HashMap<>();
        Queue<MazeNode> q = new LinkedList<>();
        MazeNode current = source;
        q.add(current);
        visited.put(current, true);
        while(!q.isEmpty()){
            current = q.remove();
            if (current == dest){
                break;
            } else {
                for (MazeNode node : current.getNeighbours()){
                    if (!visited.containsKey(node) && node != null){
                        q.add(node);
                        visited.put(node, true);
                        prev.put(node, current);
                    }
                }
            }
        }
        if (!current.equals(dest)) return null;

        for (MazeNode node = dest; node != null; node = prev.get(node)){
            directions.add(node);
        }
        Collections.reverse(directions);
        return directions;
    }

    public ArrayList<MazeNode> getSolutionOld(MazeNode source, MazeNode dest){
        ArrayList<MazeNode> shortestPath = new ArrayList<>();
        HashMap<MazeNode, Boolean> visited = new HashMap<>();

        if (source == dest) return null;

        Queue<MazeNode> queue = new LinkedList<>();
        Stack<MazeNode> pathStack = new Stack<>();

        queue.add(source);
        visited.put(source, true);

        while (!queue.isEmpty()){
            MazeNode u = queue.poll();
            ArrayList<MazeNode> adjList = u.getNeighbours();
            adjList.removeIf(Objects::isNull);
            if (u == dest) break;

            for (MazeNode v : adjList){
                if (!visited.containsKey(v)){
                    queue.add(v);
                    visited.put(v, true);
                    pathStack.add(v);
                }
            }
        }

        MazeNode node, currentSrc = dest;
        shortestPath.add(dest);
        while(!pathStack.isEmpty()){
            node = pathStack.pop();
            if (node == source) break;
            if (currentSrc.hasNeighbour(node)){
                shortestPath.add(node);
                currentSrc = node;
            }
        }
        shortestPath.add(source);

        return shortestPath;
    }

    public void setRoot(MazeNode root) {
        this.root = root;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
