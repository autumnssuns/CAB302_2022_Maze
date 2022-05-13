package Models;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public record AssetDataModel(int index, String name, ImageIcon icon) {

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