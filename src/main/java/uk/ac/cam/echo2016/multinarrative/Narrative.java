package uk.ac.cam.echo2016.multinarrative;

import java.io.Serializable;

import android.os.BaseBundle;

/**
 * 
 * Acts as a connection between nodes. Represents the paths that characters follow.
 * 
 * @author tr393
 * @author eyx20
 * @author rjm232
 * @version 1.0
 *
 */
public class Narrative implements Serializable { // TODO Documentation
	private static final long serialVersionUID = 1;
	private final String id;
	private Node start;
	private Node end;
	private BaseBundle properties = null;

	public Narrative(String id, Node start, Node end) {
		this.id = id;
		this.start = start;
		this.end = end;
	}

   @Override
    public Narrative clone() {
        try {
            Narrative clone = (Narrative) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

	public String getIdentifier() {
		return id;
	}

	public Node getStart() {
		return start;
	}

	public void setStart(Node start) {
		this.start = start;
	}

	public Node getEnd() {
		return end;
	}

	public void setEnd(Node end) {
		this.end = end;
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
}
