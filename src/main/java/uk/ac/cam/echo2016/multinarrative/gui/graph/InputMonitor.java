package uk.ac.cam.echo2016.multinarrative.gui.graph;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

public class InputMonitor {

    private EventHandler<MouseEvent> dragStartHandler;
    private EventHandler<MouseDragEvent> dragReleasedHandler;

    private EventHandler<MouseEvent> mousePressedEventHandler;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mouseReleasedEventHandler;

    public InputMonitor(final Graph graph) {

	mousePressedEventHandler = event -> {
	    Object source = event.getSource();

	    if (source instanceof Node) {
		Node node = (Node) source;
		Object graphElement = node.getUserData();

		if (graphElement instanceof GraphNode) {
		    graph.getTool().mousePressedOnNode(event, (GraphNode) graphElement);
		} else if (graphElement instanceof GraphEdge) {
		    graph.getTool().mousePressedOnEdge(event, (GraphEdge) graphElement);
		}
	    }

	    event.consume();
	};

	mouseDraggedEventHandler = event -> {

	    graph.getTool().mouseDragged(event);
	    event.consume();
	};

	mouseReleasedEventHandler = event -> {
	    graph.getTool().mouseReleased(event);
	    event.consume();
	};

	dragStartHandler = event -> {
	    graph.getTool().dragStart(event);
	    event.consume();
	};

	dragReleasedHandler = event -> {
	    Object source = event.getSource();

	    if (source instanceof Node) {
		Node node = (Node) source;
		Object graphElement = node.getUserData();

		if (graphElement instanceof GraphNode) {
		    graph.getTool().mouseReleasedOnNode(event, (GraphNode) graphElement);
		} else if (graphElement instanceof GraphEdge) {
		    graph.getTool().mouseReleasedOnEdge(event, (GraphEdge) graphElement);
		}
	    }

	    event.consume();
	};
    }

    public void registerHandlerFor(Node node) {
	node.setOnDragDetected(dragStartHandler);
	node.setOnMouseDragReleased(dragReleasedHandler);

	node.setOnMouseDragged(mouseDraggedEventHandler);
	node.setOnMousePressed(mousePressedEventHandler);
	node.setOnMouseReleased(mouseReleasedEventHandler);

    }
}