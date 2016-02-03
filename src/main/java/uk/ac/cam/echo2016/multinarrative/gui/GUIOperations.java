package uk.ac.cam.echo2016.multinarrative.gui;

import android.os.BaseBundle;

/**
 * @author jr650, eyx20
 */
public class GUIOperations{

    /**
     * TODO
     * Adds the required property
     * @throws IllegalOperationException if can't add property, 
     * message of exception is displayed to the user, using the Strings
     * class for formatting.
     */
    
    private BaseBundle properties = new BaseBundle();
    
    
    public void addProperty(String s) throws IllegalOperationException{
        properties.putString(s, s);
    }
    
    /**
     * TODO
     * Removes the required property
     * @throws IllegalOperationException if can't remove property, 
     * message of exception is displayed to the user, using the Strings
     * class for formatting.
     */
    public void removeProperty(String s) throws IllegalOperationException{
        properties.remove(s);
    }
    
    /**
     * TODO
     * Changes the name of the required property
     * @throws IllegalOperationException if can't rename property, 
     * message of exception is displayed to the user, using the Strings
     * class for formatting.
     */
    public void renameProperty(String from, String to) throws IllegalOperationException{
        String obj = (String) properties.get(from);
        properties.putString(to, obj);
        properties.remove(from);
    }

}
