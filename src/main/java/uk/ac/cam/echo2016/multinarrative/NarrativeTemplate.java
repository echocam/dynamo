package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

import android.os.BaseBundle;

/**
 * 
 * The template of the story from which the copy of the game required for each
 * play through is derived.
 * 
 * <p>
 * ALT: A full {@code MultiNarrative} graph that is used to create new
 * {@code NarrativeInstance}s for each playthrough. This contains all {@code Node}s and
 * {@code Route}s on the graph designed in the {@code FXMLGUI} editor, and is the
 * template graph used for new save files. When the template is created, it
 * should not be modified in any way. (Unless the programmer wants different
 * behaviour across new playthroughs!)
 * 
 * @author tr393
 * @author rjm232
 * @author jr650
 * @version 1.0
 * @see NarrativeTemplate#generateInstance()
 * @see NarrativeInstance
 * @see MultiNarrative
 */
public class NarrativeTemplate extends MultiNarrative {
    private static final long serialVersionUID = 1;

    // TODO Documentation
    /**
     *
     * @return
     */
    public NarrativeInstance generateInstance() {
        HashMap<String, Node> r_nodes = new HashMap<>();
        HashMap<String, Route> r_routes = new HashMap<>();
        
        if (start == null) {throw new RuntimeException();} // TODO better exception
        
        for (Node node : nodes.values()) {
            Node r_node = node.clone();
            r_node.createProperties();
            r_node.setExiting(new ArrayList<Route>());
            r_node.setEntering(new ArrayList<Route>());
            r_nodes.put(node.getId(), r_node);
        }

        for (Route route : routes.values()) {
            Route r_route = route.clone();
            // Find Start and end in r_nodes
            
            r_route.setStart(r_nodes.get(route.getStart().getId()));
            r_route.setEnd(r_nodes.get(route.getEnd().getId()));
            r_route.getStart().getExiting().add(r_route);
            r_route.getEnd().getEntering().add(r_route);
            
            // Increments the route entries property (if not found initialised to 0)
            int routeEntries = r_route.getEnd().getProperties().getInt("Impl.Node.Entries");
            r_route.getEnd().getProperties().putInt("Impl.Node.Entries", ++routeEntries);
            
            r_routes.put(route.getId(), r_route);
        }
        SynchronizationNode r_start = (SynchronizationNode) r_nodes.get(start.getId()); // TODO CHECK

        NarrativeInstance instance = new NarrativeInstance(r_routes, r_nodes, r_start, BaseBundle.deepcopy(this.properties));
        return instance;
    }
    
    
    
	 public NarrativeInstance generateInstance2() { // TODO more appropriate exception?
	 NarrativeInstance instance = new NarrativeInstance();

        if (start == null) {
            throw new RuntimeException();
        } // TODO better exception
        instance.start = (SynchronizationNode) copyToInstance(this.start, instance);
        instance.setActive(start);
        return instance;
    }
    /**
     * Copies this node and its routes and recursively calls this method on the nodes reached by the routes
     * further down the graph. The copy created is then returned. The graph instance is used to record node/route
     * references and make sure that nodes are not copied twice. The callConstructor method is effectively a clone
     * method. :P
     * 
     * @param instance
     */
    public Node copyToInstance(Node node, NarrativeInstance instance) { // TODO More Documentation

        // Eventually calls Node(this.id) via subclass's constructor
        Node result = node.create(node.getId());
        
//        Node result = null;
//        try {
//            result = node.getClass().getConstructor(String.class).newInstance(node.getIdentifier());
//        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
//                | NoSuchMethodException | SecurityException e) {
//            e.printStackTrace();
//        }
        
//        Node result = (node instanceof ChoiceNode) ? new ChoiceNode(node.getIdentifier())
//                : new SynchronizationNode(node.getIdentifier());

        if (node.getProperties() != null) // Copy getProperties() across, if any
            result.setProperties(BaseBundle.deepcopy(node.getProperties()));
        // Copy each route leaving node node and call copyGraph on their end nodes
        for (Route templateRoute : node.getExiting()) {
            Node endNodeCopy;
            if (!instance.nodes.containsKey(templateRoute.getEnd().getId())) {
                // Not already copied
                endNodeCopy = copyToInstance(templateRoute.getEnd(), instance); // Recursively copy nodes at the ends of

                // Create and update entryList property
                endNodeCopy.createProperties();

                endNodeCopy.getProperties().putInt("Impl.Node.Entries", 1);
            } else {
                // Already copied

                endNodeCopy = instance.getNode(templateRoute.getEnd().getId()); // Get reference to copied end
                // Update entryList property
                
                int routeEntries = endNodeCopy.getProperties().getInt("Impl.Node.Entries");
                routeEntries++;
                endNodeCopy.getProperties().putInt("Impl.Node.Entries", routeEntries);
            }

            // Create route using references obtained/created above, linking node node to the new end nodes
            Route routeCopy = new Route(templateRoute.getId(), templateRoute.getCharId(), result, endNodeCopy);
            routeCopy.setProperties(templateRoute.getProperties());
            
            // Update route references
            endNodeCopy.getEntering().add(routeCopy);
            result.getExiting().add(routeCopy);
            
            // Update graph references
            instance.routes.put(routeCopy.getId(), routeCopy);
        }
        instance.nodes.put(result.getId(), result);
        return result;
    }    
}
