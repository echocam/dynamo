package uk.ac.cam.echo2016.multinarrative;

import java.util.HashMap;

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
        routes.put(route.getIdentifier(), route);
        route.getStart().getOptions().add(route);
    }
    
    public void addNode(Node node) {
        nodes.put(node.getIdentifier(), node);
    }
    
    public boolean removeRoute(String id) { 
        Route route = routes.remove(id);
        if (route == null) 
            return false;
        
        route.getStart().getOptions().remove(route);	
        
        return true;
    }
    
    public boolean removeNode(String id) {
        Node node = nodes.remove(id);
        if (node == null) 
            return false;
        
        HashMap<String, Route> copy = new HashMap<String, Route>();
        copy.putAll(routes);
        
        for (Route route : copy.values()) {
            if (route.getStart() == node) {
                removeRoute(route.getIdentifier());
            } else if (route.getEnd() == node) {
                removeRoute(route.getIdentifier());			
            }
        }
        
        return true;		
    }
    
    public boolean renameRoute(String id, String newName) { 
        Route route = routes.remove(id);
        if (route == null)
            return false;
        
        Route newRoute = new Route(newName, route.getStart(), route.getEnd());
        if (route.getProperties() != null)
            newRoute.setProperties(new BaseBundle(route.getProperties()));
        
        routes.put(newName, newRoute);
        newRoute.getStart().getOptions().remove(route);
        newRoute.getStart().getOptions().add(newRoute);
        
        return true;
    }
    
    public boolean renameNode(String id, String newName) {
        Node node = nodes.remove(id);
        if (node == null) 
            return false;
        
        Node newNode = node.newInstance(newName);
        if (node.getProperties() != null) 
            newNode.setProperties(new BaseBundle(node.getProperties()));
        
        nodes.put(newName, newNode);
        for (Route route : routes.values()) {
            if (route.getStart() == node) {
                route.setStart(newNode);
            } else if (route.getEnd() == node) {
                route.setEnd(newNode);	
            }
        }
        for (Route route : node.getOptions()) {
            newNode.getOptions().add(route);
        }
        
        return true;
    }
}
