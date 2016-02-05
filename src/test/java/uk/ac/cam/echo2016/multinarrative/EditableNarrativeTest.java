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
public class EditableNarrativeTest { // TODO combine with narrativeInstanceTest/GUINarrativeTest
    public static void follow(Route route) {
        System.out.println("This route is " + route.getIdentifier());
        System.out.println("It connects " + route.getIdentifier() + " to " + route.getIdentifier());
        System.out.println();
    }
    public static void traverse(Node node) { // TODO: remove as for testing purposes only
        System.out.println("This node is " + node.getIdentifier());
        System.out.println("Exiting this node are the following routes:");
        for (Route n : node.getOptions()) {
            System.out.println("    " + n.getIdentifier());
        }
        System.out.println();
        for (Route n : node.getOptions()) {
            follow(n);
        }
        for (Route n : node.getOptions()) {
            traverse(n.getEnd());
        }
    }
    
    
    EditableNarrative eNarr;
    
    /**
     * Builds graph structure. Uncomment the traverse statement in addRouteAndNodeTest to see 
     * the structure.
     * @see addRouteAndNodeTest
     */
    @Before
    public void setup() {
        eNarr = new GUINarrative();
        
        SynchronizationNode s1 = new SynchronizationNode("start");
        ChoiceNode c1 = new ChoiceNode("choice1");
        SynchronizationNode s2 = new SynchronizationNode("left");
        SynchronizationNode s3 = new SynchronizationNode("right");
        SynchronizationNode s4 = new SynchronizationNode("end");
        Route l1 = new Route("link1", s1, c1);
        Route l2 = new Route("link2", c1, s2);
        Route l3 = new Route("link3", c1, s3);
        Route l4 = new Route("link4", s2, s4);
        Route l5 = new Route("link5", s3, s4);
        eNarr.addNode(s1);
        eNarr.addNode(c1);
        eNarr.addNode(s2);
        eNarr.addNode(s3);
        eNarr.addNode(s4);
        eNarr.addRoute(l1);
        eNarr.addRoute(l2);
        eNarr.addRoute(l3);
        eNarr.addRoute(l4);
        eNarr.addRoute(l5);
        eNarr.start = s1;
    }
    
    @Test
    public void addRouteAndNodeTest() {
        traverse(eNarr.start);
    }
    
    @Test
    public void renameRouteTest() {
        eNarr.renameRoute("link4", "fromLeftToEnd");
        assertEquals("Check link4 renamed internally", "fromLeftToEnd", eNarr.routes.get("fromLeftToEnd").getIdentifier());
        assertEquals("Check link4 renamed in start Node", "fromLeftToEnd", eNarr.nodes.get("left").getOptions().get(0).getIdentifier());
        //traverse(eNarr.start);
    }
    
    @Test
    public void renameNodeTest() {
        eNarr.renameNode("choice1", "FirstChoice");
        assertEquals("check choice1 renamed internally", "FirstChoice", eNarr.nodes.get("FirstChoice").getIdentifier());
        assertEquals("check choice1 renamed in entering narr", "FirstChoice", eNarr.routes.get("link1").getEnd().getIdentifier());
        assertEquals("check choice1 renamed in exiting left", "FirstChoice", eNarr.routes.get("link2").getStart().getIdentifier());
        assertEquals("check choice1 renamed in exiting right", "FirstChoice", eNarr.routes.get("link3").getStart().getIdentifier());
        assertEquals("check choice1 still has options", 2, eNarr.nodes.get("FirstChoice").getOptions().size());
        for (Route n : eNarr.nodes.get("FirstChoice").getOptions()) {
            assertEquals("check choice1 renamed in options", "FirstChoice", n.getStart().getIdentifier());
        }
        //traverse(eNarr.start);
    }
    
    @Test
    public void removeRouteTest() {
        eNarr.removeRoute("link1");
        assertNull("Check link1 removed from routes", eNarr.routes.get("link1"));
        assertEquals("Check link1 removed from start options", 0, eNarr.start.getOptions().size());
        //traverse(eNarr.start);
        
        eNarr.removeRoute("link3");
        assertEquals("Check link3 removed from choice1 options", 1, eNarr.nodes.get("choice1").getOptions().size());
        //traverse(eNarr.nodes.get("choice1"));
    }
    
    @Test
    public void removeNodeTest() {
        eNarr.removeNode("left");
        assertNull("Check left removed from nodes", eNarr.nodes.get("left"));
        assertNull("Check entering route removed from routes", eNarr.routes.get("link2"));
        assertNull("Check exiting route removed from routes", eNarr.routes.get("link4"));
        //traverse(eNarr.start);
        
        setup();
        eNarr.removeNode("choice1");
        assertNull("Check choice1 removed from nodes", eNarr.nodes.get("choice1"));
        assertNull("Check entering route removed from routes", eNarr.routes.get("link1"));
        assertNull("Check left exiting route removed", eNarr.routes.get("link2"));
        assertNull("Check right exiting route removed", eNarr.routes.get("link3"));
        //traverse(eNarr.start);
        //traverse(eNarr.nodes.get("left"));
        //traverse(eNarr.nodes.get("right"));
    }
}
