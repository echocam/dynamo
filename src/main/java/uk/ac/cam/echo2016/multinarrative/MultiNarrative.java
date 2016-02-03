package uk.ac.cam.echo2016.multinarrative;

import java.util.HashMap;

import uk.ac.cam.echo2016.multinarrative.Narrative;
import uk.ac.cam.echo2016.multinarrative.Node;

/**
 * 
 * @author tr393
 * @author eyx20
 * @version 1.0
 * @see GUINarrative
 * @see MultiNarrative 
 */
public class MultiNarrative { //TODO Documentation
	protected HashMap<String, Narrative> narratives = new HashMap<String, Narrative>(); // TODO Warning: Narrative and Node must not
																		// have their .equals() method overridden
	protected HashMap<String, Node> nodes = new HashMap<String, Node>();
	protected Node start;

	public Node getNode(String id) {
        return nodes.get(id);
    }

    public Narrative getNarrative(String id) {
        return narratives.get(id);
    }
}
