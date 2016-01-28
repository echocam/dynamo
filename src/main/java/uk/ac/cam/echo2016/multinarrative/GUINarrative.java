package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393, eyx20
 *
 */


public class GUINarrative extends EditableNarrative { //TODO Todo's and documentation
    
    private Narrative narrative;

    //Is this supposed to be the GUI constructor? Should this instead be GUINarrative()?
	public void newNarrative(String id, String start, String end) {
	    this.narrative = new Narrative(id);
	    narrative.setStart(new Node(start));
	    narrative.setEnd(new Node(End));
	    return;
	    }
	
	public void newSynchronizationNode(String id) {return;}
	public android.os.BaseBundle getProperties (String id) {return null;}
	public void insertChoicePoint(String id) {return;}
	public void setStartPoint(String id) {return;}
}
