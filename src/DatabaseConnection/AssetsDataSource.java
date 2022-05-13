package DatabaseConnection;

import Models.AssetDataModel;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AssetsDataSource {
    public static final String CREATE_ASSETS_TABLE =
            "CREATE TABLE IF NOT EXISTS assets ("
                    + "idx INTEGER PRIMARY KEY /*!40101 AUTO_INCREMENT */ NOT NULL UNIQUE,"
                    + "name VARCHAR(30),"
                    + "icon BLOB"
                    + ");";
    private static final String ADD_ASSET = "INSERT INTO assets (name, icon) VALUES (?,?);";
    private static final String GET_ALL_ASSETS = "SELECT * FROM assets;";
    private static final String DELETE_ASSET = "DELETE FROM assets WHERE idx = ?;";
    private static final String EDIT_ASSET = "UPDATE assets SET name = ?, icon = ? WHERE idx = ?;";

    private PreparedStatement addAsset, getAllAssets, deleteAsset, editAsset;
    private Connection connection;
    public AssetsDataSource(){
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_ASSETS_TABLE);

            addAsset = connection.prepareStatement(ADD_ASSET);
            getAllAssets = connection.prepareStatement(GET_ALL_ASSETS);
            deleteAsset = connection.prepareStatement(DELETE_ASSET);
            editAsset = connection.prepareStatement(EDIT_ASSET);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAsset(AssetDataModel assetDataModel){
        try {
            addAsset.setString(1, assetDataModel.name());
            addAsset.setBytes(2, assetDataModel.iconBlob());
            addAsset.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<AssetDataModel> getAllAssets(){
        Set<AssetDataModel> assets = new HashSet<>();
        try {
            ResultSet rs = getAllAssets.executeQuery();
            while (rs.next()){
                int idx = rs.getInt("idx");
                String name = rs.getString("name");
                byte[] data = rs.getBytes("icon");
                ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
                ImageIcon icon = (ImageIcon) objectInputStream.readObject();
                assets.add(new AssetDataModel(idx, name, icon));
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return assets;
    }

    public void deleteAsset(int idx){
        try {
            deleteAsset.setInt(1, idx);
            deleteAsset.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editAsset(AssetDataModel assetDataModel){
        try {
            editAsset.setString(1, assetDataModel.name());
            editAsset.setBytes(2, assetDataModel.iconBlob());
            editAsset.setInt(3, assetDataModel.index());
            editAsset.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
