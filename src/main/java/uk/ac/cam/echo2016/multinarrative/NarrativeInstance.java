package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;

/**
 * 
 * @author tr393
 *
 */
public class NarrativeInstance extends MultiNarrative { // TODO Documentation
	protected android.os.BaseBundle properties;
	protected ArrayList<Node> activeNodes;

	public NarrativeInstance(NarrativeTemplate template) {
		NarrativeInstance base = template.generateInstance();
		// TODO copy constructor/clone
		this.properties = base.properties;

		// activeNodes.add(start);// TODO breaking encapsulation..?, getStart()
		// required?
	}

	public android.os.BaseBundle getGlobalProperties() {
		return properties;
	}

	public android.os.BaseBundle getNarrativeProperties(String id) {
		return super.getNarrative(id).properties;
	}

	public android.os.BaseBundle getNodeProperties(String id) {
		return super.getNode(id).properties;
	}

	public android.os.BaseBundle startNarrative(String id) {
		// TODO do whatever needs to be done when starting a narrative
		return getNarrativeProperties(id);
	}

	public GameChoice endNarrative(String id) {
		Narrative finished = getNarrative(id);
		return finished.end.onEntry(finished, this);
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
	public boolean kill(String id) { // TODO More Documentation, including overloaded methods
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
	 * @see NarrativeInstance#kill(String)
	 */
	public boolean kill(Narrative narr) {
		if (narr == null)
			return false;
		Node nEnd = narr.end; // TODO assumes nEnd isn't null (see
								// Narrative.java)

		/*
		 * TODO retrieve special node properties if this is the only narrative
		 * entering the node, kill(nEnd); else update node entry narratives
		 */
		// TODO and update event if instanceof sync node? i.e. change to
		// ACTION_CONTINUE?

		Node nStart = narr.start; // TODO assumes nStart isn't null (see
									// Narrative.java)
		nStart.getOptions().remove(narr); // TODO should return true, otherwise
											// something's broken

		// assert nStart has other options
		narratives.remove(narr);
		return true;
	}
	/**
	 * {@link NarrativeInstance#kill(String)}
	 * @see NarrativeInstance#kill(String)
	 */
	public boolean kill(Node node) {
		if (node == null)
			return false;
		for (Narrative narr : node.getOptions()) {
			kill(narr);
		}

		// assert no narratives leading into node
		nodes.remove(node);
		return true;
	}

	public ArrayList<Narrative> getPlayableNarratives() { // TODO implementation + todo's
		return null;
	}

	public void setActive(Node node) {
		activeNodes.add(node);
	}
}
