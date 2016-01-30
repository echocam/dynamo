package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393, eyx20
 *
 */
public class Narrative { // TODO Documentation
	private final String id;
	protected Node start;
	protected Node end;
	protected android.os.BaseBundle properties = null;

	public Narrative(String id, Node start, Node end) {
		this.id = id;
		this.start = start;
		this.end = end;
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

	public void createProperties() { // TODO Initialize with default starting size?
		properties = new android.os.BaseBundle();
	}

	public android.os.BaseBundle getProperties() {
		return properties;
	}
}
