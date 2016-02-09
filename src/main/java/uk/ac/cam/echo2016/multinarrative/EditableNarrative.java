package uk.ac.cam.echo2016.multinarrative;

import java.util.ArrayList;

import android.os.BaseBundle;

/**
 * Represents a MultiNarrative that can be edited. Included for extensibility. We subclass it with GUINarrative, but
 * this class can be derived for other methods of editing.
 * 
 * @author tr393
 * @author rjm232
 *
 */

public abstract class EditableNarrative extends MultiNarrative { //TODO Documentation
    private static final long serialVersionUID = 1;
    public void addRoute(Route route) {
        routes.put(route.getId(), route);
    }
    
    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }
    
    public boolean removeRoute(String id) { 
        Route route = routes.remove(id);
        if (route == null)
            return false;
        
        // Should not return null else graph is broken
        route.getStart().getExiting().remove(route);
        route.getEnd().getEntering().remove(route);
        
        return true;
    }
    
    public boolean removeNode(String id) {
        Node node = nodes.remove(id);
        if (node == null) 
            return false;

        // TODO replace with verion using enteringRoutes, or merge with kill()
        for (Route route : new ArrayList<Route>(routes.values())) {
            if (route.getStart() == node) {
                removeRoute(route.getId());
            } else if (route.getEnd() == node) {
                removeRoute(route.getId());			
            }
        }

        return true;		
    }
    
    public boolean renameRoute(String id, String newName) { 
        Route route = routes.remove(id);
        if (route == null)
            return false;
        
        Route newRoute = new Route(newName, route.getCharId(), route.getStart(), route.getEnd());
        
        // Update references of nodes at either end
        // Note setup must be called afterwards in case of the ids being the same
        newRoute.getStart().getExiting().remove(route);
        newRoute.getEnd().getEntering().remove(route);
        newRoute.setup();
        
        
        if (route.getProperties() != null)
            newRoute.setProperties(new BaseBundle(route.getProperties()));
        
        routes.put(newName, newRoute);
        return true;
    }
    
    public boolean renameNode(String id, String newName) {
        Node node = nodes.remove(id);
        if (node == null) 
            return false;
        
        Node newNode = node.create(newName);
        if (node.getProperties() != null) 
            newNode.setProperties(new BaseBundle(node.getProperties()));
        
        // Update references to the node
        for (Route route : node.getExiting()) {
            route.setStart(newNode);
        }
        for (Route route : node.getEntering()) {
            route.setEnd(newNode);
        }

        // Assign the nodes references to routes
        for (Route route : node.getExiting()) {
            newNode.getExiting().add(route);
        }
        for (Route route : node.getEntering()) {
            newNode.getEntering().add(route);
        }
        
        nodes.put(newName, newNode);
        return true;
    }
}
