package uk.ac.cam.echo2016.multinarrative;

import java.util.HashMap;

/**
 * 
 * @author tr393
 *
 */
public class MultiNarrative { //TODO Documentation
	protected HashMap<String, Narrative> narratives = new HashMap<String, Narrative>(); // TODO Warning: Narrative and Node must not
																		// have their .equals() method overridden
	protected HashMap<String, Node> nodes = new HashMap<String, Node>();
	protected Node start;

	public Node getNode(String id) {
		for(Node node : nodes.values()) {
			if (node.getIdentifier().equals(id)) return node;
		}
		return null; //TODO add proper exception
	}

	public Narrative getNarrative(String id) {
		for(Narrative narr : narratives.values()) {
			if (narr.getIdentifier().equals(id)) return narr;
		}
		return null; //TODO add proper exception
	}
}
