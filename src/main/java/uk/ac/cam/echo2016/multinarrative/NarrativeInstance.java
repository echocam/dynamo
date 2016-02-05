package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.BaseBundle;

/**
 * 
 * Represents an actual play through of the story. Instantiated from the template.
 * 
 * @author tr39
 * @author rjm232
 * @version 1.0
 *
 */
public class NarrativeInstance extends MultiNarrative { // TODO Documentation
    private static final long serialVersionUID = 1;
    protected BaseBundle properties;
    protected ArrayList<Node> activeNodes = new ArrayList<Node>();

    public NarrativeInstance(NarrativeTemplate template) throws NullPointerException { // TODO testing
        NarrativeInstance base = template.generateInstance();
        this.routes = base.routes;
        this.nodes = base.nodes;
        this.start = base.start;
        this.properties = base.properties;
    }

    public NarrativeInstance(HashMap<String, Route> routes, HashMap<String, Node> nodes, Node start) {
        this.routes = routes;
        this.nodes = nodes;
        this.start = start;
    }

    public NarrativeInstance() {
        
    }

    public BaseBundle getGlobalProperties() {
        return properties;
    }

    public BaseBundle getRouteProperties(String id) {
        return getRoute(id).getProperties();
    }

    public BaseBundle getNodeProperties(String id) {
        return getNode(id).getProperties();
    }

    public BaseBundle startRoute(String id) {
        Route route = getRoute(id);
        route.start.startRoute(route);
        return route.getProperties();
    }

    public GameChoice endRoute(String id) {
        Route finished = getRoute(id);
        return finished.getEnd().onEntry(finished, this);
    }

    /**
     * Recursively deletes an item from the graph according to the instance this method is called from. Only nodes and
     * routes further down the tree are deleted, so nodes must have no entering routes and routes must start
     * from a node with other options available.
     * 
     * @param id
     *            string identifier for the item to be deleted
     * @return
     */
    public boolean kill(String id) { // TODO More Documentation, including overloaded methods
        Route route = getRoute(id);
        if (route != null) { // TODO alternate exception handling?
            kill(route);
            return true;
        } else {
            Node node = getNode(id);
            if (node != null) { // TODO alternate exception handling?
                kill(node);
                return true;
            }
            return false;
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
        
        System.out.println("Killing: " + route.getIdentifier());
        Node nEnd = route.getEnd();
        String s = nEnd == null ? "null"
            : nEnd + " " + (nEnd.getProperties() == null ? "null"
                : nEnd.getProperties() + " " + nEnd.getProperties().getInt("Impl.Node.Entries"));
        System.out.println(s);
        
        int routeEntres = nEnd.getProperties().getInt("Impl.Node.Entries"); // TODO improve naming convention?
        --routeEntres;
        nEnd.getProperties().putInt("Impl.Node.Entries", routeEntres);

        if (routeEntres == 0) {
            kill(nEnd);
        }
        // TODO and update event if instanceof sync node? i.e. change to ACTION_CONTINUE?

        Node nStart = route.getStart();
        nStart.getOptions().remove(route); // TODO should return true, otherwise something's broken

        routes.remove(route.getIdentifier());
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
        for (Route route : new ArrayList<Route>(node.getOptions())) {
            kill(route); // copy of ArrayList used to allow deletion of nodes within the function
        }

        assert node.getProperties().getInt("Impl.Node.Entries") == 0;
        nodes.remove(node.getIdentifier());
        return true;
    }

    public ArrayList<Route> getPlayableRoutes() { // TODO implementation + todo's
        return null;
    }

    public void setActive(Node node) {
        activeNodes.add(node);
    }
}
