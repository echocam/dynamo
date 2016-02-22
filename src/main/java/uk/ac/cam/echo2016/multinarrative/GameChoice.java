package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;

/**
 * The object returned by {@code NarrativeInstance} when a {@code Route} is completed. The action 
 * stored by the object is determined by the {@code onEntry} function in {@code Node}, implemented
 * in both {@code SynchronizationNode} and {@code ChoiceNode}.
 * 
 * @see NarrativeInstance#endRoute
 * @see Node#onEntry
 * @see SynchronizationNode#onEntry
 * @see ChoiceNode#onEntry
 *  
 * @author tr393
 * @author rjm232
 * @version 1.0
 */
public class GameChoice { 

    public static final int ACTION_MAJOR_DECISION = 0;
    public static final int ACTION_CHOOSE_ROUTE = 1;
    public static final int ACTION_CONTINUE = 2;

    protected ArrayList<Route> options;
    protected boolean eventTrigger;
    protected String eventIdentifier;
    protected int action;

    public GameChoice(boolean isTriggered, String nodeId, int actionName, ArrayList<Route> choices) {
        eventTrigger = isTriggered;
        eventIdentifier = nodeId;
        action = actionName;
        options = choices;
    }

    public boolean hasEvent() {
        return eventTrigger;
    }

    public String getEventIdentifier() {
        return eventIdentifier;
    }

    public int getAction() {
        return action;
    }

    public ArrayList<Route> getOptions() {
        return options;
    }
}
