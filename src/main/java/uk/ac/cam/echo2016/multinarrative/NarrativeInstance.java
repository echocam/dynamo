package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.BaseBundle;
import uk.ac.cam.echo2016.multinarrative.dev.Debug;

/**
 * 
 * Represents an actual play through of the story. Instantiated from the template.
 * 
 * @author tr39
 * @author rjm232
 * @author jr650
 * @version 1.0
 *
 */
public class NarrativeInstance extends MultiNarrative { // TODO Documentation
    private static final long serialVersionUID = 1;
    protected ArrayList<Node> activeNodes = new ArrayList<Node>();

    public NarrativeInstance(NarrativeTemplate template) throws InvalidGraphException { // TODO Clean this up?
        NarrativeInstance base = template.generateInstance();
        this.routes = base.routes;
        this.nodes = base.nodes;
        this.start = base.start;
        this.properties = base.properties;
    }

    public NarrativeInstance(HashMap<String, Route> routes, HashMap<String, Node> nodes, SynchronizationNode start, BaseBundle properties) {
        this.routes = routes;
        this.nodes = nodes;
        this.start = start;
        this.properties = properties;
    }

    public NarrativeInstance() {
    }
    
    public BaseBundle startRoute(String id) {
        Route route = getRoute(id);
        Node startNode = route.getStart();
        if (startNode instanceof ChoiceNode) {
        	activeNodes.remove(startNode);
        	for (Route deadRoute : startNode.getExiting()) {
        		if (deadRoute != route) {
        			kill(deadRoute); 
        		}
        	}
        } else {
        	if (startNode.getExiting().size() == 1) {
        		activeNodes.remove(startNode);
        	}
        }
        return startNode.startRoute(route);
    }

    public GameChoice endRoute(String id) throws GraphElementNotFoundException {
        Route route = getRoute(id);
        Node endNode = route.getEnd(); // TODO handle error - return null
        activeNodes.add(endNode);
        route.getProperties().putBoolean("System.isCompleted", true);        
        return endNode.onEntry(route, this);
    }

    /**
     * Recursively deletes an item from the graph according to the instance this method is called from. Only nodes and
     * routes further down the tree are deleted, so nodes must have no entering routes and routes must start
     * from a node with other exiting routes available.
     * 
     * @param id
     *            string identifier for the item to be deleted
     * @return
     */
    public boolean kill(String id) { // TODO More Documentation, including overloaded methods
        Route route = getRoute(id);
        if (route != null) {
            kill(route);
            return true; // TODO alternate exception handling?
        } else {
            Node node = getNode(id);
            if (node != null) {
                kill(node);
                return true;
            }
            return false; // TODO alternate exception handling?
        }
    }

    /**
     * {@link NarrativeInstance#kill(String)}
     * 
     * @see NarrativeInstance#kill(String)
     */
    public boolean kill(Route route) {
        if (route == null)
            return false;
        
//        System.out.println("Killing: " + route.getId());
        Node nEnd = route.getEnd();
        
        Debug.logInfo("Killing " + route.getId(), 4, 0); // TODO change class

        nEnd.getEntering().remove(route);
        // If there are now no routes entering the node, kill it
        if (nEnd.getEntering().size() == 0) {
            kill(nEnd);
        } else if (route.getProperties() != null){
            ArrayList<String> primaries = new ArrayList<String>(route.getProperties().getStringArrayList("Primaries"));
            // Kills all methods leaving the end node if they have the same primary property and no entering routes also
            // have that property TODO specify in documentation
            for (String primary : primaries) {
                boolean similarRouteExists = false;
                for(Route entry : nEnd.getEntering()) {
                    if (entry.getProperties().getStringArrayList("Primaries").contains(primary))
                        similarRouteExists = true;
                }
                if (!similarRouteExists) {
                    for(Route option : nEnd.getExiting()) {
                        if (option.getProperties().getStringArrayList("Primaries").contains(primary)) {
                            kill(option);
                        }
                    }
                }
            }
        }
        // Remove the route from the exiting routes of the node it comes from
        Node nStart = route.getStart();
        nStart.getExiting().remove(route); // Should return true, otherwise something's broken

        routes.remove(route.getId());
        return true;
    }

    /**
     * {@link NarrativeInstance#kill(String)}
     * 
     * @see NarrativeInstance#kill(String)
     */
    public boolean kill(Node node) {
        if (node == null)
            return false;
        for (Route route : new ArrayList<Route>(node.getExiting())) {
            kill(route); // Copy of ArrayList used to allow deletion of nodes within the function
        }

        // As specified in the javadoc
        assert node.getEntering().size() == 0;
        
        nodes.remove(node.getId());
        return true;
    }

    public ArrayList<Route> getPlayableRoutes() {
        ArrayList<Route> r_routes = new ArrayList<Route>();
        for (Node node : activeNodes) {
            for (Route route : node.getExiting()) {
                r_routes.add(route);
            }
        }
        return r_routes;
    }

    public void setActive(Node node) {
        if (!activeNodes.contains(node))
            activeNodes.add(node);
    }
}
