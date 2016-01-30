package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393
 *
 */
public class NarrativeTemplate extends MultiNarrative { // TODO Todo's and
														// documentation
	public NarrativeInstance generateInstance() {
		NarrativeInstance instance = new NarrativeInstance();
		
		for (Node node : this.nodes) { // TODO might be breaking encapsulation
			instance.nodes.add(node.copy(instance));// TODO reset copy tag then copy the start
		}
		return null;
	}
}
