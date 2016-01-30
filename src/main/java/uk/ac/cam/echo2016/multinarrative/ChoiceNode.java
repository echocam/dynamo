package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393
 *
 */
public class ChoiceNode extends Node { // TODO Todo's and documentation

	public ChoiceNode(String id) {
		super(id);
	}

	public ChoiceNode(Node node, NarrativeInstance instance) {
		super(node, instance);
	}

	public Node copy(NarrativeInstance instance) {
		return new ChoiceNode(this, instance);
	}

	public android.os.BaseBundle startNarrative(Narrative option) {
		return null;
	};

	public GameChoice onEntry(Narrative completed, NarrativeInstance instance) {
		return null;
	}

}
