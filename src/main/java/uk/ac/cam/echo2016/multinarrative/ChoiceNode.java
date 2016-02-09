package uk.ac.cam.echo2016.multinarrative;

import android.os.BaseBundle;

/**
 * Represents a major decision made in a route that can affect which sync point a character will end up in. Has one
 * route entering, and several leaving.
 *
 * <p>
 * ALT: Implements a {@link Node} at a branch point on the {@code MultiNarrative} graph structure. At this
 * point, some decision in the game affects the route taken down the graph. Only one {@code Route} should be
 * entering this node[, although this is not enforced?], as opposed to {@link SyncronizationNode}.
 * 
 * @author tr393
 * @author rjm232
 * @see Node
 * @see SyncronizationNode
 * @see MultiNarrative 
 */
public class ChoiceNode extends Node { // TODO Implementation and documentation
	private static final long serialVersionUID = 1;

	public ChoiceNode(String id) {
		super(id);
	}

	protected Node create(String id) {
		return new ChoiceNode(id);
	}

    public BaseBundle startRoute(Route option) {
        return option.getProperties();
    };

    public GameChoice onEntry(Route completed, NarrativeInstance instance) throws GraphElementNotFoundException {
        if (!this.getEntering().contains(completed)) {
        	throw new GraphElementNotFoundException("completed route not in this node's entering list");
        } 
        GameChoice gameChoice = new GameChoice(true, getId(), GameChoice.ACTION_MAJOR_DECISION);
        gameChoice.getOptions().addAll(getExiting());
        
        return gameChoice;
    }

}
