package Views;

import javax.swing.*;
import java.awt.*;

public class StaticGridPanel extends JPanel {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int numPerBox;
    private int count;

    public StaticGridPanel(int numPerBox, int direction){
        this.setLayout(new BoxLayout(this, direction));
        this.numPerBox = numPerBox;
        count = 0;
    }

    @Override
    public Component add(Component comp) {
        count++;
        return super.add(comp);
    }
}
