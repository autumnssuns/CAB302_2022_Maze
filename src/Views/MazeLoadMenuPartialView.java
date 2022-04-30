package Views;

import DatabaseConnection.MazeDataSource;
import Models.Maze;
import Models.MazeDataModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

public class MazeLoadMenuPartialView extends PartialView implements ActionListener {
    private ArrayList<MazeLoadPanel> names;
    private JScrollPane scrollPane;
    private JPanel contentPanel;

    public MazeLoadMenuPartialView(MainView view) {
        super(view);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        names = new ArrayList<>();
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        add(scrollPane);

        add(scrollPane);
    }

    public void reloadMazes(){
        contentPanel.setVisible(false);
        this.names.forEach(label -> {
            contentPanel.remove(label);
            label.setVisible(false);
        });
        this.names.removeAll(this.names);
        contentPanel.revalidate();
        contentPanel.repaint();

        MazeDataSource dataSource = new MazeDataSource();
        Set<String> names = dataSource.getNames();
        names.forEach(n -> {
            this.names.add(new MazeLoadPanel(n, this));
        });
        this.names.forEach(label -> {
            contentPanel.add(label);
            label.setVisible(true);
        });
        contentPanel.revalidate();
        contentPanel.repaint();
        contentPanel.setVisible(true);
    }

    public void loadMaze(MazeDataModel mazeModel){
        view.loadMaze(mazeModel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
