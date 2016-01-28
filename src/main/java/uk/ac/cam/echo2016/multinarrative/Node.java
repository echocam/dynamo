package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393
 *
 */
import java.util.ArrayList;

public class Node {
	private final String ID;
	protected android.os.BaseBundle properties;
	
	public Node(String id) {
	    this.ID = id;
	}

	public String getIdentifier() {return null;}
	public android.os.BaseBundle startNarrative(Narrative option) {return null;}
	public GameChoice onEntry(Narrative played, NarrativeInstance instance) {return null;}
	public void getProperties() {return;}
	public ArrayList<Narrative> getOptions() {return null;}
}
