package Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainView extends JFrame implements KeyListener, Runnable {
    private MazePartialView mazePartialView;
    private ThinSplitPane mainContainer;
    private JPanel menuContainer;
    private JPanel editorContainer;
    private JPanel mazePartialViewContainer;

    public void createGUI(){
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setFocusable(true);
        addKeyListener(this);

        getContentPane().setLayout(new BorderLayout());
        menuContainer = new JPanel(new BorderLayout());
        editorContainer = new JPanel();
        editorContainer.setLayout(new BoxLayout(editorContainer, BoxLayout.X_AXIS));

        mainContainer = new ThinSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuContainer, editorContainer);
        mainContainer.setOneTouchExpandable(true);

        mazePartialViewContainer = new JPanel(new BorderLayout());
//        mazePartialViewContainer.setBackground(Color.pink);

        JPanel leftPadding = new JPanel();
//        leftPadding.setBackground(Color.GREEN);
        JPanel rightPadding = new JPanel();
//        rightPadding.setBackground(Color.BLUE);

        editorContainer.add(leftPadding); // Left Padding
        editorContainer.add(mazePartialViewContainer);
        editorContainer.add(rightPadding); // Right Padding
        editorContainer.setAlignmentX(Component.CENTER_ALIGNMENT);

        getContentPane().add(mainContainer);

        addLabel(); // PAGE START
        addMenu();  // LINE START

        getContentPane().add(new JPanel(), BorderLayout.PAGE_END);
        getContentPane().add(new JPanel(), BorderLayout.LINE_END);

        repaint();
        setVisible(true);
    }

    public void addLabel(){
        JLabel label = new JLabel("This is a prototype. The final software may not look like this at all.");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Montserrat", Font.BOLD, 26));
        getContentPane().add(label, BorderLayout.PAGE_START);
    }

    public void addMenu(){
        MenuPartialView menuPartialView = new MenuPartialView(this);
        JPanel emptyPanel = new JPanel();

        ThinSplitPane container = new ThinSplitPane(JSplitPane.VERTICAL_SPLIT, menuPartialView, emptyPanel);
        container.setOneTouchExpandable(true);
        menuContainer.add(container);
        menuContainer.setVisible(true);
    }

    public void addMazeView(int rows, int cols){
//        JPanel container = new JPanel();
//        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        if (mazePartialView == null){
            mazePartialView = new MazePartialView(this, rows, cols);
            mazePartialViewContainer.add(mazePartialView);
        } else {
            mazePartialView.setMazeSize(rows, cols);
        }

//        container.add(new JPanel());
//        container.add(mazePartialView);
//        container.add(new JPanel());
//        mazePartialView.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void setMazeGenerator(int generatorType){
        mazePartialView.setGenerator(generatorType);
    }

    public void clearMazeView(){
        mazePartialView.clear();
    }

    public void toggleMazeGrid(boolean state){
        mazePartialView.toggleGrid(state);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (mazePartialView != null) mazePartialView.move(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        createGUI();
    }

    public void renderMazeView() {
        mazePartialView.render();
        mazePartialViewContainer.setPreferredSize(mazePartialView.getSize());
        mazePartialViewContainer.setMaximumSize(mazePartialView.getSize());
        mazePartialViewContainer.setMinimumSize(mazePartialView.getSize());
        System.out.println("Maze View Size " + mazePartialView.getSize());
    }
}
