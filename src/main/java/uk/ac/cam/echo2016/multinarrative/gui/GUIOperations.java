package uk.ac.cam.echo2016.multinarrative.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import android.os.BaseBundle;
import uk.ac.cam.echo2016.multinarrative.GUINarrative;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;
import uk.ac.cam.echo2016.multinarrative.Node;
import uk.ac.cam.echo2016.multinarrative.NonUniqueIdException;
import uk.ac.cam.echo2016.multinarrative.Route;
import uk.ac.cam.echo2016.multinarrative.gui.IllegalOperationException;

import static uk.ac.cam.echo2016.multinarrative.gui.Strings.*;

/**
 * @author jr650
 * @author eyx20
 */
public class GUIOperations {

    private GUINarrative multinarrative;

    private HashMap<String, BaseBundle> properties;
    private HashMap<String, Node> nodeslist;// TODO remove this as it isn't used
    private HashMap<String, Coordinate> nodes;
    private static final String SYNCH_NODE_PREFIX = "SynchNode-";
    private static int nodeCounter = 1;
    private static int narrativeCounter = 1;

    /**
     * Constructor. Creates a new GUIOperations.
     */
    public GUIOperations() {
        multinarrative = new GUINarrative();
        properties = new HashMap<String, BaseBundle>();
        nodes = new HashMap<String, Coordinate>();
        nodeslist = new HashMap<String, Node>();
    }

    /**
     * Adds the required property
     * 
     * @throws IllegalOperationException
     *             if can't add property, message of exception is displayed to
     *             the user, using the Strings class for formatting.
     */
    public void addProperty(String s) throws IllegalOperationException {
        if (s.equals("") || s == null) {
            throw new IllegalOperationException(ADD_EMPTY_STRING);
        }
        if (properties.containsKey(s)) {
            throw new IllegalOperationException(ALREADY_EXISTS);
        }
        properties.put(s, new BaseBundle());

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
            throw new IllegalOperationException(PROPERTY_DOES_NOT_EXIST);
        }
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
            properties.get(property).putFloat(value, Integer.parseInt(value));
            break;
        case "Double":
            properties.get(property).putDouble(value, Double.parseDouble(value));
            break;
        default:
            throw new IllegalOperationException("Type " + type + " connot be resolved.");

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
            throw new IllegalOperationException("Property " + s + " does not exist.");
        }
        properties.remove(s);
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
            throw new IllegalOperationException(PROPERTY_MISSING);
        }
        if (properties.containsKey(to)) {
            throw new IllegalOperationException("Property cannot be renamed to " + to + ": " + to + " already exists.");
        }

        BaseBundle oldprop = properties.get(from);
        properties.put(to, oldprop);

        properties.remove(from);
    }

    /**
     * Gets a name that's not already in the graph.
     * 
     * @return new node name
     */

    public String getUniqueNodeName() {
        String newName = Strings.populateString(NODE_PREFIX, nodeCounter);
        if (!nodes.containsKey(newName)) {
            nodeCounter += 1;
            return newName;
        } else {
            while (nodes.containsKey(newName)) {
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
    // TODO: Check if node is out of bounds/illegal coordinate
    public void addSynchNode(String name, double x, double y) throws IllegalOperationException
    {
        Coordinate coor = new Coordinate(x, y);
        if (nodes.containsKey(name)) {
            throw new IllegalOperationException(NODE_ALREADY_EXISTS);
        }
        for (String nodename : nodes.keySet()) {
            if (coor.equals(nodes.get(nodename))) {
                throw new IllegalOperationException(NODE_ALREADY_AT_POSITION);
            }
        }
        nodes.put(name, coor);
        try {
            multinarrative.newSynchronizationNode(name);
        }catch (NonUniqueIdException e){
            throw new IllegalOperationException(NODE_ALREADY_EXISTS);
        }
    }

    /**
     * Repositions a node by the given offset
     */
    // TODO: Check if node is out of bounds/illegal coordinate
    public void translateNode(String name, double x, double y) throws IllegalOperationException {
        if (!nodes.containsKey(name)) {
            throw new IllegalOperationException("Node does not exist.");
        }
        Coordinate coord = nodes.get(name);
        double transx = coord.getX() + x;
        double transy = coord.getY() + y;
        Coordinate newcoord = new Coordinate(transx, transy);
        for (String nodename : nodes.keySet()) {
            if (newcoord.equals(nodes.get(nodename))) {
                throw new IllegalOperationException("Node already exists at given position.");
            }
        }
        nodes.put(name, newcoord);

    }

    /**
     * Gets a name that's not already in the graph.
     * 
     * @return unique narrative name
     */
    public String getUniqueNarrativeName() {
        String newName = Strings.populateString(NARRATIVE_PREFIX, narrativeCounter);
        if (!nodes.containsKey(newName)) {
            narrativeCounter += 1;
            return newName;
        } else {
            while (nodes.containsKey(newName)) {
                narrativeCounter += 1;
                newName = Strings.populateString(NARRATIVE_PREFIX, narrativeCounter);
            }
            narrativeCounter += 1;
            return newName;
        }
    }

    /**
     * Adds a narrative, throwing exception if it fails.
     * 
     * @param name
     *            - unique id of the narrative
     * @param start
     *            - starting node. If node does not exist creates a new node.
     * @param end
     *            - ending node. If node does not exist creates a new node. Do
     *            cycle detection here!!
     * @throws GraphElementNotFoundException
     * @throws NonUniqueIdException
     */
    public void addNarrative(String name, String start, String end) throws IllegalOperationException
    {
        // TODO Figure out how to get charID and REMOOOOOOOVE DIS
        // String charID = "Filler"; //TODO replace with "primary properties"
        //i.e. putStringArrayList("Primaries", propertyId); e.g. "Mike"
        //Note: may change name from "Primaries"
        try {
            multinarrative.newRoute(name, start, end);
        } catch (NonUniqueIdException e) {
            throw new IllegalOperationException(NARRATIVE_ALREADY_EXISTS);
        }catch(GraphElementNotFoundException e){
            throw new IllegalOperationException(NODE_DOES_NOT_EXIST);
        }
        DFSCycleDetect cycleDetect = new DFSCycleDetect(multinarrative.getNode(start));
        if (cycleDetect.hasCycle()) {
            multinarrative.removeRoute(name);
            throw new IllegalOperationException("Cannot add route: Graph will contain a cycle");
        }
        
    }

    /**
     * 
     * @param node Node id
     * @return List of properties in the form "name=value"
     */
    public List<String> getNodeProperties(String node) {
         BaseBundle props =  multinarrative.getNode(node).getProperties();
         ArrayList<String> r = new ArrayList<String>();
         for (String name : props.keySet()) {
             r.add(name + "=" + props.get(name).toString());
         }
         return r;
    }
    
    /**
     * 
     * @param node Route id
     * @return List of properties in the form "name=value"
     */
    public List<String> getRouteProperties(String route) {
        BaseBundle props =  multinarrative.getRoute(route).getProperties();
        ArrayList<String> r = new ArrayList<String>();
        for (String name : props.keySet()) {
            r.add(name + "=" + props.get(name).toString());
        }
        return r;
    }
    
    /**
     * TODO
     * @param from
     * @param to
     * @throws IllegalOperationException
     */
    public void renameNode(String from, String to) throws IllegalOperationException{
        Node n = multinarrative.getNode(from);
        //if (multinarrative.)
    }
    
    /**
     * TODO 
     * @param id
     */
    public void deleteNode(String id){
    	
    }
    
    /**
     * TODO 
     * @param id
     */
    public void deleteNarrative(String id){
    	
    }
    
    /**
     * TODO 
     * @param id
     */
    public void deleteRoute(String id){
    	
    }
 }
