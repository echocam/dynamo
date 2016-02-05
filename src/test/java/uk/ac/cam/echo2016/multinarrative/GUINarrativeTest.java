package uk.ac.cam.echo2016.multinarrative;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Before;

/**
 * Here there are commented out traverse statements. One can uncomment them to see the structure of
 * the graph.
 * 
 * @author rjm232
 * @author tj393
 *
 */
public class GUINarrativeTest { // TODO add actual GUINarrative tests
    public static void follow(Route route) {
        System.out.println("This route is " + route.getIdentifier());
        System.out.println("It connects " + route.getStart().getIdentifier() + " to " + route.getEnd().getIdentifier());
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
    
    
    GUINarrative gNarr;
    
    /**
     * Builds graph structure. Uncomment the traverse statement in addRouteAndNodeTest to see 
     * the structure.
     * ________________
     * _____start______
     * ______1|________
     * _______O-choice1
     * _____2/_\3______
     * _____/___\______
     * __left___right__
     * ___4\____5/_____
     * _____\___/______
     * ______end_______
     * ________________
     * 
     * @see addRouteAndNodeTest
     */
    @Before
    public void setup() {
        gNarr = new GUINarrative();
        
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
        gNarr.addNode(s1);
        gNarr.addNode(c1);
        gNarr.addNode(s2);
        gNarr.addNode(s3);
        gNarr.addNode(s4);
        gNarr.addRoute(l1);
        gNarr.addRoute(l2);
        gNarr.addRoute(l3);
        gNarr.addRoute(l4);
        gNarr.addRoute(l5);
        gNarr.start = s1;
    }
    
    @Test
    public void addRouteAndNodeTest() {
        //traverse(gNarr.start);
    }
    
    @Test
    public void renameRouteTest() {
        gNarr.renameRoute("link4", "fromLeftToEnd");
        assertEquals("Check link4 renamed internally", "fromLeftToEnd", gNarr.routes.get("fromLeftToEnd").getIdentifier());
        assertEquals("Check link4 renamed in start Node", "fromLeftToEnd", gNarr.nodes.get("left").getOptions().get(0).getIdentifier());
        //traverse(gNarr.start);
    }
    
    @Test
    public void renameNodeTest() {
        gNarr.renameNode("choice1", "FirstChoice");
        assertEquals("check choice1 renamed internally", "FirstChoice", gNarr.nodes.get("FirstChoice").getIdentifier());
        assertEquals("check choice1 renamed in entering narr", "FirstChoice", gNarr.routes.get("link1").getEnd().getIdentifier());
        assertEquals("check choice1 renamed in exiting left", "FirstChoice", gNarr.routes.get("link2").getStart().getIdentifier());
        assertEquals("check choice1 renamed in exiting right", "FirstChoice", gNarr.routes.get("link3").getStart().getIdentifier());
        assertEquals("check choice1 still has options", 2, gNarr.nodes.get("FirstChoice").getOptions().size());
        for (Route n : gNarr.nodes.get("FirstChoice").getOptions()) {
            assertEquals("check choice1 renamed in options", "FirstChoice", n.getStart().getIdentifier());
        }
        //traverse(gNarr.start);
    }
    
    @Test
    public void removeRouteTest() {
        gNarr.removeRoute("link1");
        assertNull("Check link1 removed from routes", gNarr.routes.get("link1"));
        assertEquals("Check link1 removed from start options", 0, gNarr.start.getOptions().size());
        //traverse(gNarr.start);
        
        gNarr.removeRoute("link3");
        assertEquals("Check link3 removed from choice1 options", 1, gNarr.nodes.get("choice1").getOptions().size());
        //traverse(gNarr.nodes.get("choice1"));
    }
    
    @Test
    public void removeNodeTest() {
        gNarr.removeNode("left");
        assertNull("Check left removed from nodes", gNarr.nodes.get("left"));
        assertNull("Check entering route removed from routes", gNarr.routes.get("link2"));
        assertNull("Check exiting route removed from routes", gNarr.routes.get("link4"));
        //traverse(gNarr.start);
        
        setup();
        gNarr.removeNode("choice1");
        assertNull("Check choice1 removed from nodes", gNarr.nodes.get("choice1"));
        assertNull("Check entering route removed from routes", gNarr.routes.get("link1"));
        assertNull("Check left exiting route removed", gNarr.routes.get("link2"));
        assertNull("Check right exiting route removed", gNarr.routes.get("link3"));
        //traverse(gNarr.start);
        //traverse(gNarr.nodes.get("left"));
        //traverse(gNarr.nodes.get("right"));
    }
    
    /**
     * This test builds the graph, removes the route numbered 2 in the diagram and then adds it again.    
     * @throws NonUniqueIdException 
     */
    @Test
    public void newRouteTest() throws NonUniqueIdException {
    	gNarr.removeRoute("link4");
    	gNarr.newRoute("newLink4", "left", "end");
    	
    	assertNotNull("Check route added to routes", gNarr.getRoute("newLink4"));
    	assertEquals("Check start node has correct name reference", "newLink4", gNarr.getRoute("newLink4").getStart().getOptions().get(0).getIdentifier());
    	assertEquals("Check route has been added only once", 1, gNarr.getRoute("newLink4").getStart().getOptions().size());
    	//traverse(gNarr.start);
    }
    
    @Test(expected=NonUniqueIdException.class)
    public void newRouteExceptionTest() throws NonUniqueIdException {
    	gNarr.newRoute("link2", "start", "end");
    }
    
    @Test
    public void newSyncAndChoiceNodeTest() throws NonUniqueIdException {
    	gNarr.newSynchronizationNode("syncTest");
    	gNarr.insertChoiceNode("choiceTest");
    	
    	assertNotNull("Check Sync node added to nodes", gNarr.getNode("syncTest"));
    	assertEquals("Check Sync node is of correct type", SynchronizationNode.class, gNarr.getNode("syncTest").getClass());
    	assertNotNull("Check Choice node added to nodes", gNarr.getNode("choiceTest"));
    	assertEquals("Check Choice node is of correct type", ChoiceNode.class, gNarr.getNode("choiceTest").getClass());
    }
    
    @Test(expected=NonUniqueIdException.class)
    public void newSyncExceptionTest() throws NonUniqueIdException {
    	gNarr.newSynchronizationNode("right");
    }
    
    @Test(expected=NonUniqueIdException.class)
    public void newChoiceExceptionTest() throws NonUniqueIdException {
    	gNarr.insertChoiceNode("left");
    }
}
