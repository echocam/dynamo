package uk.ac.cam.echo2016.multinarrative;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.BaseBundle;

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
    protected BaseBundle properties;
    protected ArrayList<Narrative> options;

    public Node(String id) {
        this.id = id;
        this.options = new ArrayList<Narrative>();
    }

    /**
     * Method is implemented in derived classes ChoiceNode and SyncNode, to allow this class to make new objects of
     * those derived types in the copyToGraph method.
     *
     * @param id
     * @return
     */
    protected abstract Node callConstructor(String id);
    
    public abstract BaseBundle startNarrative(Narrative option);

    public abstract GameChoice onEntry(Narrative played, NarrativeInstance instance);

    public String getIdentifier() {
        return id;
    }

    public void createProperties() {
        if (properties == null)
            properties = new BaseBundle(); // TODO Initialize with default starting size?
    }

    public BaseBundle getProperties() {
        return properties;
    }

    public void setProperties(BaseBundle b) {
        properties = b;
    }

    public ArrayList<Narrative> getOptions() {
        return options;
    }
    
    @Override
    public Node clone(){
	try {
	    Node node = (Node) super.clone();
	    node.options = new ArrayList<>();
	    //node.properties = properties.clone(); TODO
	    return node;
	} catch (CloneNotSupportedException e) {
	    throw new RuntimeException(e);
	}
    }
}
