package Views;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
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
        addLabel(); // PAGE START
        addMenu();  // LINE START

        getContentPane().add(new JPanel(), BorderLayout.PAGE_END);
        getContentPane().add(new JPanel(), BorderLayout.LINE_END);

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
        container.setOneTouchExpandable(true);
        container.setUI(new BasicSplitPaneUI(){
            @Override
            public BasicSplitPaneDivider createDefaultDivider()
            {
                return new BasicSplitPaneDivider(this)
                {
                    public void setBorder(Border b) {}

                    @Override
                    public void paint(Graphics g)
                    {
                        g.setColor(Color.GRAY);
                        g.fillRect(0, 0, getSize().width, 1);
                        super.paint(g);
                    }
                };
            }
        });
        getContentPane().add(container, BorderLayout.LINE_START);
    }

    public void addMazeView(int rows, int cols){
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        getContentPane().add(container);

        if (mazePartialView == null){
            mazePartialView = new MazePartialView(this, rows, cols);
        } else {
            mazePartialView.setSize(rows, cols);
        }
        container.add(new JPanel());
        container.add(mazePartialView);
        container.add(new JPanel());
        mazePartialView.setAlignmentX(Component.CENTER_ALIGNMENT);
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
