package uk.ac.cam.echo2016.multinarrative;

import android.os.BaseBundle;

/**
 * The EditableNarrative that is edited using the GUI.
 * 
 * <p>
 * ALT: The {@code MultiNarrative} graph structure used by the {@code FXMLGUI} editor to store the game design. This
 * graph is generated alongside the editor and used to build the template when the design is finished. New {@code Node}s
 * and {@code Route}s can be added 
 * 
 * 
 * @author tr393
 * @author eyx20
 * @version 1.0
 * @throws NonUniqueIdException
 * @see MultiNarrative
 */
public class GUINarrative extends EditableNarrative { // TODO Finish Documentation
    private static final long serialVersionUID = 1;

    public void newRoute(String id, String startId, String endId)
            throws NonUniqueIdException, GraphElementNotFoundException {
        if (isUniqueId(id)) {
            Node startNode = getNode(startId);
            if (startNode == null)
                throw new GraphElementNotFoundException("Node with id: " + startId + " not found");
            Node endNode = getNode(endId);
            if (endNode == null)
                throw new GraphElementNotFoundException("Node with id: " + endId + " not found");

            Route route = new Route(id, startNode, endNode);
            addRoute(route);
        } else {
            throw new NonUniqueIdException("Invalid id: " + id + " is not unique.");
        }
    }

    public void newSynchronizationNode(String id) throws NonUniqueIdException {
        if (isUniqueId(id))
            nodes.put(id, new SynchronizationNode(id));
        else
            throw new NonUniqueIdException("Invalid id: " + id + " is not unique.");
    }

    public void newChoiceNode(String id) throws NonUniqueIdException {
        if (isUniqueId(id))
            nodes.put(id, new ChoiceNode(id));
        else
            throw new NonUniqueIdException("Invalid id: " + id + " is not unique.");
    }

    // TODO documentation(+overloaded function) + better refactoring?
    public void insertChoiceOnRoute(String routeId, String newChoiceId, String newRouteId)
            throws NonUniqueIdException, GraphElementNotFoundException {
        if (isUniqueId(newChoiceId) && isUniqueId(newRouteId)) {
            Route route2 = getRoute(routeId);
            if (route2 == null) throw new GraphElementNotFoundException("Route with id: " + routeId + " not found");

            ChoiceNode choice = new ChoiceNode(newChoiceId);
            Route route1 = new Route(newRouteId, route2.getStart(), choice);
            route2.setStart(choice);
            routes.put(route1.getIdentifier(), route1);
            nodes.put(choice.getIdentifier(), choice);
        } else {
            throw new NonUniqueIdException(
                    "Invalid id: " + (isUniqueId(newChoiceId) ? newChoiceId : newRouteId) + " is not unique.");
        }
    }

    public void insertChoiceOnRoute(String routeId, String newChoiceId, String newRouteId1, String newRouteId2)
            throws NonUniqueIdException, GraphElementNotFoundException {
        if (isUniqueId(newChoiceId) && isUniqueId(newRouteId1) && isUniqueId(newRouteId2)) {
            Route route = getRoute(routeId);
            if (route == null) throw new GraphElementNotFoundException("Route with id: " + routeId + " not found");
            
            ChoiceNode choice = new ChoiceNode(newChoiceId);
            Route route1 = new Route(newRouteId1, route.getStart(), choice);
            Route route2 = new Route(newRouteId1, choice, route.getEnd());
            routes.remove(route);
            routes.put(route1.getIdentifier(), route1);
            routes.put(route2.getIdentifier(),route2);
            nodes.put(choice.getIdentifier(), choice);
        } else {
            throw new NonUniqueIdException("Invalid id: "
                    + (isUniqueId(newChoiceId) ? (isUniqueId(newRouteId1) ? newRouteId2 : newRouteId1) : newChoiceId)
                    + " is not unique.");
        }
    }

    private boolean isUniqueId(String id) {
        Route route = getRoute(id);
        Node node = getNode(id);
        return ((route == null) && (node == null));
    }

    public boolean setStartPoint(String id) throws GraphElementNotFoundException {
        Node node = getNode(id);
        if (node == null) throw new GraphElementNotFoundException("Node with id: " + id + " not found");
        if (node instanceof SynchronizationNode) {
            start = (SynchronizationNode) node;
            return true;
        } else { 
            return false;
        }
    }

    public BaseBundle getProperties(String id) throws GraphElementNotFoundException {
        Route route = getRoute(id);
        if (route != null) {
            return route.getProperties();
        } else {
            Node node = getNode(id);
            if (node != null) {
                return node.getProperties();
            }
        }
        throw new GraphElementNotFoundException("Element with id: " + id + " not found");
    }
}
