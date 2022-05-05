package Utils;

import java.awt.*;

public class Graphics {
    public enum COLOR{
        LOAD_PANE_DEFAULT(new Color(255, 255, 255, 255)),
        LOAD_PANE_HOVER(new Color(0, 225, 195, 125)),
        LOAD_PANE_DELETE(new Color(255,55,55, 200)),
        LOAD_PANE_LOADED(new Color(255, 200, 0, 200)),
        ;

        private final Color color;
        COLOR(Color c){
            color = c;
        }

        public Color getColor() {
            return color;
        }
    }
}
