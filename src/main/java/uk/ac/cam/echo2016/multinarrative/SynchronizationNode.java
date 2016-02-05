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

    protected Node newInstance(String id) {
        return new SynchronizationNode(id);
    }

    public BaseBundle startRoute(Route option) {
        return option.getProperties();
    }

    public GameChoice onEntry(Route completed, NarrativeInstance instance) { // TODO finish impl
        GameChoice gameChoice = new GameChoice();
        if (!this.getOptions().contains(completed)) {
        } // TODO Exception needed???
        
        //TODO initialise gameChoice
        
        return gameChoice;
    }
}
