package uk.ac.cam.echo2016.multinarrative;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

public class NarrativeInstanceTest {
    HashMap<String, Narrative> sampleNarrs = new HashMap<String, Narrative>();
    HashMap<String, Node> sampleNodes = new HashMap<String, Node>();

    HashMap<String, Narrative> loadNarrs = new HashMap<String, Narrative>();
    HashMap<String, Node> loadNodes = new HashMap<String, Node>();

    @Before
    public void setup() {

        // Visual Basic Sample Diagram

        sampleNodes.put("syncStart", new SynchronizationNode("syncStart")); // ___0
        sampleNodes.put("syncEnd", new SynchronizationNode("syncEnd")); // _______1
        sampleNodes.put("sync1", new SynchronizationNode("sync1")); // ___________2
        sampleNodes.put("sync2", new SynchronizationNode("sync2")); // ___________3
        sampleNodes.put("sync3", new SynchronizationNode("sync3")); // ___________4
        sampleNodes.put("sync4", new SynchronizationNode("sync4")); // ___________5
        sampleNodes.put("choiceMike1", new ChoiceNode("choiceMike1")); // ________6
        sampleNodes.put("choiceSam1", new ChoiceNode("choiceSam1")); // __________7
        sampleNodes.put("choiceSarah1", new ChoiceNode("choiceSarah1")); // ______8
        sampleNodes.put("choiceChris1", new ChoiceNode("choiceChris1")); // ______9
        sampleNodes.put("choiceJessica1", new ChoiceNode("choiceJessica1")); // _10

        Narrative tempNarr;

        tempNarr = new Narrative("narrMike1", sampleNodes.get("syncStart"), sampleNodes.get("choiceMike1"));
        sampleNodes.get("syncStart").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrMike2", sampleNodes.get("choiceMike1"), sampleNodes.get("syncEnd"));
        sampleNodes.get("choiceMike1").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrMike3", sampleNodes.get("choiceMike1"), sampleNodes.get("sync3"));
        sampleNodes.get("choiceMike1").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrMike4", sampleNodes.get("sync3"), sampleNodes.get("syncEnd"));
        sampleNodes.get("sync3").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);

        tempNarr = new Narrative("narrSam1", sampleNodes.get("syncStart"), sampleNodes.get("choiceSam1"));
        sampleNodes.get("syncStart").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrSam2", sampleNodes.get("choiceSam1"), sampleNodes.get("sync3"));
        sampleNodes.get("choiceSam1").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrSam3", sampleNodes.get("sync3"), sampleNodes.get("syncEnd"));
        sampleNodes.get("sync3").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrSam4", sampleNodes.get("choiceSam1"), sampleNodes.get("sync4"));
        sampleNodes.get("choiceSam1").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrSam5", sampleNodes.get("sync4"), sampleNodes.get("syncEnd"));
        sampleNodes.get("sync4").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);

        tempNarr = new Narrative("narrSarah1", sampleNodes.get("syncStart"), sampleNodes.get("choiceSarah1"));
        sampleNodes.get("syncStart").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrSarah2", sampleNodes.get("choiceSarah1"), sampleNodes.get("sync3"));
        sampleNodes.get("choiceSarah1").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrSarah3", sampleNodes.get("sync3"), sampleNodes.get("syncEnd"));
        sampleNodes.get("sync3").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrSarah4", sampleNodes.get("choiceSarah1"), sampleNodes.get("sync1"));
        sampleNodes.get("choiceSarah1").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrSarah5", sampleNodes.get("sync1"), sampleNodes.get("sync3"));
        sampleNodes.get("sync1").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);

        tempNarr = new Narrative("narrChris1", sampleNodes.get("syncStart"), sampleNodes.get("choiceChris1"));
        sampleNodes.get("syncStart").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrChris2", sampleNodes.get("choiceChris1"), sampleNodes.get("sync1"));
        sampleNodes.get("choiceChris1").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrChris3", sampleNodes.get("sync1"), sampleNodes.get("syncEnd"));
        sampleNodes.get("sync1").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrChris4", sampleNodes.get("choiceChris1"), sampleNodes.get("sync2"));
        sampleNodes.get("choiceChris1").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrChris5", sampleNodes.get("sync2"), sampleNodes.get("syncEnd"));
        sampleNodes.get("sync2").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);

        tempNarr = new Narrative("narrJessica1", sampleNodes.get("syncStart"), sampleNodes.get("sync2"));
        sampleNodes.get("syncStart").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrJessica2", sampleNodes.get("sync2"), sampleNodes.get("choiceJessica1"));
        sampleNodes.get("sync2").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrJessica3", sampleNodes.get("choiceJessica1"), sampleNodes.get("sync4"));
        sampleNodes.get("choiceJessica1").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrJessica4", sampleNodes.get("sync4"), sampleNodes.get("syncEnd"));
        sampleNodes.get("sync4").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);
        tempNarr = new Narrative("narrJessica5", sampleNodes.get("choiceJessica1"), sampleNodes.get("syncEnd"));
        sampleNodes.get("choiceJessica1").getOptions().add(tempNarr);
        sampleNarrs.put(tempNarr.getIdentifier(), tempNarr);

        // Load Test

        for (int i = 0; i < 128; ++i) {
            Node node = new ChoiceNode(Integer.toBinaryString(i));
            loadNodes.put(node.getIdentifier(), node);
        }
        for (Node node : loadNodes.values()) {
            String id = node.getIdentifier();
            int binary = Integer.parseInt(id,2);
            int binCopy = binary;

            int bitCount = 0;
            while (binCopy > 0) {
                binCopy >>= 1;
                ++bitCount;
            }
            int bin1 = binary | 2 << bitCount - 1;
            int bin2 = binary | 3 << bitCount - 1;

            Node child1 = loadNodes.get(Integer.toBinaryString(bin1));
            Node child2 = loadNodes.get(Integer.toBinaryString(bin2));
            if (child1 != null && child2 != null) {
                Narrative narr1 = new Narrative("l" + bin1, node, child1);
                Narrative narr2 = new Narrative("r" + bin2, node, child2);
                loadNarrs.put(narr1.getIdentifier(), narr1);
                loadNarrs.put(narr2.getIdentifier(), narr2);
                node.getOptions().add(narr1);
                node.getOptions().add(narr2);
            }
        }

    }

    @Test
    public void testNodeStructure() throws NullPointerException { // TODO Documentation!
        NarrativeTemplate template = new NarrativeTemplate();
        template.narratives.putAll(sampleNarrs);
        template.nodes.putAll(sampleNodes);
        template.start = template.getNode("syncStart");

        template.getNode("choiceMike1").createProperties(); // TODO test deep clone method
        template.getNode("choiceMike1").getProperties().putBoolean("ChoicePropertyCopiedCorrectly", true);
        template.getNode("sync1").createProperties();
        template.getNode("sync1").getProperties().putBoolean("SyncPropertyCopiedCorrectly", true);

        assertEquals(24, template.narratives.size());
        assertEquals(11, template.nodes.size());
        assertEquals(template.getNarrative("narrSarah5").getEnd().getIdentifier(), "sync3");
       
        NarrativeInstance instance = template.generateInstance();
        
        assertTrue("Check Choice properties copied correctly", instance.getNodeProperties("choiceMike1").containsKey("ChoicePropertyCopiedCorrectly"));
        assertTrue("Check Sync properties copied correctly", instance.getNodeProperties("sync1").containsKey("SyncPropertyCopiedCorrectly"));

        assertEquals("Checking correct number of narratives: ", 24, instance.narratives.size());
        assertEquals("Checking correct number of nodes: ", 11, instance.nodes.size());
        assertEquals("Checking \"narrSarah5\" connects to \"sync3\":",
                instance.getNarrative("narrSarah5").getEnd().getIdentifier(), "sync3");
        assertTrue("Checking \"choiceJessica1\" has narrative \"narrJessica3\":",
                instance.getNode("choiceJessica1").getOptions().contains(instance.getNarrative("narrJessica3")));

        Narrative narr = instance.getNarrative("narrBob1");
        assertNull(narr);

        instance.kill("narrMike1");
        assertEquals(21, instance.narratives.size());
    }

    /**
     * Here template.start is not set, so an error is thrown.
     * 
     * @throws NullPointerException
     */

    /*
     * @Test(expected = NullPointerException.class) public void testErrorThrown() throws NullPointerException {
     * NarrativeTemplate template = new NarrativeTemplate(); template.narratives.putAll(narrMap);
     * template.nodes.putAll(nodeMap);
     * 
     * @SuppressWarnings("unused") NarrativeInstance instance = new NarrativeInstance(template); }
     */
    
   /*
    @Test(expected = NullPointerException.class)
    public void testErrorThrown() throws NullPointerException {
        NarrativeTemplate template = new NarrativeTemplate();
        template.narratives.putAll(narrMap);
        template.nodes.putAll(nodeMap);
        
        @SuppressWarnings("unused")
        NarrativeInstance instance = template.generateInstance();
    }
*/
    
}
