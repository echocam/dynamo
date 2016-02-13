package uk.ac.cam.echo2016.multinarrative.gui.operations;

import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.ADD_EMPTY_STRING;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.ALREADY_EXISTS;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.NODE_ALREADY_AT_POSITION;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.NODE_ALREADY_EXISTS;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.NODE_DOES_NOT_EXIST;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.NODE_PREFIX;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.PROPERTY_DOES_NOT_EXIST;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.PROPERTY_MISSING;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.PROPERTY_RENAME_EXISTS;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.ROUTE_ALREADY_EXISTS;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.ROUTE_PREFIX;
import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.CYCLE_EXISTS;

import java.io.IOException;
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
import uk.ac.cam.echo2016.multinarrative.gui.FXMLController;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphNode;
import uk.ac.cam.echo2016.multinarrative.io.SaveReader;
import uk.ac.cam.echo2016.multinarrative.io.SaveWriter;

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
public class GUIOperations {

    private GUINarrative multinarrative;

    private Map<String, BaseBundle> properties;
    private Map<String, Map<String, Color>> colors = new HashMap<String, Map<String, Color>>();
    private static int nodeCounter = 1;
    private static int narrativeCounter = 1;
    private static int valueCounter = 1;

    /**
     * Constructor. Creates a new GUIOperations.
     */
    public GUIOperations() {
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
    public void addProperty(String s) throws IllegalOperationException {
        class AddPropertyOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                if (s.equals("") || s == null) {
                    throw new IllegalOperationException(ADD_EMPTY_STRING);
                }
                if (properties.containsKey(s)) {
                    throw new IllegalOperationException(ALREADY_EXISTS);
                }
                properties.put(s, new BaseBundle());
            }

            @Override
            public void undo() {
                // TODO test this.
                properties.remove(s);
            }
        }

        Operation c = new AddPropertyOperation();

        Operation.storeAndExecute(c);
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
        class SetPropertyTypeOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                // TODO Auto-generated method stub

            }

            @Override
            public void undo() {
                // TODO Implement logical undo of the command and test it.
            }
        }

        Operation c = new SetPropertyTypeOperation();

        Operation.storeAndExecute(c);
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
        class AddValueOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                if (!properties.containsKey(property)) {
                    throw new IllegalOperationException(PROPERTY_DOES_NOT_EXIST);
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
                        throw new IllegalOperationException("Type " + type + " connot be resolved.");

                    }
                } catch (NumberFormatException e) {
                    throw new IllegalOperationException(
                            "Value " + value + " connot be " + "resolved to type " + type + ".");
                }

            }

            @Override
            public void undo() {
                // TODO Implement the UNDO!
            }
        }

        Operation c = new AddValueOperation();

        Operation.storeAndExecute(c);
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
        class AddSynchNodeOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                if (multinarrative.getNode(name) != null) {
                    throw new IllegalOperationException(NODE_ALREADY_EXISTS);
                }
                for (String nodename : multinarrative.getNodes().keySet()) {
                    if (multinarrative.getNode(nodename).getProperties().getDouble("GUI.X") == x
                            && multinarrative.getNode(nodename).getProperties().getDouble("GUI.Y") == y) {
                        throw new IllegalOperationException(NODE_ALREADY_AT_POSITION);
                    }
                }
                SynchronizationNode newNode = new SynchronizationNode(name);
                newNode.createProperties();
                newNode.getProperties().putDouble("GUI.X", x);
                newNode.getProperties().putDouble("GUI.Y", y);
                multinarrative.getNodes().put(name, newNode);
            }

            @Override
            public void undo() {
                // TODO Test this!
                multinarrative.getNodes().remove(name);
            }
        }

        Operation c = new AddSynchNodeOperation();

        Operation.storeAndExecute(c);
    }

    /**
     * Adds a choiceNode at the given position, with the given name
     */
    public void addChoiceNode(String name, double x, double y) throws IllegalOperationException {
        class AddChoiceNodeOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                if (multinarrative.getNode(name) != null) {
                    throw new IllegalOperationException(NODE_ALREADY_EXISTS);
                }
                for (String nodename : multinarrative.getNodes().keySet()) {
                    if (multinarrative.getNode(nodename).getProperties().getDouble("GUI.X") == x
                            && multinarrative.getNode(nodename).getProperties().getDouble("GUI.Y") == y) {
                        throw new IllegalOperationException(NODE_ALREADY_AT_POSITION);
                    }
                }
                ChoiceNode newNode = new ChoiceNode(name);
                newNode.createProperties();
                newNode.getProperties().putDouble("GUI.X", x);
                newNode.getProperties().putDouble("GUI.Y", y);
                multinarrative.getNodes().put(name, newNode);
            }

            @Override
            public void undo() {
                multinarrative.getNodes().remove(name);
                // TODO test it.
            }
        }

        Operation c = new AddChoiceNodeOperation();

        Operation.storeAndExecute(c);
    }

    /**
     * Repositions a node by the given offset
     */
    public void translateNode(String name, double x, double y) throws IllegalOperationException {
        class TranslateNodeOperation implements Operation {
            private double oldX;
            private double oldY;

            @Override
            public void execute() throws IllegalOperationException {
                Node theNode = multinarrative.getNode(name);
                if (theNode == null) {
                    throw new IllegalOperationException("Node does not exist.");
                }
                oldX = theNode.getProperties().getDouble("GUI.X");
                oldY = theNode.getProperties().getDouble("GUI.Y");
                double transx = oldX + x;
                double transy = oldY + y;
                for (String nodename : multinarrative.getNodes().keySet()) {
                    if (multinarrative.getNode(nodename).getProperties().getDouble("GUI.X") == transx
                            && multinarrative.getNode(nodename).getProperties().getDouble("GUI.Y") == transy) {
                        throw new IllegalOperationException("Node already exists at given position.");
                    }
                }
                theNode.getProperties().putDouble("GUI.X", transx);
                theNode.getProperties().putDouble("GUI.Y", transy);
            }

            @Override
            public void undo() {
                // TODO test it.
                Node theNode = multinarrative.getNode(name);

                theNode.getProperties().putDouble("GUI.X", oldX);
                theNode.getProperties().putDouble("GUI.Y", oldY);
            }
        }

        Operation c = new TranslateNodeOperation();

        Operation.storeAndExecute(c);
    }

    /**
     * Gets a name that's not already in the graph.
     * 
     * @return unique narrative name
     */
    public String getUniqueRouteName() {
        String newName = Strings.populateString(ROUTE_PREFIX, narrativeCounter);
        if (multinarrative.getNodes().containsKey(newName)) {
            narrativeCounter += 1;
            return newName;
        } else {
            while (multinarrative.getNodes().containsKey(newName)) {
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
        class AddRouteOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                try {
                    multinarrative.newRoute(name, start, end);
                } catch (NonUniqueIdException e) {
                    throw new IllegalOperationException(ROUTE_ALREADY_EXISTS);
                } catch (GraphElementNotFoundException e) {
                    throw new IllegalOperationException(NODE_DOES_NOT_EXIST);
                }
                DFSCycleDetect cycleDetect = new DFSCycleDetect(multinarrative.getNode(start));
                if (cycleDetect.hasCycle()) {
                    multinarrative.removeRoute(name);
                    throw new IllegalOperationException(CYCLE_EXISTS);
                }
            }

            @Override
            public void undo() {
                // TODO test it.
                multinarrative.removeRoute(name);
            }
        }

        Operation c = new AddRouteOperation();

        Operation.storeAndExecute(c);
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
        class RenameNodeOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                if (!from.equals(to) && multinarrative.isUniqueId(to)) {
                    multinarrative.renameNode(from, to);
                }
            }

            @Override
            public void undo() {
                // TODO test it.
                if (!to.equals(from))
                    multinarrative.renameNode(to, from);
            }
        }

        Operation c = new RenameNodeOperation();

        Operation.storeAndExecute(c);
    }

    /**
     * 
     * @param from
     * @param to
     * @throws IllegalOperationException
     */
    public void renameRoute(String from, String to) throws IllegalOperationException {
        class RenameRouteOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                if (!from.equals(to) && multinarrative.isUniqueId(to))
                    multinarrative.renameRoute(from, to);
            }

            @Override
            public void undo() {
                // TODO test it.
                if (!to.equals(from))
                    multinarrative.renameRoute(to, from);
            }
        }

        Operation c = new RenameRouteOperation();

        Operation.storeAndExecute(c);
    }

    /**
     * 
     * @param route
     * @param node
     */
    public void setEnd(String route, String node) throws IllegalOperationException {
        class SetEndOperation implements Operation {
            private Route mRoute = multinarrative.getRoute(route);
            private Node mOldEnd = mRoute.getEnd();

            @Override
            public void execute() throws IllegalOperationException {
                try {
                    multinarrative.setEnd(route, node);
                } catch (GraphElementNotFoundException e) {
                    throw new IllegalOperationException(e);
                }
                DFSCycleDetect detect = new DFSCycleDetect(multinarrative.getRoute(route).getStart());
                if (detect.hasCycle()) {
                    try {
                        multinarrative.setEnd(route, mOldEnd.getId());
                    } catch (GraphElementNotFoundException e) {
                        throw new IllegalOperationException(e);
                    }
                    throw new IllegalOperationException(CYCLE_EXISTS);
                }
            }

            @Override
            public void undo() {
                // TODO test it.
                mRoute.setEnd(mOldEnd);
            }
        }

        Operation c = new SetEndOperation();

        Operation.storeAndExecute(c);

    }

    /**
     * 
     * @param route
     * @param node
     */
    public void setStart(String route, String node) throws IllegalOperationException {
        class SetStartOperation implements Operation {
            private Route mRoute = multinarrative.getRoute(route);
            private Node mOldStart = mRoute.getStart();

            @Override
            public void execute() throws IllegalOperationException {
                try {
                    multinarrative.setStart(route, node);
                } catch (GraphElementNotFoundException e) {
                    throw new IllegalOperationException(e);
                }
                DFSCycleDetect detect = new DFSCycleDetect(multinarrative.getRoute(route).getStart());
                if (detect.hasCycle()) {
                    try {
                        multinarrative.setStart(route, mOldStart.getId());
                    } catch (GraphElementNotFoundException e) {
                        throw new IllegalOperationException(e);
                    }
                    throw new IllegalOperationException(CYCLE_EXISTS);
                }

            }

            @Override
            public void undo() {
                // TODO test it.
                mRoute.setStart(mOldStart);
            }
        }

        Operation c = new SetStartOperation();

        Operation.storeAndExecute(c);
    }

    /**
     * deletes a node
     * 
     * @param id
     */
    public void deleteNode(String id) {
        class DeleteNodeOperation implements Operation {
            private Node mNode = multinarrative.getNode(id);

            @Override
            public void execute() throws IllegalOperationException {
                multinarrative.removeNode(id);
            }

            @Override
            public void undo() {
                // TODO test it.
                multinarrative.addNode(mNode);
            }
        }

        Operation c = new DeleteNodeOperation();

        try {
            Operation.storeAndExecute(c);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e); // TODO: resolve this
        }
    };

    /**
     * deletes a route
     * 
     * @param id
     */
    public void deleteRoute(String id) {
        class DeletRouteOperation implements Operation {
            private Route mRoute = multinarrative.getRoute(id);

            @Override
            public void execute() throws IllegalOperationException {
                multinarrative.removeRoute(id);
            }

            @Override
            public void undo() {
                // TODO test it.
                multinarrative.addRoute(mRoute);
            }
        }

        Operation c = new DeletRouteOperation();

        try {
            Operation.storeAndExecute(c);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e); // TODO: resolve this
        }
    }

    /**
     * Saves the GUINarrative object to the supplied filename
     * 
     * @throws IOException
     */
    public void saveInstance(String fileName) throws IOException {
        SaveWriter.saveObject(fileName, multinarrative);
    }

    /**
     * Loads the GUINarrative object from the supplied filename
     */
    public GUINarrative loadInstance(String fileName) throws IOException {
        return SaveReader.loadGUINarrative(fileName);
    }

    /**
     * Uses the FXMLController provided to rebuild the graph from the filename
     * given.
     * 
     * @throws IOException
     * @throws GraphElementNotFoundException
     */
    public void buildGraph(String fileName, FXMLController controller)
            throws IOException, GraphElementNotFoundException {
        nodeCounter = 1;
        narrativeCounter = 1;
        valueCounter = 1;
        multinarrative = new GUINarrative();
        colors = new HashMap<String, Map<String, Color>>();
        properties = new HashMap<String, BaseBundle>();

        GUINarrative loaded = loadInstance(fileName);
        if (loaded == null) {
            throw new IOException();
        }

        Node node;
        for (String nodeName : loaded.getNodes().keySet()) {
            node = loaded.getNode(nodeName);
            if (node instanceof SynchronizationNode) {
                controller.addSynchNode(node.getId(), node.getProperties().getDouble("GUI.X"),
                        node.getProperties().getDouble("GUI.Y"));
            } else {
                controller.addChoiceNode(node.getId(), node.getProperties().getDouble("GUI.X"),
                        node.getProperties().getDouble("GUI.Y"));
            }
        }

        Route route;
        GraphNode start;
        GraphNode end;
        for (String routeName : loaded.getRoutes().keySet()) {
            route = loaded.getRoute(routeName);
            start = controller.getGraph().getNodes().get(route.getStart().getId());
            end = controller.getGraph().getNodes().get(route.getEnd().getId());

            if (start == null || end == null) {
                throw new GraphElementNotFoundException("The GraphNode with that name was not found");
            }
            controller.addRoute(route.getId(), start, end);
        }
    }

    /**
     * TODO add value, ensuring it is unique
     * 
     * @param id
     * @param value
     */
    public void addPropertyValue(String id, String value) throws IllegalOperationException {
        class AddPropertyTypeOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                // TODO Auto-generated method stub

            }

            @Override
            public void undo() {
                // TODO Implement logical undo of the command and test it.
            }
        }

        Operation c = new AddPropertyTypeOperation();

        Operation.storeAndExecute(c);
    }

    /**
     * TODO remove value
     * 
     * @param id
     * @param value
     */
    public void removePropertyValue(String id, String value) {
        class RemovePropertyTypeOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                // TODO Auto-generated method stub

            }

            @Override
            public void undo() {
                // TODO Implement logical undo of the command and test it.
            }
        }

        Operation c = new RemovePropertyTypeOperation();

        try {
            Operation.storeAndExecute(c);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e); // TODO: resolve this
        }
    }

    /**
     * TODO rename value, checking it is unique (unless value === newValue)
     * 
     * @param id
     * @param value
     * @param newValue
     * @throws IllegalOperationException
     */
    public void renamePropertyValue(String id, String value, String newValue) throws IllegalOperationException {
        class RenamePropertyTypeOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                // TODO Auto-generated method stub

            }

            @Override
            public void undo() {
                // TODO Implement logical undo of the command and test it.
            }
        }

        Operation c = new RenamePropertyTypeOperation();

        Operation.storeAndExecute(c);
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
     * TODO assign the given property to the correct node
     * 
     * @param node
     * @param property
     * @param type
     * @param value
     */
    public void assignPropertyToNode(String node, String property, String type, String value) {
        class AssignPropertyToNodeOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                // TODO Auto-generated method stub

            }

            @Override
            public void undo() {
                // TODO Implement logical undo of the command and test it.
            }
        }

        Operation c = new AssignPropertyToNodeOperation();

        try {
            Operation.storeAndExecute(c);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e); // TODO: resolve this
        }
    }

    /**
     * TODO Assign the given property to the given route
     * 
     * @param route
     * @param property
     * @param type
     * @param value
     */
    public void assignPropertyToRoute(String route, String property, String type, String value) {
        class AssignPropertyToRouteOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                // TODO Auto-generated method stub

            }

            @Override
            public void undo() {
                // TODO Implement logical undo of the command and test it.
            }
        }

        Operation c = new AssignPropertyToRouteOperation();

        try {
            Operation.storeAndExecute(c);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e); // TODO: resolve this
        }
    }

    /**
     * TODO Add the property to the String ArrayList global property in
     * GUINarrative "System.Types"
     * 
     * @param property
     */
    public void setAsRouteType(String property) {
        class SetAsRouteTypeOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                // TODO Auto-generated method stub

            }

            @Override
            public void undo() {
                // TODO Implement logical undo of the command and test it.
            }
        }

        Operation c = new SetAsRouteTypeOperation();

        try {
            Operation.storeAndExecute(c);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e); // TODO: resolve this
        }
    }

    /**
     * TODO Remove the property from the String ArrayList global property in
     * GUINarrative "System.Types"
     * 
     * @param property
     */
    public void clearAsRouteType(String property) {
        class ClearAsRouteTypeOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                // TODO Auto-generated method stub

            }

            @Override
            public void undo() {
                // TODO Implement logical undo of the command and test it.
            }
        }

        Operation c = new ClearAsRouteTypeOperation();

        try {
            Operation.storeAndExecute(c);
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e); // TODO: resolve this
        }
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

        class SetColorOperation implements Operation {
            private Color mOldColor = colors.get(property).get(value);

            @Override
            public void execute() throws IllegalOperationException {
                if (colors.get(property) == null) {
                    colors.put(property, new HashMap<String, Color>());
                }
                colors.get(property).put(value, c);
            }

            @Override
            public void undo() {
                // TODO test it.
                colors.get(property).put(value, mOldColor);
            }
        }

        Operation operation = new SetColorOperation();

        Operation.storeAndExecute(operation);
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
            throw new IllegalOperationException(NODE_DOES_NOT_EXIST, e);
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
        class SwitchChoiceOrSynch implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                try {
                    multinarrative.swapSyncAndChoice(node);
                } catch (GraphElementNotFoundException e) {
                    throw new IllegalOperationException(NODE_DOES_NOT_EXIST, e);
                }
            }

            @Override
            public void undo() {
                // TODO test it.
                try {
                    multinarrative.swapSyncAndChoice(node);
                } catch (GraphElementNotFoundException e) {
                    throw new RuntimeException(e); // This piece of code should
                                                   // never be reached
                }
            }
        }

        Operation c = new SwitchChoiceOrSynch();

        Operation.storeAndExecute(c);
    }

    /**
     * Removes the required property
     * 
     * @throws IllegalOperationException
     *             if can't remove property, message of exception is displayed
     *             to the user, using the Strings class for formatting.
     */
    public void removeProperty(String s) throws IllegalOperationException {
        class RemovePropertyOperation implements Operation {
            private BaseBundle mOldProperty = properties.get(s);

            @Override
            public void execute() throws IllegalOperationException {
                if (!properties.containsKey(s)) {
                    throw new IllegalOperationException("Property " + s + " does not exist.");
                }
                properties.remove(s);
            }

            @Override
            public void undo() {
                // TODO test it
                properties.put(s, mOldProperty);
            }
        }

        Operation c = new RemovePropertyOperation();

        Operation.storeAndExecute(c);
    }

    /**
     * Changes the name of the required property
     * 
     * @throws IllegalOperationException
     *             if can't rename property, message of exception is displayed
     *             to the user, using the Strings class for formatting.
     */
    public void renameProperty(String from, String to) throws IllegalOperationException {
        class RenamePropertyOperation implements Operation {
            @Override
            public void execute() throws IllegalOperationException {
                if (!properties.containsKey(from)) {
                    throw new IllegalOperationException(PROPERTY_MISSING);
                }
                if (properties.containsKey(to)) {
                    throw new IllegalOperationException(PROPERTY_RENAME_EXISTS);
                }

                BaseBundle oldprop = properties.get(from);
                properties.put(to, oldprop);

                properties.remove(from);
            }

            @Override
            public void undo() {
                // TODO test it.
                BaseBundle oldprop = properties.get(to);
                properties.put(from, oldprop);

                properties.remove(to);
            }
        }

        Operation c = new RenamePropertyOperation();

        Operation.storeAndExecute(c);
    }

    public void undoLastOperation() {
        Operation.undoLastOperation();
    }

    public void redoLastUndo() {
        Operation.redoLastUndo();
    }
}
