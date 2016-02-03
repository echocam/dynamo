package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * The template of the story from which the copy of the game required for each play through is derived.
 * 
 * @author tr393
 * @author rjm232
 *
 */
public class NarrativeTemplate extends MultiNarrative { // TODO Documentation
	
	/**
	 * @return
	 * @throws NullPointerException if the NarrativeInstance.start has not been set.
	 */
	public NarrativeInstance generateInstance() throws NullPointerException{ // TODO more appropriate exception(and change doc)?
		NarrativeInstance instance = new NarrativeInstance();
		
		if (this.start != null) {
			instance.start = this.start.copyToGraph(instance);
		} else {
			throw new NullPointerException("No node registered as start of graph.");
		}
		for (Node node : this.nodes.values()) {
			node.setCopied(false); // TODO bad encapsulation, but resets copy flag
		}
		instance.setActive(start);
		return instance;
	}
}
