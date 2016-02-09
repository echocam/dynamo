package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;

/**
 * @author tr393
 * @version 1.0
 */
public class GameChoice { //TODO Todo's and documentation
    
    public static final int ACTION_MAJOR_DECISION = 0;
    public static final int ACTION_CHOOSE_ROUTE = 1;
    public static final int ACTION_CONTINUE = 2;
    
    protected ArrayList<Route> options;
    protected boolean eventTrigger;
    protected String eventIdentifier;
    protected int action;

    public boolean hasEvent() {return eventTrigger;}
    
    public String getEventIdentifier() {return eventIdentifier;}
    
    public int getAction() {return action;}
    
    public ArrayList<Route> getOptions() {return options;}

}
