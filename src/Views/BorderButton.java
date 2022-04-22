package Views;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class BorderButton extends JButton implements MouseListener {

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
        addMouseListener(this);
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

    public void preview(){
        if (!toggleable) return;
        closed = !closed;
        linkedNodeButton.forEach(nodeButton -> nodeButton.toggleAll(this));
    }

    public void setToggleable(boolean toggleable){
        this.toggleable = toggleable;
    }

    public boolean isToggleable() {
        return toggleable;
    }

    public void updateBorder(){
        paintBorder(this.closed);
    }

    public void paintBorder(boolean closed){
        setBackground(closed ? Color.BLACK : Color.WHITE);
        setOpaque(closed);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
    private boolean clicked = false;
    @Override
    public void mouseReleased(MouseEvent e) {
        if (!clicked){
            action();
            clicked = true;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        action();
        setOpaque(true);
        setBackground(Color.GRAY);
        getParent().repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!clicked){
            action();
        }
        clicked = false;
        updateBorder();
        getParent().repaint();
    }
}
