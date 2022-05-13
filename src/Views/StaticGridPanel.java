package Views;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StaticGridPanel extends JPanel {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int numPerBox;
    private ArrayList<JPanel> boxes;
    private ArrayList<Component> components;
    private int direction;

    public StaticGridPanel(int numPerBox, int direction){
        this.setLayout(new BoxLayout(this, direction));
        boxes = new ArrayList<>();
        components = new ArrayList<>();
        this.numPerBox = numPerBox;
        this.direction = direction;
        setVisible(true);
    }

    @Override
    public Component add(Component comp) {
        int boxIndex = components.size() / numPerBox;
        while (boxes.size() <= boxIndex){
            addBox();
        }
        components.add(comp);
        return boxes.get(boxIndex).add(comp);
    }

    public void reload(){
        for (int compIndex = 0; compIndex < components.size(); compIndex++){
            int boxIndex = compIndex / numPerBox;
            while (boxes.size() <= boxIndex){
                addBox();
            }
            boxes.get(boxIndex).add(components.get(compIndex));
        }
    }

    public void setCountPerBox(int numPerBox){
        this.numPerBox = numPerBox;
        reload();
    }

    @Override
    public void removeAll(){
        super.removeAll();
        boxes.removeAll(boxes);
        components.removeAll(components);
    }

    @Override
    public void remove(Component c){
        super.remove(c);
        components.remove(c);
        reload();
    }

    private void addBox(){
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, direction == HORIZONTAL ? BoxLayout.Y_AXIS : BoxLayout.X_AXIS));
        boxes.add(box);
        super.add(box);
        box.setVisible(true);
    }
}