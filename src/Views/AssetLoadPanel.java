package Views;

import DatabaseConnection.AssetsDataSource;
import Models.AssetDataModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class AssetLoadPanel extends JPanel implements ActionListener {
    private JTextField nameTextField;
    private JPanel assetNameDeletePanel;
    private TransparentPanel previewPanel;
    private JButton loadButton, deleteButton;
    private final int IMAGE_SIZE = 125;
    private AssetDataModel model;
    private AssetsDataSource dataSource;
    private AssetMenuPartialView container;

    public AssetLoadPanel(AssetDataModel model, AssetMenuPartialView container){
        this.container = container;
        dataSource = new AssetsDataSource();
        this.model = model;
        this.setMinimumSize(new Dimension(15, 150));
        this.setPreferredSize(new Dimension(15, 150));
        this.setMaximumSize(new Dimension(150, 150));
        this.setLayout(new BorderLayout());
//        this.setBackground(Color.GRAY);

        createNameField();
        createPreviewHolder();

        add(assetNameDeletePanel, BorderLayout.PAGE_END);
        add(previewPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void createNameField(){
        assetNameDeletePanel = new JPanel();
        assetNameDeletePanel.setLayout(new BoxLayout(assetNameDeletePanel, BoxLayout.X_AXIS));

        nameTextField = new JTextField(model.name());
        nameTextField.setCaretPosition(0);
        nameTextField.setEditable(false);
        nameTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        nameTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nameTextField.setEditable(true);
                nameTextField.setCaret(nameTextField.getCaret());
            }
        });
        nameTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                nameTextField.setEditable(false);
            }
        });
        nameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    nameTextField.setEditable(false);
                }
            }
        });
        nameTextField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0 ,5));
        nameTextField.setHorizontalAlignment(SwingConstants.CENTER);

        deleteButton = new JButton("X");
        deleteButton.setMargin(new Insets(0,0,0,0));
        deleteButton.setPreferredSize(new Dimension(15,15));
//        deleteButton.setMaximumSize(new Dimension(10,10));
        deleteButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        deleteButton.setFont(deleteButton.getFont().deriveFont(10f));
        deleteButton.addActionListener(this);

        assetNameDeletePanel.add(deleteButton);
        assetNameDeletePanel.add(nameTextField);
    }

    private void createPreviewHolder(){
        previewPanel = new TransparentPanel(new FlowLayout());
        Image myPicture = model.icon().getImage();
        // https://www.baeldung.com/java-resize-image
        // Resize Image to desired size
        BufferedImage resizedImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(myPicture, 0, 0, IMAGE_SIZE, IMAGE_SIZE, null);
        graphics2D.dispose();

        loadButton = new JButton(new ImageIcon(resizedImage));
        loadButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        loadButton.addActionListener(this);
        loadButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        previewPanel.add(loadButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteButton){
            dataSource.deleteAsset(model.index());
            container.reloadAssets();
        }
    }
}
