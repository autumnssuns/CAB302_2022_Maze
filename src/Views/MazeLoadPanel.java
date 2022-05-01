package Views;

import DatabaseConnection.MazeDataSource;
import Models.MazeDataModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class MazeLoadPanel extends JPanel implements ActionListener {
    private MazeDataModel model;
    private JButton deleteButton, loadButton;
    private JPanel namePanel, datesPanel, previewPanel;
    private JLabel nameLabel, authorLabel, creationDateLabel, modifiedDateLabel;
    private JTextArea descriptionTextArea;
    private MazeLoadMenuPartialView container;
    private int IMAGE_SIZE = 90;
    private MazeDataSource dataSource;

    public MazeLoadPanel(MazeDataModel model, MazeLoadMenuPartialView container){
        dataSource = new MazeDataSource();

        this.container = container;
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(300, 150));
        this.setPreferredSize(new Dimension(300, 150));
        this.setMaximumSize(new Dimension(300, 150));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        this.model = model;
        createNamePanel();
        createDatesPanel();
        createDescriptionTextArea();
        createPreviewHolder();

        add(namePanel, BorderLayout.PAGE_START);
        add(datesPanel, BorderLayout.PAGE_END);
        add(descriptionTextArea, BorderLayout.CENTER);
        add(previewPanel, BorderLayout.LINE_START);
    }

    private void createPreviewHolder() {
        previewPanel = new JPanel(new FlowLayout());
        Image myPicture = model.icon().getImage();
        //            myPicture = ImageIO.read(new File("assets/100_100_Maze_BW.png"));
        // https://www.baeldung.com/java-resize-image

        BufferedImage resizedImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(myPicture, 0, 0, IMAGE_SIZE, IMAGE_SIZE, null);
        graphics2D.dispose();

        loadButton = new JButton(new ImageIcon(resizedImage));
        loadButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        loadButton.addActionListener(this);
        previewPanel.add(loadButton);
    }

    private void createNamePanel(){
        namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

        nameLabel = new JLabel(model.name(), SwingConstants.LEFT);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));

        authorLabel = new JLabel(String.format("By %s", model.author()), SwingConstants.LEFT);
        authorLabel.setFont(authorLabel.getFont().deriveFont(Font.ITALIC, 10f));
        authorLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        JPanel deletePanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.weightx = 1;

        deleteButton = new JButton("X");
        deleteButton.setMargin(new Insets(0,0,0,0));
        deleteButton.setPreferredSize(new Dimension(20,20));
//        deleteButton.setMaximumSize(new Dimension(10,10));
        deleteButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        deleteButton.setFont(deleteButton.getFont().deriveFont(10f));
        deleteButton.addActionListener(this);

        deletePanel.add(deleteButton, c);

        namePanel.add(nameLabel);
        namePanel.add(authorLabel);
        namePanel.add(deletePanel);
    }

    private void createDatesPanel(){
        datesPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 2);
        datesPanel.setLayout(gridLayout);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyy hh:mm");

        creationDateLabel = new JLabel(String.format("Created: %s", model.createdTime().format(formatter)), SwingConstants.LEFT);
        creationDateLabel.setFont(creationDateLabel.getFont().deriveFont(Font.ITALIC, 10f));

        modifiedDateLabel = new JLabel(String.format("Modified: %s", model.modifiedTime().format(formatter)), SwingConstants.RIGHT);
        modifiedDateLabel.setFont(modifiedDateLabel.getFont().deriveFont(Font.ITALIC, 10f));

        datesPanel.add(creationDateLabel, GroupLayout.Alignment.LEADING);
        datesPanel.add(modifiedDateLabel, GroupLayout.Alignment.TRAILING);
    }

    private void createDescriptionTextArea(){
        descriptionTextArea = new JTextArea(model.description());
        descriptionTextArea.setEditable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteButton){
            dataSource.deleteMaze(model.name());
            container.reloadMazes();
        }

        if (e.getSource() == loadButton){
            MazeDataModel mazeModel = dataSource.getMaze(model.name());
            container.loadMaze(mazeModel);
            System.out.println("Loading maze " + model.name());
        }
    }
}
