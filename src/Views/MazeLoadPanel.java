package Views;

import Models.Maze;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MazeLoadPanel extends JPanel {
    private String mazeName, authorName;

    private JPanel namePanel, datesPanel, previewPanel;
    private JLabel nameLabel, authorLabel, creationDateLabel, modifiedDateLabel;
    private JTextArea descriptionTextArea;

    private int IMAGE_SIZE = 90;

    public MazeLoadPanel(String name){
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(300, 150));
        this.setPreferredSize(new Dimension(300, 150));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        mazeName = name;
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
        BufferedImage myPicture = null;
        try {
            myPicture = ImageIO.read(new File("assets/100_100_Maze_BW.png"));
            // https://www.baeldung.com/java-resize-image
            BufferedImage resizedImage = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(myPicture, 0, 0, IMAGE_SIZE, IMAGE_SIZE, null);
            graphics2D.dispose();

            JButton imageHolder = new JButton(new ImageIcon(resizedImage));
            imageHolder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            imageHolder.addActionListener(e -> {
                System.out.println("Reloading Maze");
            });
            previewPanel.add(imageHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNamePanel(){
        namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

        nameLabel = new JLabel(mazeName, SwingConstants.LEFT);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));

        authorLabel = new JLabel("By <author>", SwingConstants.LEFT);
        authorLabel.setFont(authorLabel.getFont().deriveFont(Font.ITALIC, 10f));
        authorLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        namePanel.add(nameLabel);
        namePanel.add(authorLabel);
    }

    private void createDatesPanel(){
        datesPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 2);
        datesPanel.setLayout(gridLayout);

        creationDateLabel = new JLabel("Created: dd/mm/yyyy hh:mm", SwingConstants.LEFT);
        creationDateLabel.setFont(creationDateLabel.getFont().deriveFont(Font.ITALIC, 8f));

        modifiedDateLabel = new JLabel("Modified: dd/mm/yyyy hh:mm", SwingConstants.RIGHT);
        modifiedDateLabel.setFont(modifiedDateLabel.getFont().deriveFont(Font.ITALIC, 8f));

        datesPanel.add(creationDateLabel, GroupLayout.Alignment.LEADING);
        datesPanel.add(modifiedDateLabel, GroupLayout.Alignment.TRAILING);
    }

    private void createDescriptionTextArea(){
        descriptionTextArea = new JTextArea("Author's description");
        descriptionTextArea.setEditable(false);
    }
}
