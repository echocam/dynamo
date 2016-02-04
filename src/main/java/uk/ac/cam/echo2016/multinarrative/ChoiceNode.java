package uk.ac.cam.echo2016.multinarrative;

/**
 * Represents a major decision made in a narrative that can affect which sync point a character will end up in. Has one
 * narrative entering, and several leaving.
 *
 * <p>
 * ALT: Implements a {@link Node} at a branch point on the {@code MultiNarrative} graph structure. At this
 * point, some decision in the game affects the route taken down the graph. Only one {@code Narrative} should be
 * entering this node[, although this is not enforced?], as opposed to {@link SyncronizationNode}.
 * 
 * @author tr393
 * @author rjm232
 * @see Node
 * @see SyncronizationNode
 * @see MultiNarrative 
 */
public class ChoiceNode extends Node { // TODO Implementation and documentation

    public ChoiceNode(String id) {
        super(id);
    }

    protected Node callConstructor(String id) {
        return new ChoiceNode(id);
    }

    public android.os.BaseBundle startNarrative(Narrative option) { // TODO Finish Impl
        return null;
    };

    public GameChoice onEntry(Narrative completed, NarrativeInstance instance) { // TODO Finish Impl
        return null;
    }

}
