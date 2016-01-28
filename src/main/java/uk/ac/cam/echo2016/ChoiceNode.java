package uk.ac.cam.echo2016;

/**
 * 
 * @author tr393
 *
 */
public class ChoiceNode extends Node { //TODO Todo's and documentation
	
	public ChoiceNode(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	public android.os.BaseBundle startNarrative(Narrative option) {return null;};
	
	public GameChoice onEntry(Narrative completed, NarrativeInstance instance) {return null;}
	
}
