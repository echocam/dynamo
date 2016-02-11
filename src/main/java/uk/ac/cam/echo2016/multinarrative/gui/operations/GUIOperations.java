package uk.ac.cam.echo2016.multinarrative.gui.operations;

import static uk.ac.cam.echo2016.multinarrative.gui.operations.Strings.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.BaseBundle;
import uk.ac.cam.echo2016.multinarrative.ChoiceNode;
import uk.ac.cam.echo2016.multinarrative.GUINarrative;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;
import uk.ac.cam.echo2016.multinarrative.Node;
import uk.ac.cam.echo2016.multinarrative.NonUniqueIdException;
import uk.ac.cam.echo2016.multinarrative.SynchronizationNode;
import uk.ac.cam.echo2016.multinarrative.io.SaveReader;
import uk.ac.cam.echo2016.multinarrative.io.SaveWriter;

/**
 * @author jr650
 * @author eyx20
 * @author rjm232
 */
public class GUIOperations {

	private GUINarrative multinarrative;

	private HashMap<String, BaseBundle> properties;
	//private HashMap<String, Coordinate> nodes;
	private static int nodeCounter = 1;
	private static int narrativeCounter = 1;

	/**
	 * Constructor. Creates a new GUIOperations.
	 */
	public GUIOperations() {
		multinarrative = new GUINarrative();
		properties = new HashMap<String, BaseBundle>();
		//nodes = new HashMap<String, Coordinate>();
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
			throw new IllegalOperationException(PROPERTY_RENAME_EXISTS);
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
	// TODO: Check if node is out of bounds/illegal coordinate
	public void addSynchNode(String name, double x, double y) throws IllegalOperationException {
		if (multinarrative.getNode(name) != null) {
			throw new IllegalOperationException(NODE_ALREADY_EXISTS);
		}
		for (String nodename : multinarrative.getNodes().keySet()) {
			if (multinarrative.getNode(nodename).getProperties().getDouble("GUI.X") == x &&
					multinarrative.getNode(nodename).getProperties().getDouble("GUI.Y") == y) {
				throw new IllegalOperationException(NODE_ALREADY_AT_POSITION);
			}
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
			throw new IllegalOperationException(NODE_ALREADY_EXISTS);
		}
		for (String nodename : multinarrative.getNodes().keySet()) {
			if (multinarrative.getNode(nodename).getProperties().getDouble("GUI.X") == x &&
					multinarrative.getNode(nodename).getProperties().getDouble("GUI.Y") == y) {
				throw new IllegalOperationException(NODE_ALREADY_AT_POSITION);
			}
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
	// TODO: Check if node is out of bounds/illegal coordinate
	public void translateNode(String name, double x, double y) throws IllegalOperationException {
		Node theNode = multinarrative.getNode(name);
		if (theNode == null) {
			throw new IllegalOperationException("Node does not exist.");
		}
		double oldX = theNode.getProperties().getDouble("GUI.X");
		double oldY = theNode.getProperties().getDouble("GUI.Y");
		double transx = oldX + x;
		double transy = oldY + y;
		for (String nodename : multinarrative.getNodes().keySet()) {
			if (multinarrative.getNode(nodename).getProperties().getDouble("GUI.X") == transx &&
					multinarrative.getNode(nodename).getProperties().getDouble("GUI.Y") == transy) {
				throw new IllegalOperationException("Node already exists at given position.");
			}
		}
		theNode.getProperties().putDouble("GUI.X", transx);
		theNode.getProperties().putDouble("GUI.Y", transy);
	}

	/**
	 * Gets a name that's not already in the graph.
	 * 
	 * @return unique narrative name
	 */
	public String getUniqueNarrativeName() {
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
	public void addNarrative(String name, String start, String end) throws IllegalOperationException {
		// TODO Figure out how to get charID and REMOOOOOOOVE DIS
		// String charID = "Filler"; //TODO replace with "primary properties"
		// i.e. putStringArrayList("Primaries", propertyId); e.g. "Mike"
		// Note: may change name from "Primaries"
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
			throw new IllegalOperationException("Cannot add route: Graph will contain a cycle");
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
		// TODO Figure out how to get charID and REMOOOOOOOVE DIS
		// String charID = "Filler"; //TODO replace with "primary properties"
		// The primaries ArrayList should be a global property!
		// i.e. putStringArrayList("Primaries", propertyId); e.g. "Mike"
		// Note: may change name from "Primaries"
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
			throw new IllegalOperationException("Cannot add route: Graph will contain a cycle");
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
		if (!from.equals(to))
			multinarrative.renameNode(from, to);
	}

	/**
	 * 
	 * @param from
	 * @param to
	 * @throws IllegalOperationException
	 */
	public void renameRoute(String from, String to) throws IllegalOperationException {
		if (!from.equals(to))
			multinarrative.renameRoute(from, to);
	}

	/**
	 * TODO
	 * 
	 * @param route
	 * @param node
	 */
	public void setEnd(String route, String node) {

	}

	/**
	 * TODO
	 * 
	 * @param route
	 * @param node
	 */
	public void setStart(String route, String node) {

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
	 * Saves the GUINarrative object to the supplied filename
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
}
