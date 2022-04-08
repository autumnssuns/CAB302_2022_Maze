import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {
    private static View view;

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            view = new View();
            view.getContentPane().setLayout(new BorderLayout());

            view.addLabel();

            view.addMenu();

            view.setVisible(true);
        });
    }

    public static void createMazeView(int rows, int cols){
        view.addMazeView(rows, cols);
    }

    public static void deleteMazeView(View view){
        view.clearMazeView();
    }

    public static void toggleGrid(boolean state){
        view.toggleMazeGrid(state);
    }

    public static Color getRandomColor(){
        Random rand = new Random();
        float r = rand.nextFloat() / 2f + 0.5f;
        float g = rand.nextFloat() / 2f + 0.5f;
        float b = rand.nextFloat() / 2f + 0.5f;

        return new Color(r, g, b);
    }
}