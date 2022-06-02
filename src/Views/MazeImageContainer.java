package Views;

import Models.AssetDataModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MazeImageContainer extends JPanel {
    private int cellSize, borderSize;
    private MazePartialView container;
    private JLabel imageContainer;
    private static int RESIZE_SIZE = 50, RESIZE_WEIGHT = 5;
    private AssetDataModel assetDataModel;
    private static int VERTICAL = 0, HORIZONTAL = 1, EDGE = 2;
    private ImageIcon image;
    private int currentRowSpan, currentColSpan;
    private HashMap<String, JPanel> resizePanels;

    public MazeImageContainer(AssetDataModel assetDataModel, int cellSize, int borderSize, MazePartialView container){
        this.assetDataModel = assetDataModel;
        currentColSpan = 2;
        currentRowSpan = 2;

        int imageWidth = currentColSpan * cellSize - borderSize;
        int imageHeight = currentRowSpan * cellSize - borderSize;
        ImageIcon icon = assetDataModel.icon();
        BufferedImage resizedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(icon.getImage(), 0, 0, imageWidth, imageHeight, null);
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

        resizePanels = new HashMap<>();

        JPanel topResize = createResizePanel(HORIZONTAL, 0);
        addResizeMouseListener(topResize, ImageResizeMouseHandler.NORTH);
        JPanel bottomResize = createResizePanel(HORIZONTAL, RESIZE_WEIGHT + image.getIconHeight());
        addResizeMouseListener(bottomResize, ImageResizeMouseHandler.SOUTH);
        JPanel leftResize = createResizePanel(VERTICAL, 0);
        addResizeMouseListener(leftResize, ImageResizeMouseHandler.WEST);
        JPanel rightResize = createResizePanel(VERTICAL, RESIZE_WEIGHT + image.getIconWidth());
        addResizeMouseListener(rightResize, ImageResizeMouseHandler.EAST);
        JPanel topLeftResize = createEdgeResizePanel(0,0);
        addResizeMouseListener(topLeftResize, ImageResizeMouseHandler.NORTH, ImageResizeMouseHandler.WEST);
        JPanel topRightResize = createEdgeResizePanel(RESIZE_WEIGHT + image.getIconWidth(),0);
        addResizeMouseListener(topRightResize, ImageResizeMouseHandler.NORTH, ImageResizeMouseHandler.EAST);
        JPanel bottomLeftResize = createEdgeResizePanel(0,RESIZE_WEIGHT + image.getIconHeight());
        addResizeMouseListener(bottomLeftResize, ImageResizeMouseHandler.SOUTH, ImageResizeMouseHandler.WEST);
        JPanel bottomRightResize = createEdgeResizePanel(RESIZE_WEIGHT + image.getIconWidth(),RESIZE_WEIGHT + image.getIconHeight());
        addResizeMouseListener(bottomRightResize, ImageResizeMouseHandler.SOUTH, ImageResizeMouseHandler.EAST);

        resizePanels.put("N", topResize);
        resizePanels.put("S", bottomResize);
        resizePanels.put("W", leftResize);
        resizePanels.put("E", rightResize);
        resizePanels.put("NW", topLeftResize);
        resizePanels.put("NE", topRightResize);
        resizePanels.put("SW", bottomLeftResize);
        resizePanels.put("SE", bottomRightResize);

        setLocation(borderSize, borderSize);
        addMouseListeners();
        addKeyListeners();

        add(imageContainer);
        imageContainer.setBounds(RESIZE_WEIGHT, RESIZE_WEIGHT, image.getIconWidth(), image.getIconHeight());
        this.setBounds(this.getBounds().x - RESIZE_WEIGHT, this.getBounds().y - RESIZE_WEIGHT, this.getBounds().width, this.getBounds().height);
        this.setOpaque(false);

        container.blockRegion(this.getLocation().x, this.getLocation().y, this.getWidth(), this.getHeight());
    }


    private void updateResizeLocations(){
        resizePanels.get("N").setLocation((image.getIconWidth() - RESIZE_SIZE) / 2 + RESIZE_WEIGHT, 0);
        resizePanels.get("S").setLocation((image.getIconWidth() - RESIZE_SIZE) / 2 + RESIZE_WEIGHT, RESIZE_WEIGHT + image.getIconHeight());
        resizePanels.get("W").setLocation(0, (image.getIconHeight() - RESIZE_SIZE) / 2 + RESIZE_WEIGHT);
        resizePanels.get("E").setLocation(RESIZE_WEIGHT + image.getIconWidth(), (image.getIconHeight() - RESIZE_SIZE) / 2 + RESIZE_WEIGHT);
        resizePanels.get("NW").setLocation(0,0);
        resizePanels.get("NE").setLocation(RESIZE_WEIGHT + image.getIconWidth(),0);
        resizePanels.get("SW").setLocation(0,RESIZE_WEIGHT + image.getIconHeight());
        resizePanels.get("SE").setLocation(RESIZE_WEIGHT + image.getIconWidth(),RESIZE_WEIGHT + image.getIconHeight());
    }

    private Image getResizedImage(int width, int height){
        ImageIcon icon = assetDataModel.icon();
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(icon.getImage(), 0, 0, width, height, null);
        graphics2D.dispose();
        return resizedImage;
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

    private void addResizeMouseListener(JPanel resizePanel, Integer... orientations){
        List<Integer> orientationList = new ArrayList<>(List.of(orientations));
        ImageResizeMouseHandler imageResizeMouseHandler = new ImageResizeMouseHandler(orientations);
        resizePanel.addMouseListener(imageResizeMouseHandler);
        resizePanel.addMouseMotionListener(imageResizeMouseHandler);
        Boolean north = orientationList.contains(ImageResizeMouseHandler.NORTH);
        Boolean south = orientationList.contains(ImageResizeMouseHandler.SOUTH);
        Boolean east = orientationList.contains(ImageResizeMouseHandler.EAST);
        Boolean west = orientationList.contains(ImageResizeMouseHandler.WEST);
        if (north) resizePanel.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        if (south) resizePanel.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        if (east) resizePanel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        if (west) resizePanel.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
        if (north && east) resizePanel.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
        if (north && west) resizePanel.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
        if (south && east) resizePanel.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        if (south && west) resizePanel.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
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
                resizePanels.forEach((k,v) -> add(v));
                repaint();
                revalidate();
//                setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
//                setBorder(BorderFactory.createEmptyBorder());
                resizePanels.forEach((k,v) -> remove(v));
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
            container.blockRegion(component.getLocation().x, component.getLocation().y, component.getWidth(), component.getHeight());
        }
    }

    private class ImageResizeMouseHandler extends MouseAdapter{
        private Point offset;
        private final ArrayList<Integer> orientations;
        public static final Integer
                NORTH = 0,
                EAST = 1,
                SOUTH = 2,
                WEST = 3;

        public ImageResizeMouseHandler(Integer... orientations){
            this.orientations = new ArrayList<>(List.of(orientations));
        }

        @Override
        public void mousePressed(MouseEvent e) {
            JPanel panel = (JPanel) e.getComponent();
            container.moveToFront(panel);
            offset = e.getPoint();
            panel.requestFocus();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getPoint().x - offset.x;
            x -= x % cellSize;
            int y = e.getPoint().y - offset.y;
            y -= y % cellSize;

            Component component = e.getComponent();
            Point location = component.getLocation();

            if (orientations.contains(EAST) || orientations.contains(WEST)) location.x += x;
            if (orientations.contains(NORTH) || orientations.contains(SOUTH)) location.y += y;

            component.setLocation(location);

            Point containerLoc = getLocation();

            Image newImg;
            int newWidth = image.getIconWidth();
            int newHeight = image.getIconHeight();

            if (orientations.contains(EAST)){
                newWidth = location.x - imageContainer.getX();
            }
            if (orientations.contains(WEST)){
//                if (x < containerLoc.x + imageContainer.getWidth()) newWidth = imageContainer.getWidth() - x + RESIZE_WEIGHT;
                containerLoc.x += x;
                setLocation(containerLoc);
            }
            if (orientations.contains(SOUTH)){
                newHeight = location.y - imageContainer.getY();
            }
            if (orientations.contains(NORTH)) {
//                if (y < 0) newHeight = imageContainer.getHeight() - y + RESIZE_WEIGHT;
                containerLoc.y += y;
                setLocation(containerLoc);
            }
            updateResizeLocations();
            newImg = getResizedImage(newWidth, newHeight);
            image.setImage(newImg);
            // Resize containers (image label, and MazeImageContainer)
            imageContainer.setBounds(RESIZE_WEIGHT, RESIZE_WEIGHT, image.getIconWidth(), image.getIconHeight());
            setPreferredSize(new Dimension(image.getIconWidth() + RESIZE_WEIGHT * 2, image.getIconHeight() + RESIZE_WEIGHT * 2));
            setSize(getPreferredSize().width, getPreferredSize().height);
        }
    }
}