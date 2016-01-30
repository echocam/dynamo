package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393
 *
 */
public class NarrativeTemplate extends MultiNarrative { // TODO Todo's and documentation
	public NarrativeInstance generateInstance() {
		NarrativeInstance instance = new NarrativeInstance();
		
		instance.start = this.start.copy(instance);
		for (Node node : this.nodes) {
			node.setCopied(false); // TODO bad encapsulation, but resets copy flag
		}
		return instance;
	}
}
