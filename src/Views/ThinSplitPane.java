package Views;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

public class ThinSplitPane extends JSplitPane {
    private int lineSize = 1;

    public ThinSplitPane(int newOrientation, Component newLeftComponent, Component newRightComponent){
        super(newOrientation, newLeftComponent, newRightComponent);
        setUI(new BasicSplitPaneUI(){
            @Override
            public BasicSplitPaneDivider createDefaultDivider()
            {
                return new BasicSplitPaneDivider(this)
                {
                    public void setBorder(Border b) {}

                    @Override
                    public void paint(Graphics g)
                    {
                        g.setColor(Color.GRAY);
                        g.fillRect(0, 0, newOrientation == VERTICAL_SPLIT ? getSize().width : lineSize, newOrientation == HORIZONTAL_SPLIT ? getSize().height : 1);
                        super.paint(g);
                    }
                };
            }
        });
        setBorder(BorderFactory.createMatteBorder(0, 0, 0,0, Color.GRAY));
    }
}
