package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * The template of the story from which the copy of the game required for each play through is derived.
 * 
 * @author tr393, rjm232
 *
 */
public class NarrativeTemplate extends MultiNarrative { // TODO Todo's and documentation
	public NarrativeInstance generateInstance() {
		NarrativeInstance instance = new NarrativeInstance();
		
		instance.start = this.start.copyToGraph(instance);
		for (Node node : this.nodes.values()) {
			node.setCopied(false); // TODO bad encapsulation, but resets copy flag
		}
		return instance;
	}
}
