import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
    private MazePartialView mazePartialView;

    public View(){
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void addLabel(){
        JLabel label = new JLabel("This is a prototype. The final software may not look like this at all.");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Montserrat", Font.BOLD, 26));
        getContentPane().add(label, BorderLayout.PAGE_START);
    }

    public void addMenu(){
        MenuPartialView menuPartialView = new MenuPartialView(this);
        getContentPane().add(menuPartialView, BorderLayout.LINE_START);
    }

    public void addMazeView(int rows, int cols){
        if (mazePartialView == null){
            mazePartialView = new MazePartialView(this, rows, cols);
            getContentPane().add(mazePartialView, BorderLayout.CENTER);
        } else {
            mazePartialView.setSize(rows, cols);
        }
        mazePartialView.render();
//        mazePartialView.shift(0, 20);
        setVisible(true);
    }

    public void clearMazeView(){
        mazePartialView.clear();
    }

    public void toggleMazeGrid(boolean state){
        mazePartialView.toggleGrid(state);
    }
}
