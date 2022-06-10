package DatabaseConnection;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    /**
     * The singleton instance of the database connection.
     */
    private static Connection instance = null;

    /**
     * Constructor intializes the connection.
     */
    private DBConnection() {
        try {
            loadInstance();
        } catch (SQLException sqle) {
            System.err.println(sqle);
        } catch (FileNotFoundException fnfe) {
            try (OutputStream output = new FileOutputStream("db.props")){
                Properties props = new Properties();

                props.setProperty("jdbc.url", "jdbc:sqlite:cab302.db");
                props.setProperty("jdbc.username", "");
                props.setProperty("jdbc.password", "");
                props.setProperty("jdbc.schema", "");

                props.store(output, null);
                loadInstance();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadInstance() throws IOException, SQLException {
        Properties props = new Properties();
        FileInputStream in = null;

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        in = new FileInputStream("db.props");
        props.load(in);
        in.close();

        // specify the data source, username and password
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");
        String schema = props.getProperty("jdbc.schema");

        // get a connection
        instance = DriverManager.getConnection(url + "/" + schema, username,
                password);
    }

    /**
     * Provides global access to the singleton instance of the UrlSet.
     *
     * @return a handle to the singleton instance of the UrlSet.
     */
    public static Connection getInstance() {
        if (instance == null) {
            new DBConnection();
        }
        return instance;
    }
}