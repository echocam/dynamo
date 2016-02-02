package uk.ac.cam.echo2016.multinarrative;

/**
 * Represents a major decision made in a narrative that can affect which sync point a character will end up in. Has one
 * narrative entering, and several leaving.
 * 
 * @author tr393, rjm232
 *
 */
public class ChoiceNode extends Node { // TODO implementation and documentation

	public ChoiceNode(String id) {
		super(id);
	}

	/**
	 * Effectively a clone method.
	 */
	protected Node callConstructor(String id) {
		return new ChoiceNode(id);
	}

	public android.os.BaseBundle startNarrative(Narrative option) { // TODO Finish Impl
		return null;
	};

	public GameChoice onEntry(Narrative completed, NarrativeInstance instance) { // TODO Finish Impl
		return null;
	}

}
