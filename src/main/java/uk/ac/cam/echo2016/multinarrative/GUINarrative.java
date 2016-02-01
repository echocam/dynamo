package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393, eyx20
 *
 */
public class GUINarrative extends EditableNarrative { //TODO Documentation
    //TODO Change from ArrayLists to HashMap.
	public void newNarrative(String id, String start, String end) { //TODO error if not unique
		if (isUniqueId(id)) {
			Node startNode = getNode(start);
			Node endNode = getNode(end);
			Narrative narr = new Narrative(id, startNode, endNode);
		    this.narratives.add(narr);
		    startNode.options.add(narr);
		}
	}
	
	public void newSynchronizationNode(String id) {//TODO error if not unique
		if (isUniqueId(id))
				nodes.add(new SynchronizationNode(id));
	}
	public void insertChoiceNode(String id) {//TODO error if not unique
		if (isUniqueId(id))
				nodes.add(new ChoiceNode(id));
	}
	//TODO documentation
	//TODO parameter name refactoring
	public void insertChoiceOnNarrative(String choiceId, String narrId, String newnarrId) { // TODO error for elementNotFound
		if (isUniqueId(choiceId) && isUniqueId(newnarrId)) {
			
			Narrative narr1 = getNarrative(narrId);
			ChoiceNode choice = new ChoiceNode(choiceId);
			Narrative narr2 = new Narrative(newnarrId,choice, narr1.getEnd());
			narr1.setEnd(choice);
			
			nodes.add(choice);
			narratives.add(narr2);
			
		}
	}
	public boolean isUniqueId(String id) {
		Narrative narr = getNarrative(id);
		Node node = getNode(id);
		return ((narr == null) && (node == null));
	}
	public void setStartPoint(String id) { // TODO error for elementNotFound
		Node node = getNode(id);
		start = node;
	}
	public android.os.BaseBundle getProperties (String id) { // TODO error for elementNotFound
		Narrative narr = getNarrative(id);
		Node node = getNode(id); // TODO search might be optimizable (2nd not required)
		if (narr != null) { // TODO alternate exception handling?
			return narr.properties;
		} else if (node != null) { // TODO alternate exception handling?
			return node.properties;
		}
		return null;
	}	
}
