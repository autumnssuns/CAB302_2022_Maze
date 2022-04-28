package Views;

import DatabaseConnection.MazeDataSource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

public class MazeLoadMenuPartialView extends PartialView implements ActionListener {
    private ArrayList<JLabel> names;

    public MazeLoadMenuPartialView(MainView view) {
        super(view);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        names = new ArrayList<>();
    }

    public void reloadMazes(){
        setVisible(false);
        this.names.forEach(label -> {
            remove(label);
            label.setVisible(false);
        });
        this.names.removeAll(this.names);
        revalidate();
        repaint();

        MazeDataSource dataSource = new MazeDataSource();
        Set<String> names = dataSource.getNames();
        names.forEach(n -> {
            this.names.add(new JLabel(n));
        });
        this.names.forEach(label -> {
            add(label);
            label.setVisible(true);
        });
        revalidate();
        repaint();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
