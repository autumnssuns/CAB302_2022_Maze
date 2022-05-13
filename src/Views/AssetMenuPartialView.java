package Views;

import DatabaseConnection.AssetsDataSource;
import DatabaseConnection.MazeDataSource;
import Models.AssetDataModel;
import Models.MazeDataModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class AssetMenuPartialView extends PartialView implements ActionListener {
    private JButton addAssetButton;
    private ArrayList<AssetLoadPanel> panels;
    private JComboBox sortComboBox;
    private JScrollPane scrollPane;
    private StaticGridPanel contentPanel;
    private JCheckBox reverseCheckBox;

    public AssetMenuPartialView(MainView view) {
        super(view);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        panels = new ArrayList<>();

        createAssetManagementPanel();
        contentPanel = new StaticGridPanel(1, StaticGridPanel.VERTICAL);
        contentPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                contentPanel.setCountPerBox(contentPanel.getSize().width / 150 > 0 ? contentPanel.getSize().width / 150 : 1);
            }
        });
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        add(scrollPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);

        reloadAssets();
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
                "Upload Time",
                "Name"
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
//        panels.forEach(label -> {
//            contentPanel.remove(label);
//            label.setVisible(false);
//        });
        contentPanel.removeAll();
        panels.removeAll(panels);
        contentPanel.revalidate();
        contentPanel.repaint();

        AssetsDataSource dataSource = new AssetsDataSource();
        Set<AssetDataModel> assetDataModels = dataSource.getAllAssets();

        Comparator<AssetDataModel> comparator = (o1, o2) -> (reverseCheckBox.getModel().isSelected() ? -1 : 1) * switch (sortComboBox.getSelectedIndex()) {
            default -> o2.index() - o1.index();
            case 1 -> o1.name().compareTo(o2.name());
        };

        assetDataModels.stream().sorted(comparator).forEach(assetDataModel -> {
            panels.add(new AssetLoadPanel(assetDataModel, this));
        });

        panels.forEach(label -> {
            contentPanel.add(label);
            label.setVisible(true);
        });
        contentPanel.revalidate();
        contentPanel.repaint();
        contentPanel.setVisible(true);
    }

    private ArrayList<File> chooseImageFile(){
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images Files", "png", "jpg", "jpeg", "gif");
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle("Choose an image");
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return new ArrayList<>(List.of(chooser.getSelectedFiles()));
        }
        return new ArrayList<>();
    }

    private AssetDataModel loadImageFromFile(File file){
        try {
            Image image = ImageIO.read(file);
            ImageIcon icon = new ImageIcon(image);
            String name = file.getName();
            return new AssetDataModel(-1, name, icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addAssetButton){
            List<File> files = chooseImageFile();
            AssetsDataSource dataSource = new AssetsDataSource();
            files.forEach(f -> dataSource.addAsset(Objects.requireNonNull(loadImageFromFile(f))));
            reloadAssets();
        }
        else {
            reloadAssets();
        }
    }
}
