package uk.ac.cam.echo2016.multinarrative;

/**
 * Represents a MultiNarrative that can be edited. Included for extensibility. We subclass it with 
 * GUINarrative, but this class can be derived for other methods of editing.
 * 
 * @author tr393, rjm232
 *
 */
public class EditableNarrative extends MultiNarrative{ //TODO Todo's and documentation
	
	public void addNarrative(Narrative narrative) {
		narratives.put(narrative.getIdentifier(), narrative);
	}
	
	public void addNode(Node node) {
		nodes.put(node.getIdentifier(), node);
	}
	
	public boolean removeNarrative(String id) {
		return false;
	}
	
	public boolean removeNode(String id) {return false;}
	
	public boolean renameNarrative(String id, String newName) {return false;}
	
	public boolean renameNode(String id, String newName) {return false;}
	
}
