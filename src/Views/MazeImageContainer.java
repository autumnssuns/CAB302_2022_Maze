package Views;

import Models.AssetDataModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MazeImageContainer extends JPanel {
    private int cellSize, borderSize;
    private MazePartialView container;
    private JLabel imageContainer;
    private static int RESIZE_SIZE = 50, RESIZE_WEIGHT = 5;
    private AssetDataModel assetDataModel;
    private static int VERTICAL = 0, HORIZONTAL = 1, EDGE = 2;
    private ImageIcon image;
    private ArrayList<JPanel> resizePanels;

    public MazeImageContainer(AssetDataModel assetDataModel, int cellSize, int borderSize, MazePartialView container){
        int imageSize = 2 * cellSize - borderSize;
        ImageIcon icon = assetDataModel.icon();
        BufferedImage resizedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(icon.getImage(), 0, 0, imageSize, imageSize, null);
        graphics2D.dispose();
        image = new ImageIcon(resizedImage);

        imageContainer = new JLabel(image);
        setLayout(null);

        this.cellSize = cellSize;
        this.borderSize = borderSize;
        this.container = container;
        this.assetDataModel = assetDataModel;

        imageContainer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(image.getIconWidth() + RESIZE_WEIGHT * 2, image.getIconHeight() + RESIZE_WEIGHT * 2));
        setSize(this.getPreferredSize().width, this.getPreferredSize().height);

        resizePanels = new ArrayList<>();

        JPanel topResize = createResizePanel(HORIZONTAL, 0);
        JPanel bottomResize = createResizePanel(HORIZONTAL, RESIZE_WEIGHT + image.getIconHeight());
        JPanel leftResize = createResizePanel(VERTICAL, 0);
        JPanel rightResize = createResizePanel(VERTICAL, RESIZE_WEIGHT + image.getIconWidth());
        JPanel topLeftResize = createEdgeResizePanel(0,0);
        JPanel topRightResize = createEdgeResizePanel(RESIZE_WEIGHT + image.getIconWidth(),0);
        JPanel bottomLeftResize = createEdgeResizePanel(0,RESIZE_WEIGHT + image.getIconHeight());
        JPanel bottomRightResize = createEdgeResizePanel(RESIZE_WEIGHT + image.getIconWidth(),RESIZE_WEIGHT + image.getIconHeight());

        resizePanels.add(topResize);
        resizePanels.add(bottomResize);
        resizePanels.add(leftResize);
        resizePanels.add(rightResize);
        resizePanels.add(topLeftResize);
        resizePanels.add(topRightResize);
        resizePanels.add(bottomLeftResize);
        resizePanels.add(bottomRightResize);

        setLocation(borderSize, borderSize);
        addMouseListeners();
        addKeyListeners();

        add(imageContainer);
        imageContainer.setBounds(RESIZE_WEIGHT, RESIZE_WEIGHT, image.getIconWidth(), image.getIconHeight());
        this.setBounds(this.getBounds().x - RESIZE_WEIGHT, this.getBounds().y - RESIZE_WEIGHT, this.getBounds().width, this.getBounds().height);
        this.setOpaque(false);
    }

    private JPanel createEdgeResizePanel(int x, int y){
        JPanel resizePanel = new JPanel();
        resizePanel.setBounds(x, y, RESIZE_WEIGHT, RESIZE_WEIGHT);
        resizePanel.setBackground(Color.BLACK);
        resizePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        return resizePanel;
    }

    private JPanel createResizePanel(int orientation, int offset){
        int x = orientation == VERTICAL ? offset : (image.getIconWidth() - RESIZE_SIZE) / 2 + RESIZE_WEIGHT;
        int y = orientation == HORIZONTAL ? offset : (image.getIconHeight() - RESIZE_SIZE) / 2 + RESIZE_WEIGHT;
        int width = orientation == VERTICAL ? RESIZE_WEIGHT : RESIZE_SIZE;
        int height = orientation == HORIZONTAL ? RESIZE_WEIGHT : RESIZE_SIZE;
        JPanel resizePanel = new JPanel();
        resizePanel.setBounds(x, y, width, height);
        resizePanel.setBackground(Color.BLACK);
        resizePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        return resizePanel;
    }

    private void addKeyListeners(){
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_DELETE){
                    container.removeImage((MazeImageContainer) e.getSource());
                }
            }
        });
    }

    private void addMouseListeners(){
        ImageDragMouseHandler imageMouseHandler = new ImageDragMouseHandler();
        addMouseListener(imageMouseHandler);
        addMouseMotionListener(imageMouseHandler);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                resizePanels.forEach(p -> add(p));
                repaint();
                revalidate();
//                setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
//                setBorder(BorderFactory.createEmptyBorder());
                resizePanels.forEach(p -> remove(p));
                repaint();
                revalidate();
            }
        });
    }

    // https://stackoverflow.com/questions/27915214/how-can-i-drag-images-with-the-mouse-cursor-in-java-gui
    private class ImageDragMouseHandler extends MouseAdapter {
        private Point offset;

        @Override
        public void mousePressed(MouseEvent e) {
            MazeImageContainer label = (MazeImageContainer) e.getComponent();
            container.moveToFront(label);
            offset = e.getPoint();
            label.requestFocus();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getPoint().x - offset.x;
            x -= x % cellSize;
            int y = e.getPoint().y - offset.y;
            y -= y % cellSize;
            Component component = e.getComponent();
            Point location = component.getLocation();
            location.x += x;
            location.y += y;
            component.setLocation(location);
        }
    }
}
