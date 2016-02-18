package uk.ac.cam.echo2016.multinarrative.gui;

import static uk.ac.cam.echo2016.multinarrative.gui.Strings.ADD_EMPTY_STRING;
import static uk.ac.cam.echo2016.multinarrative.gui.Strings.CANNOT_FORMAT;
import static uk.ac.cam.echo2016.multinarrative.gui.Strings.CYCLE_EXISTS;
import static uk.ac.cam.echo2016.multinarrative.gui.Strings.INVALID_TYPE;
import static uk.ac.cam.echo2016.multinarrative.gui.Strings.ITEM_ALREADY_EXISTS;
import static uk.ac.cam.echo2016.multinarrative.gui.Strings.ITEM_DOES_NOT_EXIST;
import static uk.ac.cam.echo2016.multinarrative.gui.Strings.NODE_PREFIX;
import static uk.ac.cam.echo2016.multinarrative.gui.Strings.ROUTE_PREFIX;
import static uk.ac.cam.echo2016.multinarrative.gui.Strings.SYSTEM_PROPERTY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.BaseBundle;
import javafx.scene.paint.Color;
import uk.ac.cam.echo2016.multinarrative.ChoiceNode;
import uk.ac.cam.echo2016.multinarrative.GUINarrative;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;
import uk.ac.cam.echo2016.multinarrative.Node;
import uk.ac.cam.echo2016.multinarrative.NonUniqueIdException;
import uk.ac.cam.echo2016.multinarrative.Route;
import uk.ac.cam.echo2016.multinarrative.SynchronizationNode;
import uk.ac.cam.echo2016.multinarrative.gui.operations.DFSCycleDetect;
import uk.ac.cam.echo2016.multinarrative.gui.operations.IllegalOperationException;

/**
 * The class that encapsulates all GUI operations.
 * 
 * Every method that mutates or changes the state of the program in some form
 * MUST be implemented using the Operation class. This is to allow for the
 * undoing of operations.
 * 
 * @author jr650
 * @author eyx20
 * @author rjm232
 */
public class NarrativeOperations {

    private GUINarrative multinarrative;

    private Map<String, BaseBundle> properties;
    private Map<String, Map<String, Color>> colors = new HashMap<String, Map<String, Color>>();
    private static int nodeCounter = 1;
    private static int narrativeCounter = 1;
    private static int valueCounter = 1;

    /**
     * Constructor. Creates a new GUIOperations.
     */
    public NarrativeOperations() {
        multinarrative = new GUINarrative();
        properties = new HashMap<String, BaseBundle>();
    }

    /**
     * Adds the required property
     * 
     * @throws IllegalOperationException
     *             if can't add property, message of exception is displayed to
     *             the user, using the Strings class for formatting.
     */
    public void addProperty(String s, BaseBundle b) throws IllegalOperationException {

        if (s.equals("") || s == null) {
            throw new IllegalOperationException(ADD_EMPTY_STRING);
        }
        if (properties.containsKey(s)) {
            throw new IllegalOperationException(ITEM_ALREADY_EXISTS, s);
        }
        properties.put(s, b);

    }

    /**
     * TODO Sets property type, which this class should then use to validate
     * further input.
     * 
     * @param property
     * @param type
     * @throws IllegalOperationException
     */
    public void setPropertyType(String property, String type) throws IllegalOperationException {

    }

    /**
     * TODO Gets property type.
     * 
     * @param property
     * @param type
     * @throws IllegalOperationException
     */
    public String getPropertyType(String property) throws IllegalOperationException {
        if(property.equals("GUI.X") || property.equals("GUI.Y")){
            return "Double";
        }
        if (!properties.containsKey(property)) {
            throw new IllegalOperationException(ITEM_DOES_NOT_EXIST,property);
        }
        if (properties.get(property).isEmpty()) {
            return "String";
        }
        Object o = properties.get(property).valueSet().iterator().next();
        if (o instanceof String) {
            return "String";
        } else if (o instanceof Integer) {
            return "Integer";
        } else if (o instanceof Boolean) {
            return "Boolean";
        } else if (o instanceof Byte) {
            return "Byte";
        } else if (o instanceof Short) {
            return "Short";
        } else if (o instanceof Long) {
            return "Long";
        } else if (o instanceof Float) {
            return "Float";
        } else if (o instanceof Double) {
            return "Double";
        } else {
            throw new IllegalOperationException(INVALID_TYPE);
        }

    }

    /**
     * Adds a value to a given property
     * 
     * @param property
     *            - property name to add value to
     * @param type
     *            - data type of the value, selected from {"String", "Integer",
     *            "Boolean", "Byte", "Short", "Long", "Float", "Double"}
     * @param value
     *            - value to be added
     * @throws IllegalOperationException
     *             when value cannot be added to the property. Informative
     *             message is sent to the user.
     */
    public void addValue(String property, String type, String value) throws IllegalOperationException {
        if (!properties.containsKey(property)) {
            throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, property);
        }
        try {
            switch (type) {
            case "String":
                properties.get(property).putString(value, value);
                break;
            case "Integer":
                properties.get(property).putInt(value, Integer.parseInt(value));
                break;
            case "Boolean":
                properties.get(property).putBoolean(value, Boolean.parseBoolean(value));
                break;
            case "Byte":
                properties.get(property).putByte(value, Byte.parseByte(value));
                break;
            case "Short":
                properties.get(property).putShort(value, Short.parseShort(value));
                break;
            case "Long":
                properties.get(property).putLong(value, Long.parseLong(value));
                break;
            case "Float":
                properties.get(property).putFloat(value, Float.parseFloat(value));
                break;
            case "Double":
                properties.get(property).putDouble(value, Double.parseDouble(value));
                break;
            default:
                throw new IllegalOperationException(INVALID_TYPE);

            }
        } catch (NumberFormatException e) {
            throw new IllegalOperationException(e, CANNOT_FORMAT, value, type);
        }

    }

    /**
     * Removes value from property
     * 
     * @param id
     * @param value
     * @throws IllegalOperationException
     */
    public void removeValue(String id, String type, String value) throws IllegalOperationException {
        if (!properties.containsKey(id)) {
            throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, id);
        }
        properties.get(id).remove(value);
    }

    /**
     * TODO get default value for correct type
     * 
     * @param id
     * @param type
     * @return
     */
    public String getDefaultValue(String id, String type) {
        return Strings.populateString(Strings.PROPERTY_VALUE, "" + valueCounter++);
    }

    /**
     * Gets a name that's not already in the graph.
     * 
     * @return new node name
     */
    public String getUniqueNodeName() {
        String newName = Strings.populateString(NODE_PREFIX, nodeCounter);
        if (multinarrative.getNode(newName) == null) {
            nodeCounter += 1;
            return newName;
        } else {
            while (multinarrative.getNode(newName) != null) {
                nodeCounter += 1;
                newName = Strings.populateString(NODE_PREFIX, nodeCounter);
            }
            nodeCounter += 1;
            return newName;
        }
    }

    /**
     * Adds a node, throwing exception if it fails.
     * 
     * @throws NonUniqueIdException
     */
    public void addSynchNode(String name, double x, double y) throws IllegalOperationException {

        if (multinarrative.getNode(name) != null) {
            throw new IllegalOperationException(ITEM_ALREADY_EXISTS, name);
        }
        SynchronizationNode newNode = new SynchronizationNode(name);
        newNode.createProperties();
        newNode.getProperties().putDouble("GUI.X", x);
        newNode.getProperties().putDouble("GUI.Y", y);
        multinarrative.getNodes().put(name, newNode);

    }

    /**
     * Adds a choiceNode at the given position, with the given name
     */
    public void addChoiceNode(String name, double x, double y) throws IllegalOperationException {

        if (multinarrative.getNode(name) != null) {
            throw new IllegalOperationException(ITEM_ALREADY_EXISTS, name);
        }
        ChoiceNode newNode = new ChoiceNode(name);
        newNode.createProperties();
        newNode.getProperties().putDouble("GUI.X", x);
        newNode.getProperties().putDouble("GUI.Y", y);
        multinarrative.getNodes().put(name, newNode);

    }

    /**
     * Repositions a node by the given offset
     */
    public void translateNode(String name, double x, double y) throws IllegalOperationException {
        Node theNode = multinarrative.getNode(name);
        if (theNode == null) {
            throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, name);
        }
        double transx = x + theNode.getProperties().getDouble("GUI.X");
        double transy = y + theNode.getProperties().getDouble("GUI.Y");
        theNode.getProperties().putDouble("GUI.X", transx);
        theNode.getProperties().putDouble("GUI.Y", transy);
    }

    /**
     * Gets a name that's not already in the graph.
     * 
     * @return unique narrative name
     */
    public String getUniqueRouteName() {
        String newName = Strings.populateString(ROUTE_PREFIX, narrativeCounter);
        if (!multinarrative.getRoutes().containsKey(newName)) {
            narrativeCounter += 1;
            return newName;
        } else {
            while (multinarrative.getRoutes().containsKey(newName)) {
                narrativeCounter += 1;
                newName = Strings.populateString(ROUTE_PREFIX, narrativeCounter);
            }
            narrativeCounter += 1;
            return newName;
        }
    }

    /**
     * 
     * @param node
     *            Route id
     * @return List of properties in the form "name=value"
     */
    public List<String> getRouteProperties(String route) {
        BaseBundle props = multinarrative.getRoute(route).getProperties();
        ArrayList<String> r = new ArrayList<String>();
        if (props != null) {
            for (String name : props.keySet()) {
                r.add(name + "=" + props.get(name).toString());
            }
        }
        return r;
    }

    /**
     * Adds a route, throwing exception if it fails, due to names existing or a
     * cycle being created.
     * 
     * @param name
     *            - unique id of the route
     * @param start
     *            - starting node. If node does not exist creates a new node.
     * @param end
     *            - ending node. If node does not exist creates a new node.
     */
    public void addRoute(String name, String start, String end) throws IllegalOperationException {
        try {
            multinarrative.newRoute(name, start, end);
        } catch (NonUniqueIdException e) {
            throw new IllegalOperationException(ITEM_ALREADY_EXISTS, name);
        } catch (GraphElementNotFoundException e) {
            if (e.getItem().equals(start))
                throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, start);
            else
                throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, end);
        }
        DFSCycleDetect cycleDetect = new DFSCycleDetect(multinarrative.getNode(start));
        if (cycleDetect.hasCycle()) {
            multinarrative.removeRoute(name);
            throw new IllegalOperationException(CYCLE_EXISTS);
        }
    }

    /**
     * 
     * @param node
     *            Node id
     * @return List of properties in the form "name=value"
     */
    public List<String> getNodeProperties(String node) {
        BaseBundle props = multinarrative.getNode(node).getProperties();
        ArrayList<String> r = new ArrayList<String>();
        if (props != null) {
            for (String name : props.keySet()) {
                r.add(name + "=" + props.get(name).toString());
            }
        }
        return r;
    }

    /**
     * 
     * @param from
     * @param to
     * @throws IllegalOperationException
     */
    public void renameNode(String from, String to) throws IllegalOperationException {
        if (!from.equals(to) && multinarrative.isUniqueId(to)) {
            multinarrative.renameNode(from, to);
        }

    }

    /**
     * 
     * @param from
     * @param to
     * @throws IllegalOperationException
     */
    public void renameRoute(String from, String to) throws IllegalOperationException {

        if (!from.equals(to) && multinarrative.isUniqueId(to))
            multinarrative.renameRoute(from, to);

    }

    /**
     * 
     * @param route
     * @param node
     */
    public void setEnd(String route, String node) throws IllegalOperationException {

        Route mRoute = multinarrative.getRoute(route);
        Node mOldEnd = mRoute.getEnd();

        try {
            multinarrative.setEnd(route, node);
        } catch (GraphElementNotFoundException e) {
            throw new IllegalOperationException(e, ITEM_DOES_NOT_EXIST, e.getItem());
        }
        DFSCycleDetect detect = new DFSCycleDetect(multinarrative.getRoute(route).getStart());
        if (detect.hasCycle()) {
            try {
                multinarrative.setEnd(route, mOldEnd.getId());
            } catch (GraphElementNotFoundException e) {
                throw new IllegalOperationException(e, ITEM_DOES_NOT_EXIST, e.getItem());
            }
            throw new IllegalOperationException(CYCLE_EXISTS);
        }

    }

    /**
     * 
     * @param route
     * @param node
     */
    public void setStart(String route, String node) throws IllegalOperationException {

        Route mRoute = multinarrative.getRoute(route);
        Node mOldStart = mRoute.getStart();

        try {
            multinarrative.setStart(route, node);
        } catch (GraphElementNotFoundException e) {
            throw new IllegalOperationException(e, ITEM_DOES_NOT_EXIST, e.getItem());
        }
        DFSCycleDetect detect = new DFSCycleDetect(multinarrative.getRoute(route).getStart());
        if (detect.hasCycle()) {
            try {
                multinarrative.setStart(route, mOldStart.getId());
            } catch (GraphElementNotFoundException e) {
                throw new IllegalOperationException(e, ITEM_DOES_NOT_EXIST, e.getItem());
            }
            throw new IllegalOperationException(CYCLE_EXISTS);
        }

    }

    /**
     * deletes a node
     * 
     * @param id
     */
    public void deleteNode(String id) {
        multinarrative.removeNode(id);
    }

    /**
     * deletes a route
     * 
     * @param id
     */
    public void deleteRoute(String id) {
        multinarrative.removeRoute(id);
    }

    /**
     * Assigns the given property to the correct node
     * 
     * @param id
     * @param property
     * @param type
     * @param value
     * @throws IllegalOperationException
     */
    public void assignPropertyToNode(String id, String property, String type, String value)
            throws IllegalOperationException {

        Node node = multinarrative.getNode(id);
        if (node == null) {
            throw new IllegalOperationException(ITEM_DOES_NOT_EXIST,id);
        }
        if (value == null) {
            if (node.getProperties() != null) {
                node.getProperties().remove(property);
            }
        } else {
            node.createProperties();
            BaseBundle b = node.getProperties();
            try {
                switch (type) {
                case "String":
                    b.putString(property, value);
                    break;
                case "Integer":
                    b.putInt(property, Integer.parseInt(value));
                    break;
                case "Boolean":
                    b.putBoolean(property, Boolean.parseBoolean(value));
                    break;
                case "Byte":
                    b.putByte(property, Byte.parseByte(value));
                    break;
                case "Short":
                    b.putShort(property, Short.parseShort(value));
                    break;
                case "Long":
                    b.putLong(property, Long.parseLong(value));
                    break;
                case "Float":
                    b.putFloat(property, Float.parseFloat(value));
                    break;
                case "Double":
                    b.putDouble(property, Double.parseDouble(value));
                    break;
                default:
                    throw new IllegalOperationException(INVALID_TYPE);
                }
            } catch (NumberFormatException nfe) {
                throw new IllegalOperationException(nfe, CANNOT_FORMAT, value, type);
            }
        }

    }

    /**
     * Assigns the given property to the given route
     * 
     * @param route
     * @param property
     * @param type
     * @param value
     * @throws IllegalOperationException
     */
    public void assignPropertyToRoute(String id, String property, String type, String value)
            throws IllegalOperationException {

        Route route = multinarrative.getRoute(id);
        if (route == null) {
            throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, id);
        }
        if (value == null) {
            if (route.getProperties() != null) {
                route.getProperties().remove(property);
            }
        } else {
            route.createProperties();
            BaseBundle b = route.getProperties();
            try {
                switch (type) {
                case "String":
                    b.putString(property, value);
                    break;
                case "Integer":
                    b.putInt(property, Integer.parseInt(value));
                    break;
                case "Boolean":
                    b.putBoolean(property, Boolean.parseBoolean(value));
                    break;
                case "Byte":
                    b.putByte(property, Byte.parseByte(value));
                    break;
                case "Short":
                    b.putShort(property, Short.parseShort(value));
                    break;
                case "Long":
                    b.putLong(property, Long.parseLong(value));
                    break;
                case "Float":
                    b.putFloat(property, Float.parseFloat(value));
                    break;
                case "Double":
                    b.putDouble(property, Double.parseDouble(value));
                    break;
                default:
                    throw new IllegalOperationException(INVALID_TYPE);
                }
            } catch (NumberFormatException nfe) {
                throw new IllegalOperationException(nfe, CANNOT_FORMAT, value, type);
            }
        }

    }

    /**
     * TODO Add the property to the String ArrayList global property in
     * GUINarrative "System.Types"
     * 
     * @param property
     */
    public void setAsRouteType(String property) {

    }

    /**
     * TODO Remove the property from the String ArrayList global property in
     * GUINarrative "System.Types"
     * 
     * @param property
     */
    public void clearAsRouteType(String property) {

    }

    /**
     * Sets the correct colour for the given value of the given property TODO
     * make this use a better data structure
     * 
     * @param property
     * @param value
     * @param c
     */
    public void setColor(String property, String value, Color c) throws IllegalOperationException {
        if (colors.get(property) == null) {
            colors.put(property, new HashMap<String, Color>());
        }

    }

    /**
     * Gets the colour associated with this value of this property TODO use a
     * better data Structure
     * 
     * @param property
     * @param value
     * @return
     */
    public Color getColor(String property, String value) {
        if (colors.get(property) == null) {
            return Color.TRANSPARENT;
        }
        Color c = colors.get(property).get(value);
        return c == null ? Color.TRANSPARENT : c;
    }

    /**
     * TODO gets a list of all the non transparent colors of properties applying
     * to a node
     * 
     * @return
     * @throws GraphElementNotFoundException
     */
    public ArrayList<Color> getNodeColor(String node) throws GraphElementNotFoundException {
        ArrayList<Color> r = new ArrayList<Color>();
        multinarrative.getProperties(node);
        return r;
    }

    /**
     * TODO gets a list of all the non transparent colors of properties applying
     * to a route
     * 
     * @return
     */
    public ArrayList<Color> getRouteColor(String route) {
        ArrayList<Color> r = new ArrayList<Color>();
        return r;
    }

    /**
     * Gives whether the give id is a choice node
     * 
     * @param node
     *            node id
     * @return true if it's a choice node and false if it's a synch
     * @throws IllegalOperationException
     *             if node doesn't exist
     */
    public boolean isChoiceNode(String node) throws IllegalOperationException {
        try {
            return multinarrative.isChoiceNode(node);
        } catch (GraphElementNotFoundException e) {
            throw new IllegalOperationException(e, ITEM_DOES_NOT_EXIST, e.getItem());
        }
    }

    /**
     * Swaps the given node from a choice to a sync or vice versa
     * 
     * @param node
     *            node id
     * @return true if it's a choice node and false if it's a synch
     * @throws IllegalOperationException
     *             if node doesn't exist
     */
    public void switchChoiceOrSynch(String node) throws IllegalOperationException {

        try {
            multinarrative.swapSyncAndChoice(node);
        } catch (GraphElementNotFoundException e) {
            throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, e.getItem());
        }
    }

    /**
     * Removes the required property
     * 
     * @throws IllegalOperationException
     *             if can't remove property, message of exception is displayed
     *             to the user, using the Strings class for formatting.
     */
    public void removeProperty(String s) throws IllegalOperationException {
        if (!properties.containsKey(s)) {
            throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, s);
        }
        properties.remove(s);

    }

    /**
     * Gets the bundle of values for a property (used by undo)
     * 
     * @param s
     *            name
     * @return list of values
     * @throws IllegalOperationException
     *             if property doesn't exist
     */
    public BaseBundle getPropertyBundle(String s) throws IllegalOperationException {
        if (!properties.containsKey(s)) {
            throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, s);
        }
        return properties.get(s);
    }

    /**
     * Changes the name of the required property
     * 
     * @throws IllegalOperationException
     *             if can't rename property, message of exception is displayed
     *             to the user, using the Strings class for formatting.
     */
    public void renameProperty(String from, String to) throws IllegalOperationException {

        if (!properties.containsKey(from)) {
            throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, from);
        }
        if (properties.containsKey(to)) {
            throw new IllegalOperationException(ITEM_ALREADY_EXISTS, to);
        }

        BaseBundle oldprop = properties.get(from);
        properties.put(to, oldprop);

        properties.remove(from);
    }

    /**
     * Deletes a property from a node or route.
     * 
     * @param id
     * @param propertyAndValue
     * @throws IllegalOperationException
     */
    public void deleteProperty(String id, String property) throws IllegalOperationException {
        Node node = multinarrative.getNode(id);
        Route route = multinarrative.getRoute(id);
        if (property.equals("GUI.X") || property.equals("GUI.Y")) {
            throw new IllegalOperationException(SYSTEM_PROPERTY, property);
        }
        if (node != null) {
            BaseBundle b = node.getProperties();
            if (b != null) {
                b.remove(property);
            } else {
                throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, property);
            }
        } else if (route != null) {
            BaseBundle b = route.getProperties();
            if (b != null) {
                b.remove(property);
            } else {
                throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, property);
            }
        } else {
            throw new IllegalOperationException(ITEM_DOES_NOT_EXIST, id);
        }

    }

    public void loadNarrative(GUINarrative narrative) {
        nodeCounter = 1;
        narrativeCounter = 1;
        valueCounter = 1;
        colors = new HashMap<String, Map<String, Color>>();
        properties = new HashMap<String, BaseBundle>();
        multinarrative = narrative;
    }

    public GUINarrative getNarrative() {
        return multinarrative;
    }
    
    public void newNarrative() {
        multinarrative = new GUINarrative();
    }

}
