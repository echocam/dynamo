package uk.ac.cam.echo2016.multinarrative;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

public class NarrativeInstanceTest {
	HashMap<String, Narrative> narrMap = new HashMap<String, Narrative>();
	HashMap<String, Node> nodeMap= new HashMap<String, Node>();

	@Before
	public void NarrativeInstanceTest() {
		/**
		 * The structure created here is what is found in the Visual Basic diagram on the Google Drive.
		 */
		nodeMap.put("syncStart", new SynchronizationNode("syncStart")); // 0
		nodeMap.put("syncEnd", new SynchronizationNode("syncEnd")); 	// 1
		nodeMap.put("sync1", new SynchronizationNode("sync1")); 	// 2
		nodeMap.put("sync2", new SynchronizationNode("sync2")); 	// 3
		nodeMap.put("sync3", new SynchronizationNode("sync3")); 	// 4
		nodeMap.put("sync4", new SynchronizationNode("sync4")); 	// 5
		nodeMap.put("choiceMike1", new ChoiceNode("choiceMike1")); 		// 6
		nodeMap.put("choiceSam1", new ChoiceNode("choiceSam1")); 		// 7
		nodeMap.put("choiceSarah1", new ChoiceNode("choiceSarah1")); 		// 8
		nodeMap.put("choiceChris1", new ChoiceNode("choiceChris1")); 		// 9
		nodeMap.put("choiceJessica1", new ChoiceNode("choiceJessica1")); 	// 10

		Narrative tempNarr;

		tempNarr = new Narrative("narrMike1",nodeMap.get("syncStart"),nodeMap.get("choiceMike1"));
		nodeMap.get("syncStart").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrMike2",nodeMap.get("choiceMike1"),nodeMap.get("syncEnd"));
		nodeMap.get("choiceMike1").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrMike3",nodeMap.get("choiceMike1"),nodeMap.get("sync3"));
		nodeMap.get("choiceMike1").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrMike4",nodeMap.get("sync3"),nodeMap.get("syncEnd"));
		nodeMap.get("sync3").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		
		tempNarr = new Narrative("narrSam1",nodeMap.get("syncStart"),nodeMap.get("choiceSam1"));
		nodeMap.get("syncStart").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrSam2",nodeMap.get("choiceSam1"),nodeMap.get("sync3"));
		nodeMap.get("choiceSam1").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrSam3",nodeMap.get("sync3"),nodeMap.get("syncEnd"));
		nodeMap.get("sync3").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrSam4",nodeMap.get("choiceSam1"),nodeMap.get("sync4"));
		nodeMap.get("choiceSam1").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrSam5",nodeMap.get("sync4"),nodeMap.get("syncEnd"));
		nodeMap.get("sync4").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		
		tempNarr = new Narrative("narrSarah1",nodeMap.get("syncStart"),nodeMap.get("choiceSarah1"));
		nodeMap.get("syncStart").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrSarah2",nodeMap.get("choiceSarah1"),nodeMap.get("sync3"));
		nodeMap.get("choiceSarah1").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrSarah3",nodeMap.get("sync3"),nodeMap.get("syncEnd"));
		nodeMap.get("sync3").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrSarah4",nodeMap.get("choiceSarah1"),nodeMap.get("sync1"));
		nodeMap.get("choiceSarah1").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrSarah5",nodeMap.get("sync1"),nodeMap.get("sync3"));
		nodeMap.get("sync1").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		
		tempNarr = new Narrative("narrChris1",nodeMap.get("syncStart"),nodeMap.get("choiceChris1"));
		nodeMap.get("syncStart").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrChris2",nodeMap.get("choiceChris1"),nodeMap.get("sync1"));
		nodeMap.get("choiceChris1").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrChris3",nodeMap.get("sync1"),nodeMap.get("syncEnd"));
		nodeMap.get("sync1").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrChris4",nodeMap.get("choiceChris1"),nodeMap.get("sync2"));
		nodeMap.get("choiceChris1").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrChris5",nodeMap.get("sync2"),nodeMap.get("syncEnd"));
		nodeMap.get("sync2").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		
		tempNarr = new Narrative("narrJessica1",nodeMap.get("syncStart"),nodeMap.get("sync2"));
		nodeMap.get("syncStart").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrJessica2",nodeMap.get("sync2"),nodeMap.get("choiceJessica1"));
		nodeMap.get("sync2").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrJessica3",nodeMap.get("choiceJessica1"),nodeMap.get("sync4"));
		nodeMap.get("choiceJessica1").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrJessica4",nodeMap.get("sync4"),nodeMap.get("syncEnd"));
		nodeMap.get("sync4").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
		tempNarr = new Narrative("narrJessica5",nodeMap.get("choiceJessica1"),nodeMap.get("syncEnd"));
		nodeMap.get("choiceJessica1").options.add(tempNarr);
		narrMap.put(tempNarr.getIdentifier(), tempNarr);
	}
	
	@Test
	public void testNodeStructure() throws NullPointerException{
		NarrativeTemplate template = new NarrativeTemplate();
		template.narratives.putAll(narrMap);
		template.nodes.putAll(nodeMap);
		template.start = template.getNode("syncStart");
		
		template.getNode("choiceMike1").createProperties();
		template.getNode("choiceMike1").getProperties().putBoolean("ChoicePropertyCopiedCorrectly", true);
		template.getNode("sync1").createProperties();
		template.getNode("sync1").getProperties().putBoolean("SyncPropertyCopiedCorrectly", true);
		
		assertEquals(24, template.narratives.size());
		assertEquals(11, template.nodes.size());
		assertEquals(template.getNarrative("narrSarah5").getEnd().getIdentifier(), "sync3");
		
		NarrativeInstance instance = new NarrativeInstance(template);
		
		assertTrue("Check Choice properties copied correctly", instance.getNodeProperties("choiceMike1").containsKey("ChoicePropertyCopiedCorrectly"));
		assertTrue("Check Sync properties copied correctly", instance.getNodeProperties("sync1").containsKey("SyncPropertyCopiedCorrectly"));

		assertEquals("Checking correct number of narratives: ", 24, instance.narratives.size());
		assertEquals("Checking correct number of nodes: ", 11, instance.nodes.size());
		assertEquals("Checking \"narrSarah5\" connects to \"sync3\":", instance.getNarrative("narrSarah5").getEnd()
				.getIdentifier(), "sync3");
		assertTrue("Checking \"choiceJessica1\" has narrative \"narrJessica3\":", instance.getNode(
				"choiceJessica1").options.contains(instance.getNarrative("narrJessica3")));

		Narrative narr = instance.getNarrative("narrBob1");
		assertNull(narr);

		instance.kill("narrMike1");
		assertEquals(21, instance.narratives.size());// Implementation not finished
	}
	
	@Test(expected = NullPointerException.class)
	public void testErrorThrown() throws NullPointerException {
		NarrativeTemplate template = new NarrativeTemplate();
		template.narratives.putAll(narrMap);
		template.nodes.putAll(nodeMap);
		
		NarrativeInstance instance = new NarrativeInstance(template);
	}
}
