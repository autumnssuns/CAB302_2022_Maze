package Models;

import Generators.Generator;
import Generators.GeneratorFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Maze implements Iterable<MazeNode>{
    private final MazeNode root;
    private MazeNode destination;
    private final ArrayList<ArrayList<MazeNode>> nodes;
    private int cols, rows;
    private Generator generator;

    public Maze(){
        this.root = new MazeNode().setValue("Root: 0, 0");
        nodes = new ArrayList<>();
    }

    public void setGenerator(int generatorType){
        GeneratorFactory factory = new GeneratorFactory(this);
        this.generator = factory.create(generatorType);
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
        }//        connectAll();
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
}
