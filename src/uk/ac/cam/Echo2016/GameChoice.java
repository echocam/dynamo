package uk.ac.cam.Echo2016;

import java.util.ArrayList;

/**
 * 
 * @author tr393
 *
 */
public class GameChoice { //TODO Todo's and documentation
	
	public static final int ACTION_MAJOR_DECISION = 0;
	public static final int ACTION_CHOOSE_NARRATIVE = 0;
	public static final int ACTION_CONTINUE = 0;
	
	protected boolean eventTrigger;
	protected boolean eventIdentifier;
	protected int action;

	public boolean hasEvent() {return false;}
	
	public String getEventIdentifier() {return null;}
	
	public int getAction() {return 0;}
	
	public ArrayList<Narrative> getOptions() {return null;}

}
