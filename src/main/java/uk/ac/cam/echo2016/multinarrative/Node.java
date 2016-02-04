package uk.ac.cam.echo2016.multinarrative;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a {@code Node} on the {@code MultiNarrative} graph structure. Each node has an {@code ArrayList} of {@code Narrative} references leaving the {@code Node}.
 *
 * @author tr393
 * @author rjm232
 * @version 1.0
 *
 */
public abstract class Node implements Serializable, Cloneable{ // TODO Documentation
    private static final long serialVersionUID = 1;
    private final String id;
    private android.os.BaseBundle properties;
    private ArrayList<Narrative> options;
    private boolean copied = false; // flag used in graph copy that indicates whether this node has been passed

    public Node(String id) {
        this.id = id;
        this.options = new ArrayList<Narrative>();
    }


    protected void resetCopied() { // TODO warning - should not be concurrently called while the
        // NarrativeTemplate.getInstance() method is running!
        copied = false;
    }

    /**
     * Method is implemented in derived classes ChoiceNode and SyncNode, to allow this class to make new objects of
     * those derived types in the copyToGraph method.
     *
     * @param id
     * @return
     */
    protected abstract Node callConstructor(String id);

    public abstract android.os.BaseBundle startNarrative(Narrative option);

    public abstract GameChoice onEntry(Narrative played, NarrativeInstance instance);

    public String getIdentifier() {
        return id;
    }

    public void createProperties() {
        if (properties == null)
            properties = new android.os.BaseBundle(); // TODO Initialize with default starting size?
    }

    public android.os.BaseBundle getProperties() {
        return properties;
    }

    public void setProperties(android.os.BaseBundle b) {
        properties = b;
    }

    public ArrayList<Narrative> getOptions() {
        return options;
    }
}
