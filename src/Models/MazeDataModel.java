package Models;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.Objects;

public record MazeDataModel(int idx, String name, String author, String description, LocalDateTime createdTime, LocalDateTime modifiedTime, ImageIcon icon, int algorithm, long seed, int rowsCount, int colsCount, MazeNodeDataModel mazeNodes) {
    public Maze unpack(){
        Maze maze = new Maze(rowsCount, colsCount);
        maze.disconnectAll();
        mazeNodes.neighbours().forEach((x,y) -> mazeNodes.getNeighbours(x).forEach(n -> maze.get(x).connect(maze.get(n))));
        return maze;
    }

    public byte[] iconBlob(){
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(icon);
            byte[] data = byteStream.toByteArray();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}