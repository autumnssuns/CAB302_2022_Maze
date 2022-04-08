import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class MazePartialView extends JPanel implements ActionListener {
    private static int weight;
    private static int size;

    private static Dimension horizontalSize;
    private static Dimension verticalSize;

    private ArrayList<BorderButton> vButtons = new ArrayList<>();
    private ArrayList<BorderButton> hButtons = new ArrayList<>();
    private ArrayList<NodeButton> nodeButtons;

    private int rows, cols;
    private final View container;
    private Maze maze;

    public MazePartialView (View container, int rows, int cols){
        this.container = container;
        setSize(rows, cols);
//        setSize(55 * cols, 55 * rows);
    }

    public void setSize(int rows, int cols){
        this.maze = new Maze(rows, cols);
        this.rows = rows;
        this.cols = cols;
        nodeButtons = new ArrayList<>();
        hButtons = new ArrayList<>();
        vButtons = new ArrayList<>();
    }

    public void render(){
        size = (int) Math.ceil(Math.min((675 + 1.2*rows) / rows, (675 + 1.2*cols) / cols));
        weight = (int) Math.ceil(0.2 * size);
        horizontalSize = new Dimension(size + weight, weight);
        verticalSize = new Dimension(weight, size + weight);
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
        nodeButtons.forEach(NodeButton::connectAll);

//        nodeButtons.forEach(NodeButton::toggleAll);
//        hButtons.forEach(x -> {
//            if (new Random().nextBoolean()) x.action();
//        });
//        vButtons.forEach(x -> {
//            if (new Random().nextBoolean()) x.action();
//        });

        maze.disconnectAll();

        maze.generate();

        nodeButtons.forEach(NodeButton::repaintWalls);

        maze.getRoot().getAttachedButton().setText("R");

        setVisible(true);
    }

    public void clear(){
        setVisible(false);
        removeAll();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        BorderButton sourceButton = (BorderButton)source;
        sourceButton.action();
        maze.getRoot().getAttachedButton().changeColour();
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
}
