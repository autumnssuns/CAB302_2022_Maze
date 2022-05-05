package Views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AssetMenuPartialView extends PartialView implements ActionListener {
    private JButton addAssetButton;

    public AssetMenuPartialView(MainView view) {
        super(view);
        GridLayout layout = new GridLayout(3,1);
        this.setLayout(layout);
        addAssetButton = new JButton("Add asset");
        add(addAssetButton);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
