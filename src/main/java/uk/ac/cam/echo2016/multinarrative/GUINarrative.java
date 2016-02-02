package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393, eyx20
 *
 */
public class GUINarrative extends EditableNarrative { // TODO Documentation
	public void newNarrative(String id, String start, String end) throws NonUniqueIdException { // TODO error message
		if (isUniqueId(id)) {
			Node startNode = getNode(start);
			Node endNode = getNode(end);
			Narrative narr = new Narrative(id, startNode, endNode);
			this.narratives.add(narr);
			startNode.options.add(narr);
		} else {
			throw new NonUniqueIdException();
		}
	}

	public void newSynchronizationNode(String id) throws NonUniqueIdException { // TODO error message
		if (isUniqueId(id))
			nodes.add(new SynchronizationNode(id));
		else
			throw new NonUniqueIdException();
	}

	public void insertChoiceNode(String id) throws NonUniqueIdException { // TODO error message
		if (isUniqueId(id))
			nodes.add(new ChoiceNode(id));
		else
			throw new NonUniqueIdException();
	}

	// TODO documentation(+overloaded function) + better refactoring?
	public void insertChoiceOnNarrative(String narrId, String newChoiceId, String newNarrId)
			throws NonUniqueIdException { // TODO error for elementNotFound/ error message
		if (isUniqueId(newChoiceId) && isUniqueId(newNarrId)) {
			Narrative narr2 = getNarrative(narrId);
			ChoiceNode choice = new ChoiceNode(newChoiceId);
			Narrative narr1 = new Narrative(newNarrId, narr2.getStart(), choice);
			narr2.setStart(choice);
			narratives.add(narr1);
			nodes.add(choice);
		} else {
			throw new NonUniqueIdException();
		}
	}

	public void insertChoiceOnNarrative(String narrId, String newChoiceId, String newNarrId1, String newNarrId2)
			throws NonUniqueIdException { // TODO error for elementNotFound/error message
		if (isUniqueId(newChoiceId) && isUniqueId(newNarrId1) && isUniqueId(newNarrId2)) {
			Narrative narr = getNarrative(narrId);
			ChoiceNode choice = new ChoiceNode(newChoiceId);
			Narrative narr1 = new Narrative(newNarrId1, narr.getStart(), choice);
			Narrative narr2 = new Narrative(newNarrId2, choice, narr.getEnd());
			narratives.remove(narr);
			narratives.add(narr1);
			narratives.add(narr2);
			nodes.add(choice);
		} else {
			throw new NonUniqueIdException();
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

	public android.os.BaseBundle getProperties(String id) { // TODO error for elementNotFound
		Narrative narr = getNarrative(id);
		Node node = getNode(id); // TODO search might be optimizable (2nd not required)
		if (narr != null) { // TODO alternate exception handling?
			return narr.getProperties();
		} else if (node != null) { // TODO alternate exception handling?
			return node.getProperties();
		}
		return null;
	}
}
