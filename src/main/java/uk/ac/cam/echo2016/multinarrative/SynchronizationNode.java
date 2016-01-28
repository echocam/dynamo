package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393
 *
 */
public class SynchronizationNode extends Node { //TODO Documentation
	public SynchronizationNode(String id) {
		super(id);
	}
	public android.os.BaseBundle startNarrative(Narrative option) { //TODO Finish Impl
		return option.getProperties();
	}
	public GameChoice onEntry (Narrative completed, NarrativeInstance instance) { // TODO finish impl
		if (!this.getOptions().contains(completed)) {} //TODO Exception needed???
		
		return null;
	}
}
