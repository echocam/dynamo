package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;

/**
 * @author tr393
 * @version 1.0
 */
public class GameChoice { //TODO Todo's and documentation
    
    public static final int ACTION_MAJOR_DECISION = 0;
    public static final int ACTION_CHOOSE_ROUTE = 0;
    public static final int ACTION_CONTINUE = 0;
    
    protected boolean eventTrigger;
    protected boolean eventIdentifier;
    protected int action;

    public boolean hasEvent() {return false;}
    
    public String getEventIdentifier() {return null;}
    
    public int getAction() {return 0;}
    
    public ArrayList<Route> getOptions() {return null;}

}
