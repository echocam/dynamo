package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;

/**
 * 
 * Represents an actual play through of the story. Instantiated from the template.
 * 
 * @author tr39
 * @author rjm232
 *
 */
public class NarrativeInstance extends MultiNarrative { // TODO Documentation
	protected android.os.BaseBundle properties;
	protected ArrayList<Node> activeNodes = new ArrayList<Node>();

	public NarrativeInstance() {
		properties = new android.os.BaseBundle();
	}

	public NarrativeInstance(NarrativeTemplate template) throws NullPointerException{ // TODO check copy constructor
		NarrativeInstance base = template.generateInstance();
		this.narratives = base.narratives;
		this.nodes = base.nodes;
		this.start = base.start;
		this.properties = base.properties;
	}

	public android.os.BaseBundle getGlobalProperties() {
		return properties;
	}

	public android.os.BaseBundle getNarrativeProperties(String id) {
		return getNarrative(id).getProperties();
	}

	public android.os.BaseBundle getNodeProperties(String id) {
		return getNode(id).getProperties();
	}

	public android.os.BaseBundle startNarrative(String id) {
		// TODO do whatever needs to be done when starting a narrative
		return getNarrativeProperties(id);
	}

	public GameChoice endNarrative(String id) {
		Narrative finished = getNarrative(id);
		return finished.getEnd().onEntry(finished, this);
	}

	/**
	 * Recursively deletes an item from the graph according to the instance this method is called from. Only nodes and
	 * narratives further down the tree are deleted, so nodes must have no entering narratives and narratives must start
	 * from a node with other options available.
	 * 
	 * @param id
	 *            string identifier for the item to be deleted
	 * @return
	 */
	public boolean kill(String id) { // TODO More Documentation, including overloaded methods
		Narrative narr = getNarrative(id);
		Node node = getNode(id); // TODO search might be optimizable (2nd not required)

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
		Node nEnd = narr.getEnd();
		
		int narrEntries = nEnd.getProperties().getInt("Impl.Node.Entries"); // TODO improve naming convention?
		--narrEntries;
		nEnd.getProperties().putInt("Impl.Node.Entries", narrEntries);
		
		if (narrEntries == 0) {
			kill(nEnd);
		}
		// TODO and update event if instanceof sync node? i.e. change to ACTION_CONTINUE?

		Node nStart = narr.getStart();
		nStart.getOptions().remove(narr); // TODO should return true, otherwise something's broken

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
		for (Narrative narr : new ArrayList<Narrative>(node.getOptions())) {
			kill(narr); // copy of ArrayList used to allow deletion of nodes within the function
		}

		assert node.getProperties().getInt("Impl.Node.Entries") == 0;
		
		nodes.remove(node.getIdentifier());
		return true;
	}

	public ArrayList<Narrative> getPlayableNarratives() { // TODO implementation + todo's
		return null;
	}

	public void setActive(Node node) {
		activeNodes.add(node);
	}
}
