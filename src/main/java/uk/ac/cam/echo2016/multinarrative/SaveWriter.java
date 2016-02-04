package uk.ac.cam.echo2016.multinarrative;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveWriter {
    public static void saveObject(String filename, Object toSave) throws IOException{
        ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(filename));
        ois.writeObject(toSave);
    }
}
