package Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AssetMenuPartialView extends PartialView implements ActionListener {
    private JButton addAssetButton;
    private ArrayList<AssetLoadPanel> panels;
    private ArrayList<JPanel> boxPanels;
    private JComboBox sortComboBox;
    private JScrollPane scrollPane;
    private StaticGridPanel contentPanel;
    private JCheckBox reverseCheckBox;

    public AssetMenuPartialView(MainView view) {
        super(view);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        panels = new ArrayList<>();

        createAssetManagementPanel();
        contentPanel = new StaticGridPanel(2, StaticGridPanel.VERTICAL);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        add(scrollPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);

        setVisible(true);
    }

    public void createAssetManagementPanel(){
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 0));
//        container.setMinimumSize(new Dimension(300, 25));
//        container.setPreferredSize(new Dimension(300, 25));
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        JLabel label = new JLabel("Sort by: ");
        container.add(label);
        sortComboBox = new JComboBox<>(new String[] {
                "Name",
                "Upload Time"
        });
        sortComboBox.addActionListener(this);
        container.add(sortComboBox);
        container.add(sortComboBox);
        reverseCheckBox = new JCheckBox("Reversed");
        reverseCheckBox.addActionListener(this);
        container.add(reverseCheckBox);

        addAssetButton = new JButton("+");
        addAssetButton.addActionListener(this);
        container.add(addAssetButton);

        add(container);
    }

    public void reloadAssets(){
        contentPanel.setVisible(false);
        contentPanel.removeAll();
        contentPanel.revalidate();
        contentPanel.repaint();

        panels.forEach(label -> {
            contentPanel.add(label);
            label.setVisible(true);
        });
        contentPanel.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addAssetButton){
            panels.add(new AssetLoadPanel());
            reloadAssets();
        }
    }
}
