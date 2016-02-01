package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * Represents a synchronization node, where multiple characters are allowed to interact. Can have multiple
 * entering and multiple exiting narratives.
 * 
 * @author tr393, rjm232
 *
 */
public class SynchronizationNode extends Node { //TODO Documentation
	public SynchronizationNode(String id) {
		super(id);
	}
//	public SynchronizationNode(Node node, NarrativeInstance instance) {
//		super(node, instance);
//	}
	public Node callConstructor(String id) {
		return new SynchronizationNode(id);
	}
	
	public android.os.BaseBundle startNarrative(Narrative option) { //TODO Finish Impl
		return option.getProperties();
	}
	
	public GameChoice onEntry (Narrative completed, NarrativeInstance instance) { // TODO finish impl
		if (!this.getOptions().contains(completed)) {} //TODO Exception needed???
		
		return null;
	}
}
