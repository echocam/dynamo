package uk.ac.cam.echo2016.multinarrative.gui;

import android.os.BaseBundle;
import static uk.ac.cam.echo2016.multinarrative.gui.Strings.*;

/**
 * @author jr650, eyx20
 */
public class GUIOperations{

    private BaseBundle properties = new BaseBundle();
    
    /**
     * TODO
     * Adds the required property
     * @throws IllegalOperationException if can't add property, 
     * message of exception is displayed to the user, using the Strings
     * class for formatting.
     */    
    public void addProperty(String s, String type) throws IllegalOperationException{
        if (s.equals("") || s == null) {
            throw new IllegalOperationException(ADD_EMPTY_STRING);
        }
        if (properties.containsKey(s)) {
            throw new IllegalOperationException(ALREADY_EXISTS);
        }
        switch (type) {
        case "String": 
            properties.putString(s, s);
            break;
        case "Integer":
            properties.putInt(s, Integer.parseInt(s));
            break;
        case "Boolean":
            properties.putBoolean(s, Boolean.parseBoolean(s));
            break;
        case "Byte":
            properties.putByte(s, Byte.parseByte(s));
            break;
        case "Short":
            properties.putShort(s, Short.parseShort(s));
            break;
        case "Long":
            properties.putLong(s, Long.parseLong(s));
            break;
        case "Float":
            properties.putFloat(s, Integer.parseInt(s));
            break;
        case "Double":
            properties.putDouble(s, Double.parseDouble(s));
            break;
        default:
            throw new IllegalOperationException(INVALID_TYPE);
        
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
            throw new IllegalOperationException(PROPERTY_DOES_NOT_EXIST);
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
        Object obj = properties.get(from);
        if (String.class.isInstance(obj)) {
            properties.putString(to, (String) obj);
        } else if (Integer.class.isInstance(obj)) {
            properties.putInt(to, (Integer) obj);
        } else if (Boolean.class.isInstance(obj)) {
            properties.putBoolean(to, (Boolean) obj);
        } else if (Byte.class.isInstance(obj)) {
            properties.putByte(to, (Byte) obj);
        } else if (Short.class.isInstance(obj)) {
            properties.putShort(to, (Short) obj);
        } else if (Long.class.isInstance(obj)) {
            properties.putLong(to, (Long) obj);
        } else if (Float.class.isInstance(obj)) {
            properties.putFloat(to, (Float) obj);
        } else if (Double.class.isInstance(obj)) {
            properties.putDouble(to, (Double) obj);
        } else {
            throw new IllegalOperationException("Property " + from + " cannot be renamed: "
                    + "Type cannot be resolved.");
        }
        
        properties.remove(from);
    }

}
