package uk.ac.cam.echo2016.multinarrative;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Before;

/**
 * Here there are commented out traverse statements. One can uncomment them to see the structure of
 * the graph.
 * 
 * @author Rjmcf
 *
 */

public class EditableNarrativeTest {
	EditableNarrative eNarr;
	
	/**
	 * Builds graph structure. Uncomment the traverse statement in addNarrativeAndNodeTest to see 
	 * the structure.
	 * @see addNarrativeAndNodeTest
	 */
	@Before
	public void setup() {
		eNarr = new EditableNarrative();
		
		SynchronizationNode s1 = new SynchronizationNode("start");
		ChoiceNode c1 = new ChoiceNode("choice1");
		SynchronizationNode s2 = new SynchronizationNode("left");
		SynchronizationNode s3 = new SynchronizationNode("right");
		SynchronizationNode s4 = new SynchronizationNode("end");
		Narrative l1 = new Narrative("link1", s1, c1);
		Narrative l2 = new Narrative("link2", c1, s2);
		Narrative l3 = new Narrative("link3", c1, s3);
		Narrative l4 = new Narrative("link4", s2, s4);
		Narrative l5 = new Narrative("link5", s3, s4);
		eNarr.addNode(s1);
		eNarr.addNode(c1);
		eNarr.addNode(s2);
		eNarr.addNode(s3);
		eNarr.addNode(s4);
		eNarr.addNarrative(l1);
		eNarr.addNarrative(l2);
		eNarr.addNarrative(l3);
		eNarr.addNarrative(l4);
		eNarr.addNarrative(l5);
		eNarr.start = s1;
	}
	
	@Test
	public void addNarrativeAndNodeTest() {
		//eNarr.start.traverse();
	}
	
	@Test
	public void renameNarrativeTest() {
		eNarr.renameNarrative("link4", "fromLeftToEnd");
		assertEquals("Check link4 renamed internally", "fromLeftToEnd", eNarr.narratives.get("fromLeftToEnd").getIdentifier());
		assertEquals("Check link4 renamed in start Node", "fromLeftToEnd", eNarr.nodes.get("left").getOptions().get(0).getIdentifier());
		//eNarr.start.traverse();
	}
	
	@Test
	public void renameNodeTest() {
		eNarr.renameNode("choice1", "FirstChoice");
		assertEquals("check choice1 renamed internally", "FirstChoice", eNarr.nodes.get("FirstChoice").getIdentifier());
		assertEquals("check choice1 renamed in entering narr", "FirstChoice", eNarr.narratives.get("link1").getEnd().getIdentifier());
		assertEquals("check choice1 renamed in exiting left", "FirstChoice", eNarr.narratives.get("link2").getStart().getIdentifier());
		assertEquals("check choice1 renamed in exiting right", "FirstChoice", eNarr.narratives.get("link3").getStart().getIdentifier());
		assertEquals("check choice1 still has options", 2, eNarr.nodes.get("FirstChoice").getOptions().size());
		for (Narrative n : eNarr.nodes.get("FirstChoice").getOptions()) {
			assertEquals("check choice1 renamed in options", "FirstChoice", n.getStart().getIdentifier());
		}
		//eNarr.start.traverse();
	}
	
	@Test
	public void removeNarrativeTest() {
		eNarr.removeNarrative("link1");
		assertNull("Check link1 removed from narratives", eNarr.narratives.get("link1"));
		assertEquals("Check link1 removed from start options", 0, eNarr.start.getOptions().size());
		//eNarr.start.traverse();
		
		eNarr.removeNarrative("link3");
		assertEquals("Check link3 removed from choice1 options", 1, eNarr.nodes.get("choice1").getOptions().size());
		//eNarr.nodes.get("choice1").traverse();
	}
	
	@Test
	public void removeNodeTest() {
		eNarr.removeNode("left");
		assertNull("Check left removed from nodes", eNarr.nodes.get("left"));
		assertNull("Check entering narrative removed from narratives", eNarr.narratives.get("link2"));
		assertNull("Check exiting narrative removed from narratives", eNarr.narratives.get("link4"));
		//eNarr.start.traverse();
		
		setup();
		eNarr.removeNode("choice1");
		assertNull("Check choice1 removed from nodes", eNarr.nodes.get("choice1"));
		assertNull("Check entering narrative removed from narratives", eNarr.narratives.get("link1"));
		assertNull("Check left exiting narrative removed", eNarr.narratives.get("link2"));
		assertNull("Check right exiting narrative removed", eNarr.narratives.get("link3"));
		//eNarr.start.traverse();
		//eNarr.nodes.get("left").traverse();
		//eNarr.nodes.get("right").traverse();
	}
}
