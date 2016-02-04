package uk.ac.cam.echo2016.multinarrative;

/**
 * Represents a synchronization node, where multiple characters are allowed to interact. Can have multiple entering and
 * multiple exiting narratives.
 * 
 * <p>
 * ALT: Implements a {@link Node} at an intersection point on the {@code MultiNarrative} graph structure. At this point,
 * multiple {@code Narrative}s interact and affect each other's gameplay. There should be at least 2 {@code Narrative}s
 * entering the {@code Node} for the {@code Node} to be meaningful. The most typical use would be to have the same number of entering
 * {@code Narrative}s also exiting (one for each character), but this isn't required.
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

    protected Node callConstructor(String id) {
        return new SynchronizationNode(id);
    }

    public android.os.BaseBundle startNarrative(Narrative option) { // TODO Finish Impl
        return option.getProperties();
    }

    public GameChoice onEntry(Narrative completed, NarrativeInstance instance) { // TODO finish impl
        if (!this.getOptions().contains(completed)) {
        } // TODO Exception needed???

        return null;
    }
}
