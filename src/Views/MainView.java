package Views;

import Models.Maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainView extends JFrame implements KeyListener, Runnable {
    private MazePartialView mazePartialView;
    private MazeLoadMenuPartialView mazeLoadMenuPartialView;
    private ThinSplitPane mainContainer, editorLoadContainer;
    private JPanel menuContainer;
    private JPanel editorContainer;
    private JPanel mazePartialViewContainer;
    private JPanel loadContainer;

    private boolean showingAnimation;
    private boolean showingGrid;
    private boolean showingSolution;

    public boolean isShowingGrid() {
        return showingGrid;
    }

    public void setShowingGrid(boolean showingGrid) {
        this.showingGrid = showingGrid;
    }

    public boolean isShowingSolution() {
        return showingSolution;
    }

    public void setShowingSolution(boolean showingSolution) {
        this.showingSolution = showingSolution;
    }

    public void createGUI(){
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setFocusable(true);
        addKeyListener(this);

        getContentPane().setLayout(new BorderLayout());
        menuContainer = new JPanel(new BorderLayout());

        loadContainer = new JPanel(new BorderLayout());

        editorContainer = new JPanel();
        editorContainer.setLayout(new BoxLayout(editorContainer, BoxLayout.X_AXIS));

        editorLoadContainer = new ThinSplitPane(JSplitPane.HORIZONTAL_SPLIT, editorContainer, loadContainer);
        editorContainer.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
        editorContainer.setPreferredSize(new Dimension(950, 800));

        mainContainer = new ThinSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuContainer, editorLoadContainer);
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
        mazeLoadMenuPartialView = new MazeLoadMenuPartialView(this);

        loadContainer.add(mazeLoadMenuPartialView);

        getContentPane().add(new JPanel(), BorderLayout.PAGE_END);
        getContentPane().add(new JPanel(), BorderLayout.LINE_END);

        mazeLoadMenuPartialView.reloadMazes();
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
        MazeCreationMenuPartialView mazeCreationMenuPartialView = new MazeCreationMenuPartialView(this);
        MazeViewOptionPartialView mazeViewOptionPartialView = new MazeViewOptionPartialView(this);

        ThinSplitPane container = new ThinSplitPane(JSplitPane.VERTICAL_SPLIT, mazeCreationMenuPartialView, mazeViewOptionPartialView);
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

    public void setMazeGenerator(int generatorType, long seed){
        mazePartialView.setGenerator(generatorType, seed);
    }


    public void clearMazeView(){
        mazePartialView.clear();
    }

    public void toggleMazeGrid(boolean state){
        showingGrid = state;
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

    public Maze getMaze(){
        return mazePartialView.getMaze();
    }

    public void renderMazeView() {
        mazePartialView.render();
        mazePartialViewContainer.setPreferredSize(mazePartialView.getSize());
        mazePartialViewContainer.setMaximumSize(mazePartialView.getSize());
        mazePartialViewContainer.setMinimumSize(mazePartialView.getSize());
        toggleMazeGrid(showingGrid);
    }

    public void toggleMazeSolution(boolean state) {
        showingSolution = state;
        mazePartialView.repaint();
    }

    public void toggleAnimation(boolean state){
        showingAnimation = state;
        if (mazePartialView != null){
            if (state) mazePartialView.startAnimation();
            else mazePartialView.stopAnimation();
        }
    }

    public boolean isShowingAnimation() {
        return showingAnimation;
    }

    public void setShowingAnimation(boolean showingAnimation) {
        this.showingAnimation = showingAnimation;
    }

    public void reloadSavedMazes(){
        mazeLoadMenuPartialView.reloadMazes();
    }
}
