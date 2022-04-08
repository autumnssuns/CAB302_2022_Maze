import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BorderButtonMouseAdapter extends MouseAdapter {
    @Override
    public void mouseEntered(MouseEvent e) {
//        super.mouseEntered(e);
        BorderButton sourceButton = (BorderButton)e.getSource();
        sourceButton.setOpaque(true);
        sourceButton.setBackground(Color.GRAY);
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        super.mouseExited(e);
        BorderButton sourceButton = (BorderButton)e.getSource();
        sourceButton.updateBorder();
    }
}
