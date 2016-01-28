package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393
 *
 */
public class Narrative { // TODO Documentation
	private String id;
	protected Node start;
	protected Node end;
	protected android.os.BaseBundle properties = null;

	public Narrative(String id) { // TODO require start/end node arguments?
		this.id = id;
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
