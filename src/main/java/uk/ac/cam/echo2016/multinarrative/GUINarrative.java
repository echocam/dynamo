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
public class GUINarrative extends EditableNarrative { // TODO Documentation
	private static final long serialVersionUID = 1;
	public void newRoute(String id, String start, String end) throws NonUniqueIdException { // TODO error message
		if (isUniqueId(id)) {
			Node startNode = getNode(start);
			Node endNode = getNode(end);
			Route route = new Route(id, startNode, endNode);
			startNode.getOptions().add(route);
			addRoute(route);
		} else {
			throw new NonUniqueIdException();
		}
	}

    public void newSynchronizationNode(String id) throws NonUniqueIdException { // TODO error message
        if (isUniqueId(id))
            nodes.put(id, new SynchronizationNode(id));
        else
            throw new NonUniqueIdException();
    }

    public void insertChoiceNode(String id) throws NonUniqueIdException { // TODO error message
        if (isUniqueId(id))
            nodes.put(id, new ChoiceNode(id));
        else
            throw new NonUniqueIdException();
    }

    // TODO documentation(+overloaded function) + better refactoring?
    public void insertChoiceOnRoute(String routeId, String newChoiceId, String newRouteId)
            throws NonUniqueIdException { // TODO error for elementNotFound/ error message
        if (isUniqueId(newChoiceId) && isUniqueId(newRouteId)) {
            Route route2 = getRoute(routeId);
            ChoiceNode choice = new ChoiceNode(newChoiceId);
            Route route1 = new Route(newRouteId, route2.getStart(), choice);
            route2.setStart(choice);
            routes.put(route1.getIdentifier(), route1);
            nodes.put(choice.getIdentifier(), choice);
        } else {
            throw new NonUniqueIdException();
        }
    }

    public void insertChoiceOnRoute(String routeId, String newChoiceId, String newRouteId1, String newRouteId2)
            throws NonUniqueIdException { // TODO error for elementNotFound/error message
        if (isUniqueId(newChoiceId) && isUniqueId(newRouteId1) && isUniqueId(newRouteId2)) {
            Route route = getRoute(routeId);
            ChoiceNode choice = new ChoiceNode(newChoiceId);
            Route route1 = new Route(newRouteId1, route.getStart(), choice);
            Route route2 = new Route(newRouteId1, choice, route.getEnd());
            routes.remove(route);
            routes.put(route1.getIdentifier(), route1);
            routes.put(route2.getIdentifier(),route2);
            nodes.put(choice.getIdentifier(), choice);
        } else {
            throw new NonUniqueIdException();
        }
    }

    private boolean isUniqueId(String id) {
        Route route = getRoute(id);
        Node node = getNode(id);
        return ((route == null) && (node == null));
    }

    public void setStartPoint(String id) { // TODO error for elementNotFound
        Node node = getNode(id);
        start = node;
    }

    public BaseBundle getProperties(String id) { // TODO error for elementNotFound
        Route route = getRoute(id);
        if (route != null) { // TODO alternate exception handling?
            return route.getProperties();
        } else {
            Node node = getNode(id);
            if (node != null) { // TODO alternate exception handling?
                return node.getProperties();
            }
        }
        return null;
    }
}
