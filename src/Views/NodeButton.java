package Views;

import Models.MazeNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

public class NodeButton extends JButton implements MouseListener {
    public MazeNode node;
    public BorderButton topWall, bottomWall, leftWall, rightWall;
    public NodeButton top, bottom, left, right;

    public NodeButton(MazeNode node){
        this.node = node;
        node.attachButton(this);
        addMouseListener(this);
//        this.addActionListener(e -> changeColour());
    }

    public void connectAll(){
        top = node.getTop() != null? node.getTop().getAttachedButton() : null;
        bottom = node.getBottom() != null ? node.getBottom().getAttachedButton() : null;
        left = node.getLeft() != null? node.getLeft().getAttachedButton() : null;
        right = node.getRight() != null? node.getRight().getAttachedButton() : null;
    }

    public void repaintWalls(){
        if (topWall.isToggleable()) topWall.setClosed(node.getTop() == null);
        if (bottomWall.isToggleable()) bottomWall.setClosed(node.getBottom() == null);
        if (leftWall.isToggleable()) leftWall.setClosed(node.getLeft() == null);
        if (rightWall.isToggleable()) rightWall.setClosed(node.getRight() == null);
    }

    public void paintWall(String location, boolean closed){
        switch (location){
            case "top" -> {
                if (topWall.isToggleable()) topWall.paintBorder(closed);
            }
            case "bottom" -> {
                if (bottomWall.isToggleable()) bottomWall.paintBorder(closed);
            }
            case "left" -> {
                if (leftWall.isToggleable()) leftWall.paintBorder(closed);
            }
            case "right" -> {
                if (rightWall.isToggleable()) rightWall.paintBorder(closed);
            }
        }
    }

    public void changeColour(){
        Random rand = new Random();
        float r = rand.nextFloat() / 2f + 0.5f;
        float g = rand.nextFloat() / 2f + 0.5f;
        float b = rand.nextFloat() / 2f + 0.5f;

        Color color = new Color(r, g, b);
        ArrayList<MazeNode> visitedNodes = new ArrayList<>();
        visitedNodes.add(node);
        changeColour(color, visitedNodes);
    }

    public void changeColour(Color color, ArrayList<MazeNode> visitedNodes){
        final Color finalColor = color;
        int delay = 0;
        Timer timer = new Timer( delay, e -> {
            float r = finalColor.getRed() / 255f * 0.99f;
            float g = finalColor.getGreen() / 255f * 0.99f;
            float b = finalColor.getBlue() / 255f * 0.99f;
            Color newColor = new Color(r,g,b);
            this.setBackground(newColor);
            ArrayList<MazeNode> next = node.getNeighbours();
            next.removeIf(x -> x == null || visitedNodes.contains(x));
            for (MazeNode node : next){
                visitedNodes.add(node);
                if (node != null && node.getAttachedButton().getBackground() != color) node.getAttachedButton().changeColour(newColor, visitedNodes);
            }
        });
        timer.setRepeats( false );
        timer.start();
    }

    public void attachTopButton(BorderButton topButton){
        this.topWall = topButton;
        topButton.linkNodeButton(this);
//        topButton.addActionListener(e -> {
//            toggleTop();
//        });
    }

    public void attachBottomButton(BorderButton bottomButton){
        this.bottomWall = bottomButton;
        bottomButton.linkNodeButton(this);
//        bottomButton.addActionListener(e -> {
//            toggleBottom();
//        });
    }

    public void attachLeftButton(BorderButton leftButton){
        this.leftWall = leftButton;
        leftButton.linkNodeButton(this);
//        leftButton.addActionListener(e -> {
//            toggleLeft();
//        });
    }

    public void attachRightButton(BorderButton rightButton){
        this.rightWall = rightButton;
        rightButton.linkNodeButton(this);
//        rightButton.addActionListener(e -> {
//            toggleRight();
//        });
    }

    public void toggleLeft() {
        boolean state = leftWall.getClosed();
        if (left != null){
            left.getNode().setRight(!state ? node : null);
            node.setLeft(!state ? left.getNode() : null);
        }
    }

    public void toggleRight() {
        boolean state = rightWall.getClosed();
        if (right != null){
            right.getNode().setLeft(!state ? node : null);
            node.setRight(!state ? right.getNode() : null);
        }
    }

    public void toggleTop(){
        boolean state = topWall.getClosed();
        if (top != null){
            top.getNode().setBottom(!state ? node : null);
            node.setTop(!state ? top.getNode() : null);
        }
    }

    public void toggleBottom() {
        boolean state = bottomWall.getClosed();
        if (bottom != null){
            bottom.getNode().setTop(!state ? node : null);
            node.setBottom(!state ? bottom.getNode() : null);
        }
    }

    public void toggleAll(){
        toggleTop();
        toggleBottom();
        toggleLeft();
        toggleRight();
    }

    public MazeNode getNode(){
        return node;
    }

    public void toggleAll(BorderButton source){
        if (source == topWall) toggleTop();
        if (source == bottomWall) toggleBottom();
        if (source == leftWall) toggleLeft();
        if (source == rightWall) toggleRight();
        repaintWalls();
    }

    public void removeWalls(NodeButton other){
        if (this.top == other) toggleTop();
        if (this.bottom == other) toggleBottom();
        if (this.left == other) toggleLeft();
        if (this.right == other) toggleRight();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        getParent().repaint();
        System.out.println("Attempt to reprint");
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}