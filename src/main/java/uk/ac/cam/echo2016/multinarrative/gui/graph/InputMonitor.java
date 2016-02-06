package uk.ac.cam.echo2016.multinarrative.gui.graph;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

public class InputMonitor {

    private double currentScale = 1;
    private double scale = 1;

    private EventHandler<MouseEvent> dragStartHandler;
    private EventHandler<MouseDragEvent> dragReleasedHandler;
    
    private EventHandler<MouseEvent> mousePressedEventHandler;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mouseReleasedEventHandler;
    private EventHandler<ScrollEvent> scrollEventHandler;

    public InputMonitor(final Graph graph, ScrollPane pane) {

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

	scrollEventHandler = event -> {
	    if (event.getDeltaY() > 0) {
		if (scale > 0.2)
		    scale -= 0.1;
	    } else {
		scale += 0.1;
	    }
	    if (update())
		graph.update(currentScale);
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

	Duration duration = Duration.millis(1000 / 25);
	KeyFrame keyFrame = new KeyFrame(duration, event -> {
	    if (update())
		graph.update(currentScale);
	});

	Timeline timeline = new Timeline(keyFrame);
	timeline.cycleCountProperty().set(Animation.INDEFINITE);
	timeline.play();

	pane.setOnScroll(scrollEventHandler);
    }

    private boolean update() {
	if (currentScale != scale) {
	    scale += (currentScale - scale) * 0.06;
	    if (Math.abs((currentScale - scale)) < 0.01) {
		currentScale = scale;
	    }
	    return true;
	}
	return false;
    }

    public void registerHandlerFor(Node node) {
	node.setOnDragDetected(dragStartHandler);
	node.setOnMouseDragReleased(dragReleasedHandler);
	
	node.setOnScroll(scrollEventHandler);
	node.setOnMouseDragged(mouseDraggedEventHandler);
	node.setOnMousePressed(mousePressedEventHandler);
	node.setOnMouseReleased(mouseReleasedEventHandler);

    }

    public double getScale() {
	return scale;
    }

    public void setScale(double scale) {
	this.scale = scale;
    }
}
