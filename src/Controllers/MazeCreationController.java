package Controllers;

import DatabaseConnection.MazeDataSource;
import Models.Maze;
import Models.MazeDataModel;
import Models.MazeNode;
import Models.MazeNodeDataModel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class MazeCreationController {
    public static void addOrEditMaze(String name, String author, String description, Maze maze, ImageIcon icon){
        int idx = -1;
        int rowsCount = maze.getSize(0);
        int colsCount = maze.getSize(1);

        ArrayList<MazeNode> nodes = new ArrayList<>();
        HashMap<Integer, String> neighbours = new HashMap<>();

        for (MazeNode node : maze){
            nodes.add(node);
        }

        for (int i = 0; i < nodes.size(); i++){
            MazeNode node = nodes.get(i);
            String neighboursStr = node.getNeighbours().stream().filter(Objects::nonNull).map(x -> String.valueOf(nodes.indexOf(x))).collect(Collectors.joining(","));
            neighbours.put(i, neighboursStr);
        }

        MazeDataModel model = new MazeDataModel(idx, name, author, description, LocalDateTime.now(), LocalDateTime.now(), icon, maze.getGeneratorType(), maze.getSeed(), rowsCount, colsCount, new MazeNodeDataModel(neighbours));

        MazeDataSource dataSource = new MazeDataSource();
        dataSource.editMaze(model);
    }
}