package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393
 *
 */
import java.util.ArrayList;

public abstract class Node { //TODO Documentation
	private String id;
	protected android.os.BaseBundle properties;
	protected ArrayList<Narrative> options;
	private boolean copied = false;

	public Node(String id) {
		this.id = id;
		this.properties = new android.os.BaseBundle();
		this.options = new ArrayList<Narrative>();
	}
	
	public Node(Node node, NarrativeInstance instance) { // TODO check + todo's
		this.id = node.id;
		this.properties = new android.os.BaseBundle(node.properties);
		this.options = new ArrayList<Narrative>();
		node.copied = true;
		for(Narrative narr : node.options) {
			Node endCopy;
			if (narr.getEnd().copied == false) {
				endCopy = narr.getEnd().copy(instance);
			} else {
				endCopy = instance.getNode(narr.getEnd().getIdentifier());
			}
			this.options.add(new Narrative(narr.getIdentifier(),this,endCopy));
		}
	}
	public abstract Node copy(NarrativeInstance instance);

	public abstract android.os.BaseBundle startNarrative(Narrative option);

	public abstract GameChoice onEntry(Narrative played, NarrativeInstance instance);

	public String getIdentifier() {
		return id;
	}

	public android.os.BaseBundle getProperties() {
		return properties;
	}

	public ArrayList<Narrative> getOptions() {
		return options;
	}
}
