package Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainView extends JFrame implements KeyListener, Runnable {
    private MazePartialView mazePartialView;

    public void createGUI(){
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setFocusable(true);
        addKeyListener(this);

        getContentPane().setLayout(new BorderLayout());
        addLabel();
        addMenu();
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

        JSplitPane container = new JSplitPane(JSplitPane.VERTICAL_SPLIT, menuPartialView, emptyPanel);
        getContentPane().add(container, BorderLayout.LINE_START);
    }

    public void addMazeView(int rows, int cols){
        if (mazePartialView == null){
            mazePartialView = new MazePartialView(this, rows, cols);
            getContentPane().add(mazePartialView, BorderLayout.CENTER);
        } else {
            mazePartialView.setSize(rows, cols);
        }
        setVisible(true);
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
    }
}
