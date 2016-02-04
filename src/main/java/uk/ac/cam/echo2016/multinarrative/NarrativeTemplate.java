package uk.ac.cam.echo2016.multinarrative;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.BaseBundle;

/**
 * 
 * The template of the story from which the copy of the game required for each
 * play through is derived.
 * 
 * <p>
 * ALT: A full {@code MultiNarrative} graph that is used to create new
 * {@code NarrativeInstance}s for each playthrough. This contains all nodes and
 * narratives on the graph designed in the {@code FXMLGUI} editor, and is the
 * template graph used for new save files. When the template is created, it
 * should not be modified in any way. (Unless the programmer wants different
 * behaviour across new playthroughs!)
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
        
        if (start == null) {throw new RuntimeException();} // TODO better exception
        
        for (Node node : nodes.values()) {
            Node r_node = node.clone();
            r_node.createProperties();
            r_node.setOptions(new ArrayList<Narrative>());
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
            r_narr.getEnd().getProperties().putInt("Impl.Node.Entries", ++narrEntries);
            
            r_narrs.put(narr.getIdentifier(), r_narr);
        }
        Node r_start = r_nodes.get(start.getIdentifier());

        NarrativeInstance instance = new NarrativeInstance(r_narrs, r_nodes, r_start);
        return instance;
    }
	 public NarrativeInstance generateInstance2() throws NullPointerException { // TODO more appropriate exception?
	 NarrativeInstance instance = new NarrativeInstance();

        if (this.start != null) {
            instance.start = copyToGraph(this.start,instance);
        } else {
            throw new NullPointerException("No node registered as start of graph.");
        }
        for (Node node : this.nodes.values()) {
            node.resetCopied();
        }
        instance.setActive(start);
        return instance;
    }  
    public Node copyToGraph(Node node, NarrativeInstance instance) { // TODO More Documentation!!! and tests

        // Eventually calls Node(this.id) via subclass's constructor
        Node result = node.callConstructor(node.getIdentifier());
        //Node result = node.clone2();

        if (node.getProperties() != null) // Copy getProperties() across, if any
            result.setProperties(BaseBundle.deepcopy(node.getProperties()));
        node.copied = true; // TODO encapsulation of copied flag

        // Copy each narrative leaving node node and call copyGraph on their end nodes
        for (Narrative narrTemplate : node.options) {
            Node endNodeCopy;
            if (narrTemplate.getEnd().copied == false) {
                // Not already copied
                endNodeCopy = narrTemplate.getEnd().copyToGraph(instance); // Recursively copy nodes at the ends of

                // Create and update entryList property
                endNodeCopy.createProperties();

                endNodeCopy.getProperties().putInt("Impl.Node.Entries", 1);
            } else {
                // Already copied

                endNodeCopy = instance.getNode(narrTemplate.getEnd().getIdentifier()); // Get reference to copied end
                // Update entryList property

                int narrEntries = endNodeCopy.getProperties().getInt("Impl.Node.Entries");
                narrEntries++;
                endNodeCopy.getProperties().putInt("Impl.Node.Entries", narrEntries);
            }

            // Create narrative using references obtained/created above, linking node node to the new end nodes
            Narrative narrCopy = new Narrative(narrTemplate.getIdentifier(), result, endNodeCopy);
            narrCopy.setProperties(narrTemplate.getProperties());
            
            // Update narrative references
            result.getOptions().add(narrCopy);
            // Update graph references
            instance.narratives.put(narrCopy.getIdentifier(), narrCopy);
        }
        instance.nodes.put(result.getIdentifier(), result);
        return result;
    }    
}
