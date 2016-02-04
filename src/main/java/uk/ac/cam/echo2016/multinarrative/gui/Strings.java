package uk.ac.cam.echo2016.multinarrative.gui;

/**
 * @author jr650, eyx20
 */
public class Strings{
    
    public static final String PROPERTY_ADDED = "Added Property: %1";
    public static final String PROPERTY_REMOVED = "Removed Property: %1";
    public static final String PROPERTY_VALUE = "Value%1";
    
    public static final String PROPERTY_MISSING = "Property %1 cannot be renamed: Property does not exist.";
    public static final String ADD_EMPTY_STRING = "Please type a property name";
    public static final String ALREADY_EXISTS = "Property %1 already exists.";
    public static final String INVALID_TYPE = "Type cannot be resolved.";
    public static final String PROPERTY_DOES_NOT_EXIST = "Property %1 does not exist.";

    

    private Strings(){}

    public static String populateString(String template, String... values){
        String r = template;
        for(int i = 0;i<values.length;i++){
            r = r.replace((CharSequence)("%"+(i+1)),values[i]);
        }
        return r;
    }
}
