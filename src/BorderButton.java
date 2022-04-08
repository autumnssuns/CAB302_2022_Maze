import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BorderButton extends JButton {

    private boolean closed;
    private boolean toggleable = true;
    private ArrayList<NodeButton> linkedNodeButton;

    public boolean getClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
        updateBorder();
    }

    public void linkNodeButton(NodeButton nodeButton){
        this.linkedNodeButton.add(nodeButton);
    }

    public void unlinkNodeButton(NodeButton nodeButton){
        this.linkedNodeButton.remove(nodeButton);
    }

    public BorderButton(){
        super();
        addMouseListener(new BorderButtonMouseAdapter());
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        setClosed(false);
        linkedNodeButton = new ArrayList<>();
    }

    public BorderButton(ActionListener actionListener){
        this();
        this.addActionListener(actionListener);
    }

    public void action(){
        if (!toggleable) return;
        setClosed(!closed);
        linkedNodeButton.forEach(nodeButton -> nodeButton.toggleAll(this));
    }

    public void setToggleable(boolean toggleable){
        this.toggleable = toggleable;
    }

    public boolean isToggleable() {
        return toggleable;
    }

    public void updateBorder(){
        setBackground(this.closed ? Color.BLACK : Color.WHITE);
        setOpaque(closed);
//        setBorderPainted(!state);
    }
}
