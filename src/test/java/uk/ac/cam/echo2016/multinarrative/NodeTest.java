package uk.ac.cam.echo2016.multinarrative;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class NodeTest {
	@Test
	public void SimpleTest() {
		SynchronizationNode syncTest = new SynchronizationNode("syncTestID");
		ChoiceNode choiceTest = new ChoiceNode("choiceTestID");
		
		assertEquals("syncTestID",syncTest.getIdentifier());
		assertEquals("choiceTestID",choiceTest.getIdentifier());
		assertEquals(new ArrayList<Narrative>(), syncTest.getOptions());
		assertEquals(new ArrayList<Narrative>(), choiceTest.getOptions());
		assertNull(syncTest.getProperties());
		assertNull(choiceTest.getProperties());
		
		syncTest.createProperties();
		choiceTest.createProperties();
		assertNotNull(syncTest.getProperties());
		assertNotNull(choiceTest.getProperties());
	}
}
