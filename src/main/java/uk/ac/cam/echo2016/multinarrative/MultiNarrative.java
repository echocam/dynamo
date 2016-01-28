package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;

/**
 * 
 * @author tr393
 *
 */
public class MultiNarrative { //TODO Documentation
	protected ArrayList<Narrative> narratives;
	protected ArrayList<Node> nodes;
	protected Node start;

	public Node getNode(String id) {
		for(Node node : nodes) {
			if (node.getIdentifier().equals(id)) return node;
		}
		return null; //TODO add proper exception
	}

	public Narrative getNarrative(String id) {
		for(Narrative narr : narratives) {
			if (narr.getIdentifier().equals(id)) return narr;
		}
		return null; //TODO add proper exception
	}

	// TODO Add getStartNode()?
}
