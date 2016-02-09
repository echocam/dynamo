package uk.ac.cam.echo2016.multinarrative.gui.tool;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeLineCap;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;
import uk.ac.cam.echo2016.multinarrative.NonUniqueIdException;
import uk.ac.cam.echo2016.multinarrative.gui.IllegalOperationException;
import uk.ac.cam.echo2016.multinarrative.gui.graph.Graph;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphEdge;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphNode;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphTool;

public class InsertTool implements GraphTool {

    private GraphNode start;
    private GraphEdge pressed;

    boolean dragging = false;

    private double mouseX;
    private double mouseY;

    private Graph graph;

    public InsertTool(Graph graph) {
	this.graph = graph;
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
	if (start == null && pressed == null) {
	    addSynchNode(event.getX(), event.getY());
	}
	start = null;
	pressed = null;
	mouseX = Double.NaN;
	mouseY = Double.NaN;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
	double movementX = mouseX == mouseX ? event.getSceneX() - mouseX : 0.0;
	double movementY = mouseY == mouseY ? event.getSceneY() - mouseY : 0.0;
	if (pressed != null) {
	    pressed.translate(movementX, movementY);
	    graph.updateEdge(pressed);
	}

	mouseX = event.getSceneX();
	mouseY = event.getSceneY();
    }

    @Override
    public void mousePressedOnNode(MouseEvent event, GraphNode node) {
	start = node;
    }

    @Override
    public void mousePressedOnEdge(MouseEvent event, GraphEdge edge) {
	pressed = edge;
    }

    @Override
    public void mouseReleasedOnNode(MouseEvent event, GraphNode node) {
	if (start != null) {
	    addRoute(start, node);
	}
    }

    @Override
    public void mouseReleasedOnEdge(MouseEvent event, GraphEdge edge) {
    }

    @Override
    public void dragStart(MouseEvent event) {
	dragging = true;
	if (event.getSource() instanceof Node) {
	    ((Node) event.getSource()).startFullDrag();
	}
    }

    public void addSynchNode(Double x, Double y) {
	try {
	    String name = graph.getOperations().getUniqueNodeName();
	    graph.getOperations().addSynchNode(name, x, y);
	    Button b = FXMLLoader.load(getClass().getResource("graph_button.fxml"));
	    b.setText(name);
	    GraphNode newNode = new GraphNode(b, b.textProperty());
	    graph.addNode(newNode);
	    graph.getController().addNode(name);
	    newNode.setPosition(x, y);
	} catch (IOException ioe) {
	    // Error with fxml files
	    throw new RuntimeException("FXML files not configured correctly", ioe);
	} catch (IllegalOperationException e) {
	    graph.getController().setInfo(e.getMessage());
	} catch (NonUniqueIdException e) {
	    // TODO Replace with Illegal Operation Exception with sensible
	    // message
	}
    }

    public void addRoute(GraphNode from, GraphNode to) {
	try {
	    String name = graph.getOperations().getUniqueNarrativeName();
	    graph.getOperations().addNarrative(name, from.getName(), to.getName());
	    CubicCurve c = new CubicCurve();
	    c.setStroke(Color.WHITE);
	    c.setStrokeWidth(2);
	    c.setStrokeLineCap(StrokeLineCap.ROUND);
	    c.setFill(Color.TRANSPARENT);
	    Circle ci = new Circle(4, Color.rgb(51, 51, 51));
	    GraphEdge edge = new GraphEdge(new SimpleStringProperty(name), from, to, c, ci);
	    graph.addEdge(edge);
	    graph.getController().addRoute(name);
	    graph.updateEdge(edge);
	} catch (IllegalOperationException e) {
	    graph.getController().setInfo(e.getMessage());
	} catch (NonUniqueIdException e) {
	    // TODO Replace with Illegal Operation Exception with sensible
	    // message
	} catch (GraphElementNotFoundException e) {
	    // TODO Replace with Illegal Operation Exception with sensible
	    // message
	}
    }

}
