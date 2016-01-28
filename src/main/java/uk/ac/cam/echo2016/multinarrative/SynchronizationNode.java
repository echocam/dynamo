package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393
 *
 */
public class SynchronizationNode extends Node { //TODO Todo's and documentation
	public SynchronizationNode(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	public android.os.BaseBundle startNarrative(Narrative option) {
		return option.getProperties();
	}
	public GameChoice onEntry (Narrative completed, NarrativeInstance instance) {
		if (!this.getOptions().contains(completed)) {} //TODO Exception needed???
		
		int count = 0;
		for(Narrative narr : this.getOptions()) {
			
		}
		return null;
	}
}
