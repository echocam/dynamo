package uk.ac.cam.echo2016.multinarrative.gui.operations;

/**
 * @author jr650, eyx20
 */
public class Strings{
    
    public static final String PROPERTY_ADDED = "Added Property: %1";
    public static final String PROPERTY_REMOVED = "Removed Property: %1";
    public static final String PROPERTY_VALUE = "Value%1";
    
    public static final String PROPERTY_MISSING = "Property %1 cannot be renamed: Property does not exist.";
    public static final String PROPERTY_RENAME_EXISTS = "Property cannot be renamed to %2: already exists."; 
    public static final String ADD_EMPTY_STRING = "Please type a property name";
    public static final String ALREADY_EXISTS = "Property %1 already exists.";
    public static final String INVALID_TYPE = "Type cannot be resolved.";
    public static final String PROPERTY_DOES_NOT_EXIST = "Property %1 does not exist.";
    public static final String NODE_ALREADY_EXISTS = "A node with that name already exists";
    public static final String NODE_DOES_NOT_EXIST = "No node with that name exists";
    public static final String NODE_ALREADY_AT_POSITION = "Node already exists at given position.";
    public static final String ROUTE_ALREADY_EXISTS = "A route with this name already exists";
    public static final String COULD_NOT_RENAME = "Couldn't rename %1";

    public static final String ROUTE_PREFIX = "Route-%1";
    public static final String NODE_PREFIX = "Node-%1";
    public static final String SYNCH_NODE_PREFIX = "SynchNode-";

    private Strings(){}

    public static String populateString(String template, Object... values){
        String r = template;
        for(int i = 0;i<values.length;i++){
            r = r.replace((CharSequence)("%"+(i+1)),values[i].toString());
        }
        return r;
    }
    
    public static String populateString(String template, String... values){
        String r = template;
        for(int i = 0;i<values.length;i++){
            r = r.replace((CharSequence)("%"+(i+1)),values[i]);
        }
        return r;
    }
}
