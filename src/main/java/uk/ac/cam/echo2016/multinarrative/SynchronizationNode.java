package uk.ac.cam.echo2016.multinarrative;

import android.os.BaseBundle;

/**
 * Represents a synchronization node, where multiple characters are allowed to interact. Can have multiple entering and
 * multiple exiting routes.
 * 
 * <p>
 * ALT: Implements a {@link Node} at an intersection point on the {@code MultiNarrative} graph structure. At this point,
 * multiple {@code Route}s interact and affect each other's gameplay. There should be at least 2 {@code Route}s
 * entering the {@code Node} for the {@code Node} to be meaningful. The most typical use would be to have the same number of entering
 * {@code Route}s also exiting (one for each character), but this isn't required.
 * 
 * @author tr393
 * @author rjm232
 * @version 1.0
 * @see Node
 * @see ChoiceNode
 * @see MultiNarrative
 */
public class SynchronizationNode extends Node { // TODO Documentation
	private static final long serialVersionUID = 1;
	public SynchronizationNode(String id) {
		super(id);
	}

    protected Node create(String id) {
        return new SynchronizationNode(id);
    }

    public BaseBundle startRoute(Route option) {
        return option.getProperties();
    }
    
    private boolean isCompleted() {
    	for (Route route : getEntering()) {
    		if (!route.getProperties().containsKey("System.isCompleted")) {
    			return false;
    		}
    	}
    	
    	return true;
    }

    public GameChoice onEntry(Route completed, NarrativeInstance instance) throws GraphElementNotFoundException {
        if (!getEntering().contains(completed)) {
        	throw new GraphElementNotFoundException("completed route not in this nodes entering list");
        } 
        
        GameChoice gameChoice;
        if (getEntering().size() == 1 && getExiting().size() == 1) {
        	gameChoice = new GameChoice(true, getId(), GameChoice.ACTION_CONTINUE, getExiting());
        } else if (isCompleted()){
        	gameChoice = new GameChoice(true, getId(), GameChoice.ACTION_CHOOSE_ROUTE, instance.getPlayableRoutes());
        } else {
        	gameChoice = new GameChoice(false, null, GameChoice.ACTION_CHOOSE_ROUTE, instance.getPlayableRoutes());
        }
        
        
        return gameChoice;
    }
}
