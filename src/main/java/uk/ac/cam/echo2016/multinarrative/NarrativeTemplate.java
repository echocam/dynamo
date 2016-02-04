package uk.ac.cam.echo2016.multinarrative;

import java.util.HashMap;

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
    private static final long serialVersionUID = 1;

    // TODO Documentation
    /**
     *
     * @return
     */
    public NarrativeInstance generateInstance() {
        HashMap<String, Node> r_nodes = new HashMap<>();
        HashMap<String, Narrative> r_narrs = new HashMap<>();

        for (Node node : nodes.values()) {
            Node r_node = node.clone();
            r_node.createProperties();
            r_nodes.put(node.getIdentifier(), r_node);
        }

        for (Narrative narr : narratives.values()) {
            Narrative r_narr = narr.clone();
            // Find Start and end in r_nodes
            r_narr.setStart(r_nodes.get(narr.getStart().getIdentifier()));
            r_narr.setEnd(r_nodes.get(narr.getEnd().getIdentifier()));
            r_narr.getStart().getOptions().add(r_narr);
            
            // Increments the narrative entries property
            int narrEntries = r_narr.getEnd().getProperties().getInt("Impl.Node.Entries");
            narrEntries++;
            r_narr.getEnd().getProperties().putInt("Impl.Node.Entries", narrEntries);
            
            r_narrs.put(narr.getIdentifier(), r_narr);
        }

        return new NarrativeInstance(r_narrs, r_nodes, start);
    }

    /*
     * public NarrativeInstance generateInstance() throws NullPointerException { // TODO more appropriate exception?
     * NarrativeInstance instance = new NarrativeInstance();
     * 
     * if (this.start != null) { instance.start = this.start.copyToGraph(instance); } else { throw new
     * NullPointerException("No node registered as start of graph."); } for (Node node : this.nodes.values()) {
     * node.resetCopied(); } instance.setActive(start); return instance; }
     */
}
