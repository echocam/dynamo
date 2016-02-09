package uk.ac.cam.echo2016.multinarrative;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

public class NarrativeInstanceTest {
    HashMap<String, Route> sampleRoutes = new HashMap<String, Route>();
    HashMap<String, Node> sampleNodes = new HashMap<String, Node>();

    HashMap<String, Route> loadRoutes = new HashMap<String, Route>();
    HashMap<String, Node> loadNodes = new HashMap<String, Node>();

    @Before
    public void setup() {

        // Standard Test // - From the Visual Basic Sample Diagram

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

        Route tempRoute;

        tempRoute = new Route("routeMike1", "Mike", sampleNodes.get("syncStart"), sampleNodes.get("choiceMike1"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeMike2", "Mike", sampleNodes.get("choiceMike1"), sampleNodes.get("syncEnd"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeMike3", "Mike", sampleNodes.get("choiceMike1"), sampleNodes.get("sync3"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeMike4", "Mike", sampleNodes.get("sync3"), sampleNodes.get("syncEnd"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);

        tempRoute = new Route("routeSam1", "Sam", sampleNodes.get("syncStart"), sampleNodes.get("choiceSam1"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeSam2", "Sam", sampleNodes.get("choiceSam1"), sampleNodes.get("sync3"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeSam3", "Sam", sampleNodes.get("sync3"), sampleNodes.get("syncEnd"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeSam4", "Sam", sampleNodes.get("choiceSam1"), sampleNodes.get("sync4"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeSam5", "Sam", sampleNodes.get("sync4"), sampleNodes.get("syncEnd"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);

        tempRoute = new Route("routeSarah1", "Sarah", sampleNodes.get("syncStart"), sampleNodes.get("choiceSarah1"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeSarah2", "Sarah", sampleNodes.get("choiceSarah1"), sampleNodes.get("sync3"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeSarah3", "Sarah", sampleNodes.get("sync3"), sampleNodes.get("syncEnd"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeSarah4", "Sarah", sampleNodes.get("choiceSarah1"), sampleNodes.get("sync1"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeSarah5", "Sarah", sampleNodes.get("sync1"), sampleNodes.get("sync3"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);

        tempRoute = new Route("routeChris1", "Chris", sampleNodes.get("syncStart"), sampleNodes.get("choiceChris1"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeChris2", "Chris", sampleNodes.get("choiceChris1"), sampleNodes.get("sync1"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeChris3", "Chris", sampleNodes.get("sync1"), sampleNodes.get("syncEnd"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeChris4", "Chris", sampleNodes.get("choiceChris1"), sampleNodes.get("sync2"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeChris5", "Chris", sampleNodes.get("sync2"), sampleNodes.get("syncEnd"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);

        tempRoute = new Route("routeJessica1", "Jessica", sampleNodes.get("syncStart"), sampleNodes.get("sync2"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeJessica2", "Jessica", sampleNodes.get("sync2"), sampleNodes.get("choiceJessica1"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeJessica3", "Jessica", sampleNodes.get("choiceJessica1"), sampleNodes.get("sync4"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeJessica4", "Jessica", sampleNodes.get("sync4"), sampleNodes.get("syncEnd"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);
        tempRoute = new Route("routeJessica5", "Jessica", sampleNodes.get("choiceJessica1"), sampleNodes.get("syncEnd"));
        tempRoute.setup();
        sampleRoutes.put(tempRoute.getId(), tempRoute);

        // Load Test // - binary tree with node "1X" having children "10X" and "11X"

        for (int i = 1; i < 100000; ++i) {
            Node node = new ChoiceNode(Integer.toBinaryString(i));
            loadNodes.put(node.getId(), node);
        }
        for (Node node : loadNodes.values()) {
            String id = node.getId();
            int binary = Integer.parseInt(id, 2);
            int binCopy = binary;

            int bitCount = 0;
            while (binCopy > 0) {
                binCopy >>= 1;
                ++bitCount;
            }
            int bin1 = binary | 2 << bitCount - 1; // Set highest bit+1 to 1
            int bin2 = bin1 & ~(1 << bitCount - 1); // Set 2nd highest bit to 0
            if (bin1 == 0 || bin2 == 0) {
                System.out.println("?");
            }
            Node child1 = loadNodes.get(Integer.toBinaryString(bin1));
            Node child2 = loadNodes.get(Integer.toBinaryString(bin2));
            if (child1 != null && child2 != null) {
                Route route1 = new Route("route" + Integer.toBinaryString(bin1), "Chris", node, child1);
                route1.setup();
                Route route2 = new Route("route" + Integer.toBinaryString(bin2), "Chris", node, child2);
                route2.setup();
                loadRoutes.put(route1.getId(), route1);
                loadRoutes.put(route2.getId(), route2);
            }
        }
        SynchronizationNode start = new SynchronizationNode("start");
        Route startRoute = new Route("startRoute", "Chris", start, loadNodes.get("1"));
        startRoute.setup();
        loadNodes.put("start", start);
        loadRoutes.put("startRoute", startRoute);

    }

    @Test
    public void testNodeStructure() throws NullPointerException {

        // Sample Tests //

        // creates template using the maps created above
        NarrativeTemplate sampleTemplate = new NarrativeTemplate();
        sampleTemplate.routes.putAll(sampleRoutes);
        sampleTemplate.nodes.putAll(sampleNodes);
        sampleTemplate.start = (SynchronizationNode) sampleTemplate.getNode("syncStart");

        // Adds a test property for reference test later
        sampleTemplate.getNode("syncStart").createProperties();
        sampleTemplate.getNode("syncStart").getProperties().putIntArray("TestProperty", new int[] { 1, 2, 3 });

        // Tests the template constructor - these are for the full sample graph
        assertEquals(24, sampleTemplate.routes.size());
        assertEquals(11, sampleTemplate.nodes.size());
        assertEquals(sampleTemplate.getRoute("routeSarah5").getEnd().getId(), "sync3");
        assertEquals(sampleTemplate.getNode("choiceMike1").getEntering().get(0).getId(), "routeMike1");
        assertEquals(sampleTemplate.getNode("choiceMike1").getExiting().size(), 2);

        // Copy the template into an instance
        NarrativeInstance sampleInst = sampleTemplate.generateInstance2();

        // Tests whether the copy has the correct structure
        assertEquals("Testing correct number of routes: ", 24, sampleInst.routes.size());
        assertEquals("Testing correct number of nodes: ", 11, sampleInst.nodes.size());
        assertEquals("Testing \"routeSarah5\" connects to \"sync3\":",
                sampleInst.getRoute("routeSarah5").getEnd().getId(), "sync3");
        assertTrue("Testing \"choiceJessica1\" has route \"routeJessica3\" exiting:",
                sampleInst.getNode("choiceJessica1").getExiting().contains(sampleInst.getRoute("routeJessica3")));
        assertTrue("Testing \"sync4\" has route \"routeSam4\" entering:",
                sampleInst.getNode("sync4").getEntering().contains(sampleInst.getRoute("routeSam4")));

        // Tests whether the copy has different references
        assertFalse("Testing \"routes\" reference is different: ", sampleInst.routes == sampleTemplate.routes);
        assertFalse("Testing \"nodes\" reference is different: ", sampleInst.nodes == sampleTemplate.nodes);
        assertFalse("Testing \"start\" reference is different: ", sampleInst.start == sampleTemplate.start);
        assertFalse("Testing Test Property reference is different: ", sampleInst.getNode("syncStart").getProperties()
                .get("TestProperty") == sampleTemplate.getNode("syncStart").getProperties().get("TestProperty"));

        // Tests whether getRoute returns null for incorrect routes
        assertNull("Testing incorrect route: ", sampleInst.getRoute("routeBob1"));

        // Tests whether the kill method works correctly NOTE: may need changing to 20 with character association
        sampleInst.kill("routeMike1");
        assertEquals("Testing kill method: ", 21, sampleInst.routes.size());

        // Load Test //

        NarrativeTemplate loadTemplate = new NarrativeTemplate();
        loadTemplate.routes.putAll(loadRoutes);
        loadTemplate.nodes.putAll(loadNodes);
        loadTemplate.start = (SynchronizationNode) loadTemplate.getNode("start");

        NarrativeInstance loadInst = loadTemplate.generateInstance2();
        assertTrue("Testing load test constructor: ", loadInst.routes.containsKey("route10101"));
    }

    /**
     * Here template.start is not set, so an error is thrown.
     * 
     * @throws NullPointerException
     */

    /*
     * @Test(expected = NullPointerException.class) public void testErrorThrown() throws NullPointerException {
     * NarrativeTemplate template = new NarrativeTemplate(); template.route.putAll(routeMap);
     * template.nodes.putAll(nodeMap);
     * 
     * @SuppressWarnings("unused") NarrativeInstance instance = new NarrativeInstance(template); }
     */

    /*
     * @Test(expected = NullPointerException.class) public void testErrorThrown() throws NullPointerException {
     * NarrativeTemplate template = new NarrativeTemplate(); template.routes.putAll(routeMap);
     * template.nodes.putAll(nodeMap);
     * 
     * @SuppressWarnings("unused") NarrativeInstance instance = template.generateInstance(); }
     */

}
