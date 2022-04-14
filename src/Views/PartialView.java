package Views;

import javax.swing.*;

public abstract class PartialView extends JPanel {
    protected MainView view;

    public PartialView(MainView view){
        this.view = view;
    }
}
