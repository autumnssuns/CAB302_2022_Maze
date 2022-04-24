package DatabaseConnection;

import Models.MazeDataModel;
import Models.MazeNode;
import Models.MazeNodeDataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class MazeDataSource {
    public static final String CREATE_MAZES_TABLE =
            "CREATE TABLE IF NOT EXISTS mazes ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE," // from https://stackoverflow.com/a/41028314
                    + "name VARCHAR(30),"
                    + "author VARCHAR(30),"
                    + "description VARCHAR(20),"
                    + "rowsCount INTEGER,"
                    + "colsCount INTEGER"
                    + ");";

    public static final String CREATE_MAZE_NODES_TABLE =
            "CREATE TABLE IF NOT EXISTS mazeNodes   ("
                    + "mazeIdx INTEGER NOT NULL,"
                    + "idx INTEGER NOT NULL,"
                    + "neighbours VARCHAR(30),"
                    + "PRIMARY KEY (mazeIdx, idx),"
                    + "FOREIGN KEY(mazeIdx) REFERENCES mazes(idx)"
                    + ");";

    private static final String INSERT_MAZE = "INSERT INTO mazes (name, author, description, rowsCount, colsCount) VALUES (?, ?, ?, ?, ?);";

    private static final String INSERT_NODE = "INSERT INTO mazeNodes (mazeIdx, idx, neighbours) VALUES (?, ?, ?);";

    private static final String GET_NAMES = "SELECT name FROM mazes;";

    private static final String GET_MAZE = "SELECT * FROM mazes WHERE name=?;";

    private static final String GET_MAZE_NODES = "SELECT * FROM mazeNodes where idx=?;";

    private static final String DELETE_MAZE = "DELETE FROM mazes WHERE name=?;";

    private static final String DELETE_NODE_BY_MAZE_IDX = "DELETE FROM mazeNodes WHERE mazeIdx=?;";

    private Connection connection;

    private PreparedStatement addMaze, addNode, getNames, getMaze, getMazeNodes, editMaze, deleteMaze, deleteNodeByMazeIdx;

    public MazeDataSource(){
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_MAZES_TABLE);
            st.execute(CREATE_MAZE_NODES_TABLE);

            addMaze = connection.prepareStatement(INSERT_MAZE);
            addNode = connection.prepareStatement(INSERT_NODE);
            getNames = connection.prepareStatement(GET_NAMES);
            getMaze = connection.prepareStatement(GET_MAZE);
            getMazeNodes = connection.prepareStatement(GET_MAZE_NODES);
            deleteMaze = connection.prepareStatement(DELETE_MAZE);
            deleteNodeByMazeIdx = connection.prepareStatement(DELETE_NODE_BY_MAZE_IDX);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMaze(MazeDataModel maze){
        try {
            addMaze.setString(1, maze.name());
            addMaze.setString(2, maze.author());
            addMaze.setString(3, maze.description());
            addMaze.setInt(4, maze.rowsCount());
            addMaze.setInt(5, maze.colsCount());
            addMaze.executeUpdate();

            ResultSet generatedKeys = addMaze.getGeneratedKeys();
            int mazeIdx;
            if (generatedKeys.next()){
                mazeIdx = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating maze failed, no ID obtained.");
            }

            maze.mazeNodes().neighbours().forEach((x, y) -> {
                try {
                    addNode.setInt(1, mazeIdx);
                    addNode.setInt(2, x);
                    addNode.setString(3, y);
                    addNode.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            addNode.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MazeDataModel getMaze(String name){
        try {
            getMaze.setString(1, name);
            ResultSet rs = getMaze.executeQuery();
            rs.next();
            int mazeIdx = rs.getInt("idx");
            String mazeName = rs.getString("name");
            String mazeAuthor = rs.getString("author");
            String mazeDescription = rs.getString("description");
            int mazeRowsCount = rs.getInt("rowsCount");
            int mazeColsCount = rs.getInt("colsCount");
            rs.close();

            getMazeNodes.setInt(1, mazeIdx);
            rs = getMazeNodes.executeQuery();
            HashMap<Integer, String> neighbours = new HashMap<>();
            while(rs.next()){
                int key = rs.getInt("idx");
                String value = rs.getString("neighbours");
                neighbours.put(key, value);
            }
            return new MazeDataModel(mazeIdx, mazeName, mazeAuthor, mazeDescription, mazeRowsCount, mazeColsCount, new MazeNodeDataModel(neighbours));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<String> getNames(){
        Set<String> names = new TreeSet<>();
        try {
            ResultSet rs = getNames.executeQuery();
            while(rs.next()){
                names.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public void editMaze(MazeDataModel model){

    }

    public void deleteMaze(String name){
        try {
            int idxToDelete = getMaze(name).idx();
            deleteNodeByMazeIdx.setInt(1, idxToDelete);
            deleteNodeByMazeIdx.executeUpdate();

            deleteMaze.setString(1, name);
            deleteMaze.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        /* BEGIN MISSING CODE */
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        /* END MISSING CODE */
    }
}
