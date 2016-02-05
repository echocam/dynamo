package uk.ac.cam.echo2016.multinarrative.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import uk.ac.cam.echo2016.multinarrative.GUINarrative;
import uk.ac.cam.echo2016.multinarrative.NarrativeInstance;
import uk.ac.cam.echo2016.multinarrative.NarrativeTemplate;

public class SaveReader {
    public static GUINarrative loadGUINarrative(String filename) throws IOException{
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            return (GUINarrative) ois.readObject();
        } catch(ClassNotFoundException e){
            throw new IOException();
        }
    }

    public static NarrativeInstance loadNarrativeInstance(String filename) throws IOException{
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            return (NarrativeInstance) ois.readObject();
        } catch(ClassNotFoundException e){
            throw new IOException();
        }
    }

    public static NarrativeTemplate loadNarrativeTemplate(String filename) throws IOException{
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            return (NarrativeTemplate) ois.readObject();
        } catch(ClassNotFoundException e){
            throw new IOException();
        }
    }
}
