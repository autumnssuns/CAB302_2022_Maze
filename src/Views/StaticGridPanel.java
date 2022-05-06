package Views;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StaticGridPanel extends JPanel {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int numPerBox;
    private int count;
    private ArrayList<JPanel> boxes;
    private int direction;

    public StaticGridPanel(int numPerBox, int direction){
        this.setLayout(new BoxLayout(this, direction));
        boxes = new ArrayList<>();
        this.numPerBox = numPerBox;
        this.direction = direction;
        count = 0;
        setVisible(true);
    }

    @Override
    public Component add(Component comp) {
        int boxIndex = count / numPerBox;
        while (boxes.size() <= boxIndex){
            addBox();
        }
        count++;
        return boxes.get(boxIndex).add(comp);
    }

    @Override
    public void removeAll(){
        super.removeAll();
        boxes.removeAll(boxes);
        count = 0;
    }

    private void addBox(){
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, direction == HORIZONTAL ? BoxLayout.Y_AXIS : BoxLayout.X_AXIS));
        boxes.add(box);
        super.add(box, 0);
        box.setVisible(true);
    }
}
