package uk.ac.cam.echo2016.multinarrative.gui;

import java.util.HashMap;

import android.os.BaseBundle;
import static uk.ac.cam.echo2016.multinarrative.gui.Strings.*;

/**
 * @author jr650, eyx20
 */
public class GUIOperations{

    private HashMap<String, BaseBundle> properties;
    
    /**
     * TODO
     * Adds the required property
     * @throws IllegalOperationException if can't add property, 
     * message of exception is displayed to the user, using the Strings
     * class for formatting.
     */    
    public void addProperty(String s) throws IllegalOperationException{
        if (s.equals("") || s == null) {
            throw new IllegalOperationException(ADD_EMPTY_STRING);
        }
        if (properties.containsKey(s)) {
            throw new IllegalOperationException(ALREADY_EXISTS);
        }
        properties.put(s, new BaseBundle());
        
    }
    
    public void addValue(String property, String type, String value) throws IllegalOperationException {
        if (!properties.containsKey(property)) {
            throw new IllegalOperationException(PROPERTY_DOES_NOT_EXIST);
        }
        switch (type) {
        case "String": 
            properties.get(property).putString(value, value);
            break;
        case "Integer":
            properties.get(property).putInt(value, Integer.parseInt(value));
            break;
        case "Boolean":
            properties.get(property).putBoolean(value, Boolean.parseBoolean(value));
            break;
        case "Byte":
            properties.get(property).putByte(value, Byte.parseByte(value));
            break;
        case "Short":
            properties.get(property).putShort(value, Short.parseShort(value));
            break;
        case "Long":
            properties.get(property).putLong(value, Long.parseLong(value));
            break;
        case "Float":
            properties.get(property).putFloat(value, Integer.parseInt(value));
            break;
        case "Double":
            properties.get(property).putDouble(value, Double.parseDouble(value));
            break;
        default:
            throw new IllegalOperationException("Type " + type + " connot be resolved.");
        
        }
    }
    
    /**
     * TODO
     * Removes the required property
     * @throws IllegalOperationException if can't remove property, 
     * message of exception is displayed to the user, using the Strings
     * class for formatting.
     */
    public void removeProperty(String s) throws IllegalOperationException{
        if (!properties.containsKey(s)) {
            throw new IllegalOperationException("Property " + s + " does not exist.");
        }
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
        if (!properties.containsKey(from)) {
            throw new IllegalOperationException(PROPERTY_MISSING);
        }
        if (properties.containsKey(to)) {
            throw new IllegalOperationException("Property cannot be renamed to " + to + ": "
                    + to + " already exists.");
        }

        BaseBundle oldprop = properties.get(from);
        properties.put(to, oldprop);

        properties.remove(from);
    }
    
    /**
     * TODO
     * Gets a name that's not already in the graph...
     * @return
     */
    public String getUniqueNodeName(){
	return "Node";
    }
    
    /**
     * TODO
     * Adds a node, throwing exception if it fails.
     */
    public void addSynchNode(String name, double x, double y) throws IllegalOperationException{}
    
    /**
     * TODO
     * Repositions a node by the given offset
     */
    public void translateNode(String name, double x, double y) {}

    /**
     * TODO
     * Gets a name that's not already in the graph...
     * @return
     */
    public String getUniqueNarrativeName(){
	return "Narrative";
    }
    
    /**
     * TODO
     * Adds a narrative, throwing exception if it fails.
     */
    public void addNarrative(String name, String start, String end)throws IllegalOperationException{
	
    }
}
