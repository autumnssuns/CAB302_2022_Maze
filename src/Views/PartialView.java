package Views;

import javax.swing.*;

public abstract class PartialView extends JLayeredPane {
    protected MainView view;

    public PartialView(MainView view){
        this.view = view;
    }
}
