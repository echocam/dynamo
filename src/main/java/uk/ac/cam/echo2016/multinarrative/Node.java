package uk.ac.cam.echo2016.multinarrative;

import java.io.Serializable;
import java.util.ArrayList;

import javax.management.RuntimeErrorException;

import android.os.BaseBundle;

/**
 * Represents a {@code Node} on the {@code MultiNarrative} graph structure. Each node has an {@code ArrayList} of {@code Narrative} references leaving the {@code Node}.
 * 
 * @author tr393
 * @author rjm232
 * @version 1.0
 *
 */
public abstract class Node implements Serializable, Cloneable { // TODO Documentation
	private static final long serialVersionUID = 1;
	private final String id;
	private BaseBundle properties;
	private ArrayList<Narrative> options;
	private boolean copied = false; // flag used in graph copy that indicates whether this node has been passed

	public Node(String id) {
		this.id = id;
		this.options = new ArrayList<Narrative>();
	}

	/**
	 * Copies this node and its narratives and recursively calls this method on the nodes reached by the narratives
	 * further down the graph. The copy created is then returned. The graph instance is used to record node/narrative
	 * references and make sure that nodes are not copied twice. The callConstructor method is effectively a clone
	 * method. :P
	 * 
	 * @param instance
	 */
	// TODO change callConstructor to use clone() instead?
	// TODO move to NarrativeTemplate and copy through the hashmap?
	public Node copyToGraph(NarrativeInstance instance) { // TODO More Documentation!!! and tests

		// Eventually calls Node(this.id) via subclass's constructor
		Node result = this.callConstructor(this.id);

		if (this.properties != null) // Copy properties across, if any
			result.properties = new BaseBundle(this.properties);

		this.copied = true; // TODO encapsulation of copied flag

		// Copy each narrative leaving this node and call copyGraph on their end nodes
		for (Narrative narrTemplate : this.options) {
			Node endNodeCopy;
			if (narrTemplate.getEnd().copied == false) {
				// Not already copied

				endNodeCopy = narrTemplate.getEnd().copyToGraph(instance); // Recursively copy nodes at the ends of
																			// narratives

				// Create and update entryList property
				endNodeCopy.createProperties();

				endNodeCopy.properties.putInt("Impl.Node.Entries", 1);
			} else {
				// Already copied

				endNodeCopy = instance.getNode(narrTemplate.getEnd().getIdentifier()); // Get reference to copied end

				// Update entryList property

				int narrEntries = endNodeCopy.properties.getInt("Impl.Node.Entries");
				narrEntries++;
				endNodeCopy.properties.putInt("Impl.Node.Entries", narrEntries);
			}

			// Create narrative using references obtained/created above, linking this node to the new end nodes
			Narrative narrCopy = new Narrative(narrTemplate.getIdentifier(), result, endNodeCopy);

			// Update narrative references
			result.options.add(narrCopy);
			// Update graph references
			instance.narratives.put(narrCopy.getIdentifier(), narrCopy);
		}
		instance.nodes.put(result.getIdentifier(), result);
		return result;
	}
	@Override
	public Node clone() {
	    try {
	        Node clone = (Node) super.clone();
	        clone.properties = BaseBundle.deepcopy(this.properties);
	        
	        return clone;
	    } catch (CloneNotSupportedException e) {
	        throw new RuntimeException(e);
	    }
	}
	protected void resetCopied() { // TODO warning - should not be concurrently called while the
									// NarrativeTemplate.getInstance() method is running!
		copied = false;
	}

	/**
	 * Method is implemented in derived classes ChoiceNode and SyncNode, to allow this class to make new objects of
	 * those derived types in the copyToGraph method.
	 * 
	 * @param id
	 * @return
	 */
	protected abstract Node callConstructor(String id);

	public abstract BaseBundle startNarrative(Narrative option);

	public abstract GameChoice onEntry(Narrative played, NarrativeInstance instance);

	public String getIdentifier() {
		return id;
	}

	public void createProperties() {
		if (properties == null)
			properties = new BaseBundle(); // TODO Initialize with default starting size?
	}

	public BaseBundle getProperties() {
		return properties;
	}

	public void setProperties(BaseBundle b) {
		properties = b;
	}

	public ArrayList<Narrative> getOptions() {
		return options;
	}
}
