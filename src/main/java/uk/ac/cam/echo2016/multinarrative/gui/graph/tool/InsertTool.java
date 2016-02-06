package uk.ac.cam.echo2016.multinarrative.gui.graph.tool;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import uk.ac.cam.echo2016.multinarrative.gui.graph.Graph;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphEdge;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphNode;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphTool;

public class InsertTool implements GraphTool {

    private boolean dragging;

    private GraphEdge press;
    
    private GraphNode start;

    private Graph graph;

    public InsertTool(Graph graph) {
	this.graph = graph;
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
	if (!dragging) {
	    try {
		String name = graph.getOperations().getUniqueNodeName(); 
		Button b = FXMLLoader.load(getClass().getResource("graph_button.fxml"));
		b.setText(name);
		System.out.println(b.getStylesheets());
		GraphNode newNode = new GraphNode(b,name);
		graph.addNode(newNode);
		newNode.setPosition(event.getX() - b.getWidth() / 2, event.getY() - b.getHeight() / 2);
	    } catch (IOException ioe) {
		// Error with fxml files
		throw new RuntimeException("FXML files not configured correctly", ioe);
	    }
	}

	dragging = false;
	start = null;
	press = null;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
    }

    @Override
    public void mousePressedOnNode(MouseEvent event, GraphNode node) {
	start = node;
    }

    @Override
    public void mousePressedOnEdge(MouseEvent event, GraphEdge edge) {
	press = edge;
    }

    @Override
    public void mouseReleasedOnNode(MouseEvent event, GraphNode node) {
	if (start != null) {
	    String name = graph.getOperations().getUniqueNarrativeName();
	    GraphEdge edge = new GraphEdge(name, start, node);
	}
	mouseReleased(event);
    }

    @Override
    public void mouseReleasedOnEdge(MouseEvent event, GraphEdge edge) {
	if(press == edge){
	    //TODO add choice point on the edge
	}
	mouseReleased(event);
    }

}
