package Views;

import javax.swing.*;
import java.awt.*;

public class TransparentPanel extends JPanel {
    public TransparentPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        setOpaque(false);
    }

    public TransparentPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }

    public TransparentPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        setOpaque(false);
    }

    public TransparentPanel() {
        setOpaque(false);
    }
}
