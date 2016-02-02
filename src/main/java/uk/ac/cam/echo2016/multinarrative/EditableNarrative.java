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
	
	public boolean removeNarrative(String id) { // TODO: test this!
		Narrative narr = narratives.remove(id);
		if (narr == null) 
			return false;
		
		narr.getStart().options.remove(narr);	
		
		return true;
	}
	
	public boolean removeNode(String id) { // TODO: test this!
		Node node = nodes.remove(id);
		if (node == null) 
			return false;
		
		for (Narrative narr : narratives.values()) {
			if (narr.getStart() == node) {
				narratives.remove(narr.getIdentifier());
			} else if (narr.getEnd() == node) {
				removeNarrative(narr.getIdentifier());	
			}
		}
		
		return true;		
	}
	
	public boolean renameNarrative(String id, String newName) { // TODO: test this!
		Narrative narr = narratives.remove(id);
		if (narr == null)
			return false;
		
		Narrative newNarr = new Narrative(newName, narr.getStart(), narr.getEnd());
		if (narr.getProperties() != null)
			newNarr.setProperties(new android.os.BaseBundle(narr.getProperties()));
		
		narratives.put(newName, newNarr);
		newNarr.getStart().options.remove(narr);
		newNarr.getStart().options.add(newNarr);
		
		return true;
	}
	
	public boolean renameNode(String id, String newName) { // TODO: test this!
		Node node = nodes.remove(id);
		if (node == null) 
			return false;
		
		Node newNode = node.callConstructor(newName);
		if (node.getProperties() != null) 
			newNode.setProperties(new android.os.BaseBundle(node.getProperties()));
		
		nodes.put(newName, newNode);
		for (Narrative narr : narratives.values()) {
			if (narr.getStart() == node) {
				narr.setStart(newNode);
			} else if (narr.getEnd() == node) {
				narr.setEnd(newNode);	
			}
		}
		
		return true;
	}	
}
