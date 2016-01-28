package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;

/**
 * 
 * @author tr393
 *
 */
public class NarrativeInstance extends MultiNarrative { //TODO Todo's and documentation
	protected android.os.BaseBundle properties;
	public NarrativeInstance(NarrativeTemplate template) {}
	public android.os.BaseBundle getGlobalProperties() {return null;}
	public android.os.BaseBundle getNarrativeProperties(String id) {return null;}
	public android.os.BaseBundle getNodeProperties(String id) {return null;}
	public android.os.BaseBundle startNarrative(String id) {return null;}
	public GameChoice endNarrative(String id) {return null;}
	public boolean kill(String id) {return false;}
	public ArrayList<Narrative> getPlayableNarratives() {return null;}
	public void setActive(Node node) {return;}
}
