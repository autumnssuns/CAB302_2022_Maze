package Views;

import javax.swing.*;
import java.awt.*;

public class AssetLoadPanel extends JPanel{
    private JTextField nameTextField;
    private JPanel jpanel;

    public AssetLoadPanel(){
        this.setMinimumSize(new Dimension(15, 150));
        this.setPreferredSize(new Dimension(15, 150));
        this.setMaximumSize(new Dimension(150, 150));

        this.setBackground(Color.GRAY);

        nameTextField = new JTextField("Image.png");
        this.add(nameTextField);

        setVisible(true);
    }
}
