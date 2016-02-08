package uk.ac.cam.echo2016.multinarrative;

import java.io.Serializable;
import java.util.HashMap;

import uk.ac.cam.echo2016.multinarrative.Route;
import uk.ac.cam.echo2016.multinarrative.Node;

/**
 * 
 * @author tr393
 * @author eyx20
 * @version 1.0
 * @see GUINarrative
 */
public abstract class MultiNarrative implements Serializable { //TODO Documentation
    private static final long serialVersionUID = 1;
    protected HashMap<String, Route> routes = new HashMap<String, Route>();
    protected HashMap<String, Node> nodes = new HashMap<String, Node>();
    protected SynchronizationNode start;

    // TODO change ALL getters and setters to protected?
    public Node getNode(String id) {
        return nodes.get(id);
    }

    public Route getRoute(String id) {
        return routes.get(id);
    }
}
