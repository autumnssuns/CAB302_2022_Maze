package Views;

import DatabaseConnection.AssetsDataSource;
import Generators.GeneratorFactory;
import Models.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class MazePartialView extends PartialView implements ActionListener {

    private ArrayList<BorderButton> vButtons = new ArrayList<>();
    private ArrayList<BorderButton> hButtons = new ArrayList<>();
    private ArrayList<NodeButton> nodeButtons;
    private Timer animationTimer;

    private int rows, cols;
    private Maze maze;
    private MazeNode current;
    private ArrayList<JLabel> images;
    private int size, weight;
//    private ArrayList<Map.Entry<AssetDataModel, ArrayList<Integer>>> images;

    public MazePartialView (MainView container, Maze maze){
        super(container);
        this.setBackground(Color.WHITE);
        setMaze(maze);
    }

    public void setMaze(Maze maze){
        this.maze = maze;
        this.rows = maze.getSize(0);
        this.cols = maze.getSize(1);
        nodeButtons = new ArrayList<>();
        hButtons = new ArrayList<>();
        vButtons = new ArrayList<>();
        images = new ArrayList<>();
        current = maze.getRoot();
    }

    public void setGenerator(int generatorType, long seed){
        maze.setGenerator(generatorType, seed);
    }

    public void render(){
        renderButtons();
        if (!maze.isLocked()) generate();
        else nodeButtons.forEach(NodeButton::repaintWalls);
        current.getAttachedButton().setBackground(Color.RED);
        setVisible(true);
    }

    private void generate(){
//        maze.connectAll();
//        nodeButtons.forEach(NodeButton::connectAll);
        maze.disconnectAll();
        maze.generate();
        ArrayList<StateFrame> steps = maze.getFrames();
        createTimer(steps);
        if (view.isShowingAnimation()){
            animationTimer.start();
        } else {
            nodeButtons.forEach(NodeButton::repaintWalls);
        }
    }

    private void renderButtons(){
        //        int size = (int) Math.floor(Math.min((675 + 1.2 * rows) / rows, (675 + 1.2 * cols) / cols));
        float sig = Math.max(rows, cols);
        float numerator = sig > 2 ? 675 : sig == 2 ? 650 : 600;
        size = (int) Math.ceil(numerator / sig) + 1;
        weight = (int) Math.ceil(0.2 * size);
        Dimension horizontalSize = new Dimension(size + weight, weight);
        Dimension verticalSize = new Dimension(weight, size + weight);
        setLayout(null);
        int maxHeight = (size) * rows + weight;
        int maxWidth = (size) * cols + weight;
        setSize(new Dimension(maxWidth, maxHeight));

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

        IntStream.range(0, nodeButtons.size()).forEach(idx -> {
            NodeButton n = nodeButtons.get(idx);
            int row = idx / cols;
            int col = idx % cols;
            NodeButton top = row == 0 ? null : nodeButtons.get((row - 1) * cols + col);
            NodeButton bottom = row == rows - 1 ? null : nodeButtons.get((row + 1) * cols + col);
            NodeButton left = col == 0 ? null : nodeButtons.get(row * cols + col - 1);
            NodeButton right = col == cols - 1 ? null : nodeButtons.get(row * cols + col + 1);
            n.connectAll(top, bottom, left, right);
        });
    }

    public void startAnimation(){
        createTimer(maze.getFrames());
        animationTimer.restart();
    }

    public void stopAnimation(){
        nodeButtons.forEach(NodeButton::repaintWalls);
        if (animationTimer.isRunning()) animationTimer.stop();
    }

    private void createTimer(ArrayList<StateFrame> steps){
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

    private void showSolution(Graphics2D g){
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

    public void addImage(AssetDataModel assetDataModel){
//        AbstractMap.SimpleEntry<AssetDataModel, ArrayList<Integer>> entry = new AbstractMap.SimpleEntry<>(assetDataModel, new ArrayList<>(List.of(new Integer[]{0, 0})));
//        images.add(entry);
        int imageSize = 2 * size - weight;
        ImageIcon icon = assetDataModel.icon();
        BufferedImage resizedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(icon.getImage(), 0, 0, imageSize, imageSize, null);
        graphics2D.dispose();
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel label = new JLabel(resizedIcon);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.setSize(label.getPreferredSize());
        label.setLocation(weight, weight);
        ImageMouseHandler imageMouseHandler = new ImageMouseHandler();
        label.addMouseListener(imageMouseHandler);
        label.addMouseMotionListener(imageMouseHandler);
        add(label, 0);
        images.add(label);
        repaint();
    }

    // https://stackoverflow.com/questions/27915214/how-can-i-drag-images-with-the-mouse-cursor-in-java-gui
    private class ImageMouseHandler extends MouseAdapter{
        private Point offset;

        @Override
        public void mousePressed(MouseEvent e) {
            JLabel label = (JLabel) e.getComponent();
            moveToFront(label);
            offset = e.getPoint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getPoint().x - offset.x;
            x -= x % size;
            int y = e.getPoint().y - offset.y;
            y -= y % size;
            Component component = e.getComponent();
            Point location = component.getLocation();
            location.x += x;
            location.y += y;
            component.setLocation(location);
        }
    }
//    private void showImages(Graphics2D g){
//        images.forEach(entry -> {
//            AssetDataModel model = entry.getKey();
//            ArrayList<Integer> coords = entry.getValue();
//            ImageIcon image = model.icon();
//            g.drawImage(image.getImage(), coords.get(0), coords.get(1), this);
//        });
//    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        showSolution(g2);
//        showImages(g2);
//        images.forEach(im -> im.grabFocus());
    }

    public void saveImage(String path){
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        paint(g2);
        g2.dispose();

        int maxDim = Math.max(cols,rows);
        int cellSize = (int) Math.ceil(((16 - 64)/(100f - 6))*(maxDim - 6) + 64);

        int newWidth = cols * cellSize;
        int newHeight = rows * cellSize;

        Image tmp = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        try {
            File imageFile = new File(path + ".png");
            ImageIO.write(newImage, "png", imageFile);
            if(!Desktop.isDesktopSupported()){
                System.out.println("Desktop is not supported");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if(imageFile.exists()) desktop.open(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImageIcon getIcon(){
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        paint(g2);
        g2.dispose();
        Image tmp = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return new ImageIcon(newImage);
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

    public void toggleGrid(boolean state){
        vButtons.forEach(x -> x.setBorderPainted(state));
        hButtons.forEach(x -> x.setBorderPainted(state));
    }

    public Maze getMaze() {
        return maze;
    }
}
