package uk.ac.cam.echo2016.multinarrative;

import java.util.HashMap;

/**
 * 
 * The template of the story from which the copy of the game required for each
 * play through is derived.
 * 
 * <p>
 * ALT: A full {@code MultiNarrative} graph that is used to create new
 * {@code NarrativeInstance}s for each playthrough. This contains all nodes and
 * narratives on the graph designed in the {@code FXMLGUI} editor, and is the
 * template graph used for new save files. When the template is created, it
 * should not be modified in any way. (Unless the programmer wants different
 * behaviour across new playthroughs!)
 * 
 * @author tr393
 * @author rjm232
 * @version 1.0
 * @see NarrativeTemplate#generateInstance()
 * @see NarrativeInstance
 * @see MultiNarrative
 */
public class NarrativeTemplate extends MultiNarrative {
    private static final long serialVersionUID = 1;

    // TODO Documentation
    /**
     *
     * @return
     * @throws NullPointerException
     *             if the NarrativeInstance.start has not been set.
     */
    public NarrativeInstance generateInstance() throws NullPointerException { // TODO
									      // more
									      // appropriate
									      // exception?
	HashMap<String, Node> r_nodes = new HashMap<>();
	HashMap<String, Narrative> r_narrs = new HashMap<>();

	for (Node node : nodes.values()) {
	    Node r_node = node.clone();
	    r_node.createProperties();
	    r_nodes.put(node.getIdentifier(), r_node);
	}

	for (Narrative narr : narratives.values()) {
	    Narrative r_narr = narr.clone();
	    r_narr.createProperties();
	    
	    Node start = r_nodes.get(narr.getStart().getIdentifier());
	    r_narr.start = start;
	    start.getOptions().add(r_narr);
	    
	    Node end = r_nodes.get(narr.getEnd().getIdentifier());
	    r_narr.end = end;

	    end.getProperties().putInt("Impl.Node.Entries", end.getProperties().getInt("Impl.Node.Entries",0)+1);
	    
	    r_narrs.put(narr.getIdentifier(), r_narr);
	}

	Node r_start = r_nodes.get(start.getIdentifier());

	NarrativeInstance instance = new NarrativeInstance(r_nodes, r_narrs, r_start);
	return instance;
    }
}
