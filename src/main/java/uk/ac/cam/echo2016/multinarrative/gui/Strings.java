package uk.ac.cam.echo2016.multinarrative.gui;

/**
 * @author jr650
 */
public class Strings{
    
    public static final String PROPERTY_ADDED = "Added Property: %1";

    private Strings(){}

    public static String populateString(String template, String... values){
        String r = template;
        for(int i = 0;i<values.length;i++){
            r = r.replace((CharSequence)("%"+(i+1)),values[i]);
        }
        return r;
    }
}
