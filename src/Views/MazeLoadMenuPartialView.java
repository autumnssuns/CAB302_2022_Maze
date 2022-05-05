package Views;

import DatabaseConnection.MazeDataSource;
import Models.MazeDataModel;
import Utils.Graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.*;

public class MazeLoadMenuPartialView extends PartialView implements ActionListener {
    private ArrayList<MazeLoadPanel> panels;
    private JScrollPane scrollPane;
    private JPanel contentPanel;
    private JComboBox sortComboBox;
    private JCheckBox reverseCheckBox;

    public MazeLoadMenuPartialView(MainView view) {
        super(view);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createSortSelection();

        panels = new ArrayList<>();
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Graphics.COLOR.LOAD_PANE_DEFAULT.getColor());

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        add(scrollPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);
    }

    private void createSortSelection(){
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 0));
        container.setMinimumSize(new Dimension(300, 25));
        container.setPreferredSize(new Dimension(300, 25));
        container.setMaximumSize(new Dimension(300, 25));
        JLabel label = new JLabel("Sort Mazes By: ");
        container.add(label);
        sortComboBox = new JComboBox<>(new String[] {
                "Name",
                "Author",
                "Created Time",
                "Modified Time",
        });
        sortComboBox.addActionListener(this);
        container.add(sortComboBox);
        reverseCheckBox = new JCheckBox("Reversed");
        reverseCheckBox.addActionListener(this);
        container.add(reverseCheckBox);
        add(container);
    }

    public void reloadMazes(){
        contentPanel.setVisible(false);
        panels.forEach(label -> {
            contentPanel.remove(label);
            label.setVisible(false);
        });
        panels.removeAll(panels);
        contentPanel.revalidate();
        contentPanel.repaint();

        MazeDataSource dataSource = new MazeDataSource();
        Set<MazeDataModel> mazeDataModels = dataSource.getMazes();

        Comparator<MazeDataModel> comparator = (o1, o2) -> (reverseCheckBox.getModel().isSelected() ? -1 : 1) * switch (sortComboBox.getSelectedIndex()) {
            default -> o1.name().compareTo(o2.name());
            case 1 -> o1.author().compareTo(o2.author());
            case 2 -> o1.createdTime().compareTo(o2.modifiedTime());
            case 3 -> o1.modifiedTime().compareTo(o2.modifiedTime());
        };

        mazeDataModels.stream().sorted(comparator).forEach(n -> {
            panels.add(new MazeLoadPanel(n, this));
        });
        panels.forEach(label -> {
            contentPanel.add(label);
            label.setVisible(true);
        });
        contentPanel.revalidate();
        contentPanel.repaint();
        contentPanel.setVisible(true);
    }

    public void loadMaze(MazeDataModel mazeModel){
        view.loadMaze(mazeModel);
        panels.forEach(p -> p.setDefaultColor(Graphics.COLOR.LOAD_PANE_DEFAULT.getColor()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        reloadMazes();
    }
}
