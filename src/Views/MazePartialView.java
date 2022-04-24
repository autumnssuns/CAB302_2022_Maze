package Views;

import Generators.GeneratorFactory;
import Models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class MazePartialView extends PartialView implements ActionListener {

    private ArrayList<BorderButton> vButtons = new ArrayList<>();
    private ArrayList<BorderButton> hButtons = new ArrayList<>();
    private ArrayList<NodeButton> nodeButtons;
    private Timer animationTimer;

    private int rows, cols;
    private Maze maze;
    private MazeNode current;

    public MazePartialView (MainView container, int rows, int cols){
        super(container);
        this.setBackground(Color.WHITE);
        setMazeSize(rows, cols);
    }

    public void setMazeSize(int rows, int cols){
        this.maze = new Maze(rows, cols);
        this.rows = rows;
        this.cols = cols;
        nodeButtons = new ArrayList<>();
        hButtons = new ArrayList<>();
        vButtons = new ArrayList<>();
        current = maze.getRoot();
    }


    public void setGenerator(int generatorType, long seed){
        maze.setGenerator(generatorType, seed);
    }

    public void render(){
//        int size = (int) Math.floor(Math.min((675 + 1.2 * rows) / rows, (675 + 1.2 * cols) / cols));
        float sig = Math.max(rows, cols);
        float numerator = sig > 2 ? 675 : sig == 2 ? 650 : 600;
        int size = (int) Math.ceil(numerator / sig) + 1;
        int weight = (int) Math.ceil(0.2 * size);
        Dimension horizontalSize = new Dimension(size + weight, weight);
        Dimension verticalSize = new Dimension(weight, size + weight);
        setLayout(null);

        for (int i = 0; i <= cols; i++){
            for (int j = 0; j < rows; j++){
                int x = size * i;
                int y = size * j;

                BorderButton buttonVertical = new BorderButton(this);
                buttonVertical.setBounds(x, y, verticalSize.width, verticalSize.height);
                buttonVertical.setClosed(i == 0 || i == cols);
//                buttonVertical.setText(String.valueOf(vButtons.size()));
                if (i == 0 || i == cols) buttonVertical.setToggleable(false);
                vButtons.add(buttonVertical);
                add(buttonVertical);
            }
        }

        for (int i = 0; i < cols; i++){
            for (int j = 0; j <= rows; j++){
                int x = size * i;
                int y = size * j;

                BorderButton buttonHorizontal = new BorderButton(this);
                buttonHorizontal.setBounds(x, y, horizontalSize.width, horizontalSize.height);
                buttonHorizontal.setClosed(j == 0 || j == rows);
//                buttonHorizontal.setText(String.valueOf(hButtons.size()));
                if (j == 0 || j == rows) buttonHorizontal.setToggleable(false);
                hButtons.add(buttonHorizontal);
                add(buttonHorizontal);
            }
        }
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                NodeButton nodeButton = new NodeButton(maze.get(i,j));
                nodeButton.setBounds(weight + (size) * j, weight + (size) * i, (size - weight), (size - weight));
                nodeButton.setBorderPainted(false);
                nodeButton.setBackground(Color.WHITE);
//                nodeButton.setText(String.format("LR: %d%d TB:%d%d",i + j * rows, i + (1 + j) * rows, i + j * rows + j, i + 1 + j * rows + j));
//                nodeButton.setText(String.format("L:%d R:%d T:%d B:", i + j * cols, i + (j + 1) * cols, i + j * cols, i + (j + 1) * cols));
                add(nodeButton);

                BorderButton leftButton = vButtons.get(i + j * rows);
//                leftButton.setState(new Random().nextBoolean());
                nodeButton.attachLeftButton(leftButton);

                BorderButton rightButton = vButtons.get(i + (1 + j) * rows);
//                rightButton.setState(new Random().nextBoolean());
                nodeButton.attachRightButton(rightButton);

                BorderButton topButton = hButtons.get(i + j * rows + j);
//                topButton.setState(false);
                nodeButton.attachTopButton(topButton);

                BorderButton bottomButton = hButtons.get(i + 1 + j * rows + j);
//                bottomButton.setState(new Random().nextBoolean());
                nodeButton.attachBottomButton(bottomButton);

                nodeButtons.add(nodeButton);
//                nodeButton.toggleAll();
            }
        }

        maze.connectAll();
        nodeButtons.forEach(NodeButton::connectAll);
        maze.disconnectAll();

//        nodeButtons.forEach(NodeButton::repaintWalls);
        maze.generate();

        ArrayList<StateFrame> steps = maze.getFrames();
        createTimer(steps);
        if (view.isShowingAnimation()){
            animationTimer.start();
        } else {
            nodeButtons.forEach(NodeButton::repaintWalls);
        }

        current.getAttachedButton().setBackground(Color.RED);

        int maxHeight = (size) * rows + weight;
        int maxWidth = (size) * cols + weight;
        setSize(new Dimension(maxWidth, maxHeight));

        setVisible(true);
    }

    public void startAnimation(){
        createTimer(maze.getFrames());
        animationTimer.restart();
    }

    public void stopAnimation(){
        nodeButtons.forEach(NodeButton::repaintWalls);
        if (animationTimer.isRunning()) animationTimer.stop();
    }

    public void createTimer(ArrayList<StateFrame> steps){
        boolean blankStart = GeneratorFactory.isCreative(maze.getGeneratorType());
        hButtons.forEach(x -> {
            if (x.isToggleable()) x.paintBorder(!blankStart);
        });
        vButtons.forEach(x -> {
            if (x.isToggleable()) x.paintBorder(!blankStart);
        });
        int interval = 1;
        int end = interval * steps.size();
//        maze.disconnectAll();
        animationTimer = new Timer(interval, new ActionListener() {
            int currentTime = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = currentTime / interval;
                if (idx < steps.size()) {
                    MazeNode node = steps.get(idx).getNode();
//                    node.getAttachedButton().repaintWalls();
                    boolean top = (Boolean) steps.get(idx).getState().get("top");
                    node.getAttachedButton().paintWall("top", !top);
                    boolean bottom = (Boolean) steps.get(idx).getState().get("bottom");
                    node.getAttachedButton().paintWall("bottom", !bottom);
                    boolean left = (Boolean) steps.get(idx).getState().get("left");
                    node.getAttachedButton().paintWall("left", !left);
                    boolean right = (Boolean) steps.get(idx).getState().get("right");
                    node.getAttachedButton().paintWall("right", !right);
                }
                if (currentTime <= end){
                    currentTime++;
                } else {
                    ((Timer)e.getSource()).stop();
                }
            }
        });
    }

    public void showSolution(Graphics2D g){
        ArrayList<MazeNode> solution = maze.getSolution(current, maze.getDestination());
        maze.getRoot().getAttachedButton().setText("R");
        maze.getDestination().getAttachedButton().setText("D");
        ArrayList<Point> points = new ArrayList<>();

        if (solution == null) return;
        if (!view.isShowingSolution()) return;
        for (MazeNode node : solution){
            Point point = node.getAttachedButton().getLocation();
            Dimension size = node.getAttachedButton().getSize();
            point.setLocation(point.getX() + size.width / 2, point.getY() + size.getHeight() / 2);
            points.add(point);
        }
        for (int i = 0; i < points.size() - 1; i ++){
            double xFrom = points.get(i).getX();
            double yFrom = points.get(i).getY();
            double xTo = points.get(i + 1).getX();
            double yTo = points.get(i + 1).getY();
            Line2D line = new Line2D.Double(xFrom, yFrom, xTo, yTo);
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(2));
            g.draw(line);
        }
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        showSolution(g2);
    }

    public void move(int keyCode){
        switch (keyCode){
            case KeyEvent.VK_UP -> {
                current.getAttachedButton().setBackground(Color.WHITE);
                current = current.getTop() != null ? current.getTop() : current;
                current.getAttachedButton().setBackground(Color.RED);
            }
            case KeyEvent.VK_DOWN -> {
                current.getAttachedButton().setBackground(Color.WHITE);
                current = current.getBottom() != null ? current.getBottom() : current;
                current.getAttachedButton().setBackground(Color.RED);
            }
            case KeyEvent.VK_LEFT -> {
                current.getAttachedButton().setBackground(Color.WHITE);
                current = current.getLeft() != null ? current.getLeft() : current;
                current.getAttachedButton().setBackground(Color.RED);
            }
            case KeyEvent.VK_RIGHT -> {
                current.getAttachedButton().setBackground(Color.WHITE);
                current = current.getRight() != null ? current.getRight() : current;
                current.getAttachedButton().setBackground(Color.RED);
            }
        }
        repaint();
    }

    public void clear(){
        setVisible(false);
        removeAll();
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        BorderButton sourceButton = (BorderButton) source;
        sourceButton.action();
//        maze.getRoot().getAttachedButton().changeColour();
        repaint();
        view.requestFocus();
    }
//
//    public void shift(int x, int y) {
//        for (JButton button : vButtons){
//            button.setLocation(button.getX() + x, button.getY() + y);
//        }
//        for (JButton button : hButtons){
//            button.setLocation(button.getX() + x, button.getY() + y);
//        }
//    }

    public void toggleGrid(boolean state){
        vButtons.forEach(x -> x.setBorderPainted(state));
        hButtons.forEach(x -> x.setBorderPainted(state));
    }

    public void export(String path){
        // TODO: Export as images
    }

    public Maze getMaze() {
        return maze;
    }
}
