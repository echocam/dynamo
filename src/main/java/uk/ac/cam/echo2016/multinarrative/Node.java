package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393
 *
 */
import java.util.ArrayList;

public abstract class Node { // TODO Documentation

	private final String id;
	protected android.os.BaseBundle properties;
	protected ArrayList<Narrative> options;
	private boolean copied = false;

	public Node(String id) {
		this.id = id;
		this.options = new ArrayList<Narrative>();
	}

	public Node(Node node, NarrativeInstance instance) { // TODO check + todo's
		this.id = node.id;
		if (properties != null) this.properties = new android.os.BaseBundle(node.properties);
		this.options = new ArrayList<Narrative>();
		node.copied = true;
		for(Narrative narrOrig : node.options) {
			Node endCopy;
			if (narrOrig.getEnd().copied == false) {
				endCopy = narrOrig.getEnd().copy(instance);
			} else {
				endCopy = instance.getNode(narrOrig.getEnd().getIdentifier());
			}
			Narrative narrCopy = new Narrative(narrOrig.getIdentifier(),this,endCopy);
			this.options.add(narrCopy);
			instance.narratives.add(narrCopy);
		}
		instance.nodes.add(this);
	}

	protected void setCopied(boolean bool) { // TODO WARNING - should only be accessed by a template
		copied = bool;
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
