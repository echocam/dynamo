package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.BaseBundle;

/**
 * 
 * Represents an actual play through of the story. Instantiated from the
 * template.
 * 
 * @author tr39
 * @author rjm232
 * @version 1.0
 *
 */
public class NarrativeInstance extends MultiNarrative { // TODO Documentation
    private static final long serialVersionUID = 1;
    protected BaseBundle properties;
    protected ArrayList<Node> activeNodes = new ArrayList<Node>();

    public NarrativeInstance() {
	properties = new BaseBundle();
    }

    public NarrativeInstance(HashMap<String, Node> nodes, HashMap<String, Narrative> narratives, Node start)
	    throws NullPointerException {
	this.nodes = nodes;
	this.narratives = narratives;
	this.activeNodes = new ArrayList<>();
	this.activeNodes.add(start);
	this.properties = new BaseBundle();
    }

    public BaseBundle getGlobalProperties() {
	return properties;
    }

    public BaseBundle getNarrativeProperties(String id) {
	return getNarrative(id).getProperties();
    }

    public BaseBundle getNodeProperties(String id) {
	return getNode(id).getProperties();
    }

    public BaseBundle startNarrative(String id) {
	Narrative narr = getNarrative(id);
	narr.start.startNarrative(narr);
	return narr.getProperties();
    }

    public GameChoice endNarrative(String id) {
	Narrative finished = getNarrative(id);
	return finished.getEnd().onEntry(finished, this);
    }

    /**
     * Recursively deletes an item from the graph according to the instance this
     * method is called from. Only nodes and narratives further down the tree
     * are deleted, so nodes must have no entering narratives and narratives
     * must start from a node with other options available.
     * 
     * @param id
     *            string identifier for the item to be deleted
     * @return
     */
    public boolean kill(String id) { // TODO More Documentation, including
				     // overloaded methods
	Narrative narr = getNarrative(id);
	Node node = getNode(id); // TODO search might be optimizable (2nd not
				 // required)

	if (narr != null) { // TODO alternate exception handling?
	    kill(narr);
	    return true;
	} else if (node != null) { // TODO alternate exception handling?
	    kill(node);
	    return true;
	}
	return false;
    }

    /**
     * {@link NarrativeInstance#kill(String)}
     * 
     * @see NarrativeInstance#kill(String)
     */
    public boolean kill(Narrative narr) {
	if (narr == null)
	    return false;

	System.out.println("Killing: " + narr.getIdentifier());

	Node nEnd = narr.getEnd();

	String s = nEnd == null ? "null"
		: nEnd + " " + (nEnd.getProperties() == null ? "null"
			: nEnd.getProperties() + " " + nEnd.getProperties().getInt("Impl.Node.Entries"));

	System.out.println(s);

	int narrEntries = nEnd.getProperties().getInt("Impl.Node.Entries"); // TODO
									    // improve
									    // naming
									    // convention?
	--narrEntries;
	nEnd.getProperties().putInt("Impl.Node.Entries", narrEntries);

	if (narrEntries == 0) {
	    kill(nEnd);
	}

	Node nStart = narr.getStart();
	nStart.getOptions().remove(narr); // TODO should return true, otherwise
					  // something's broken

	narratives.remove(narr.getIdentifier());
	return true;
    }

    /**
     * {@link NarrativeInstance#kill(String)}
     * 
     * @see NarrativeInstance#kill(String)
     */
    public boolean kill(Node node) {
	if (node == null)
	    return false;

	System.out.println("Killing: " + node.getIdentifier());

	assert node.getProperties().getInt("Impl.Node.Entries") == 0;

	for (Narrative narr : new ArrayList<>(node.getOptions())) {// Duplicates
								   // list so
								   // modifications
								   // don't
								   // affect
								   // iteration
	    kill(narr);
	}

	nodes.remove(node.getIdentifier());
	return true;
    }

    public ArrayList<Narrative> getPlayableNarratives() { // TODO implementation
							  // + todo's
	return null;
    }

    public void setActive(Node node) {
	activeNodes.add(node);
    }
}
