package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * The template of the story from which the copy of the game required for each play through is derived.
 * 
 * <p>
 * ALT: A full {@code MultiNarrative} graph that is used to create new {@code NarrativeInstance}s for each playthrough.
 * This contains all nodes and narratives on the graph designed in the {@code FXMLGUI} editor, and is the template graph
 * used for new save files. When the template is created, it should not be modified in any way. (Unless the programmer
 * wants different behaviour across new playthroughs!)
 * 
 * @author tr393
 * @author rjm232
 * @version 1.0
 * @see NarrativeTemplate#generateInstance()
 * @see NarrativeInstance
 * @see MultiNarrative
 */
public class NarrativeTemplate extends MultiNarrative {

    //TODO Documentation
    /**
     *
     * @return
     * @throws NullPointerException
     *             if the NarrativeInstance.start has not been set.
     */
    public NarrativeInstance generateInstance() throws NullPointerException { // TODO more appropriate exception?
        NarrativeInstance instance = new NarrativeInstance();

        if (this.start != null) {
            instance.start = this.start.copyToGraph(instance);
        } else {
            throw new NullPointerException("No node registered as start of graph.");
        }
        for (Node node : this.nodes.values()) {
            node.resetCopied();
        }
        instance.setActive(start);
        return instance;
    }
}
