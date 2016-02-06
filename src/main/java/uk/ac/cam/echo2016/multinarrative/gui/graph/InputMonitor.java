package uk.ac.cam.echo2016.multinarrative.gui.graph;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

public class InputMonitor {

    private double currentScale;
    private double scale;

    private EventHandler<MouseEvent> mousePressedEventHandler;
    private EventHandler<MouseEvent> mouseDraggedEventHandler;
    private EventHandler<MouseEvent> mouseReleasedEventHandler;
    private EventHandler<ScrollEvent> scrollEventHandler;

    public InputMonitor(final Graph graph, ScrollPane pane) {

	mousePressedEventHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		
		Object source = event.getSource();
		
		if (source instanceof Node) {
		    Node node = (Node) source;
		    Object graphElement = node.getUserData();
		    
		    if (graphElement instanceof GraphNode) {
			graph.getTool().mousePressedOnNode(event, (GraphNode) graphElement);
		    } else if (graphElement instanceof GraphEdge) {
			graph.getTool().mousePressedOnEdge(event, (GraphEdge) graphElement);
		    } else
			graph.getTool().mousePressed(event);
		} else {
		    graph.getTool().mousePressed(event);
		}
		
		event.consume();
	    }
	};

	mouseDraggedEventHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {

		graph.getTool().mouseDragged(event);
		event.consume();
	    }
	};

	mouseReleasedEventHandler = new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		Object source = event.getSource();
		
		if (source instanceof Node) {
		    Node node = (Node) source;
		    Object graphElement = node.getUserData();
		    
		    if (graphElement instanceof GraphNode) {
			graph.getTool().mouseReleasedOnNode(event, (GraphNode) graphElement);
		    } else if (graphElement instanceof GraphEdge) {
			graph.getTool().mouseReleasedOnEdge(event, (GraphEdge) graphElement);
		    } else
			graph.getTool().mouseReleased(event);
		} else {
		    graph.getTool().mouseReleased(event);
		}
		
		event.consume();
	    }
	};

	scrollEventHandler = new EventHandler<ScrollEvent>() {

	    @Override
	    public void handle(ScrollEvent event) {
		if (event.getDeltaY() > 0) {
		    if (scale > 0.2)
			scale -= 0.1;
		} else {
		    scale += 0.1;
		}
		if(update())
		    graph.update(currentScale);
	    }
	};
	
	Duration duration = Duration.millis(1000 / 25);
        KeyFrame keyFrame = new KeyFrame(duration, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
        	if(update())
		    graph.update(currentScale);
            }
        });

        Timeline timeline = new Timeline(keyFrame);
        timeline.cycleCountProperty().set(Animation.INDEFINITE);
        timeline.play();

	pane.setOnScroll(scrollEventHandler);
    }
    
    private boolean update(){
	if(currentScale!=scale){
	    scale += (currentScale-scale)*0.06;
	    if(Math.abs((currentScale-scale))<0.01){
		currentScale=scale;
	    }
	    return true;
	}
	return false;
    }

    public void registerHandlerFor(Node node) {
	node.setOnScroll(scrollEventHandler);
	node.setOnMouseDragged(mouseDraggedEventHandler);
	node.setOnMousePressed(mousePressedEventHandler);
	node.setOnMouseReleased(mouseReleasedEventHandler);
    }
    
    public double getScale(){
	return scale;
    }
    
    public void setScale(double scale){
	this.scale =scale;
    }
}
