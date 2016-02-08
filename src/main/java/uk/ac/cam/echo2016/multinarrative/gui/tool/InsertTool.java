package uk.ac.cam.echo2016.multinarrative.gui.tool;

import java.io.IOException;

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
	    try {
		String name = graph.getOperations().getUniqueNodeName();
		graph.getOperations().addSynchNode(name, event.getX(), event.getSceneY());
		Button b = FXMLLoader.load(getClass().getResource("graph_button.fxml"));
		b.setText(name);
		GraphNode newNode = new GraphNode(b,name);
		graph.addNode(newNode);
		newNode.setPosition(event.getX(), event.getY());
	    } catch (IOException ioe) {
		// Error with fxml files
		throw new RuntimeException("FXML files not configured correctly", ioe);
	    } catch (IllegalOperationException e) {
		graph.getController().setInfo(e.getMessage());
	    } catch (NonUniqueIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	start = null;
	pressed = null;
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
	pressed=edge;
    }

    @Override
    public void mouseReleasedOnNode(MouseEvent event, GraphNode node) {
	if (start != null) {
	    try {
		String name = graph.getOperations().getUniqueNarrativeName();
		graph.getOperations().addNarrative(name, start.getName(), node.getName());
		CubicCurve c = new CubicCurve();
		c.setStroke(Color.WHITE);
		c.setStrokeWidth(2);
		c.setStrokeLineCap(StrokeLineCap.ROUND);
		c.setFill(Color.TRANSPARENT);
		Circle ci = new Circle(4,Color.rgb(51, 51, 51));
		GraphEdge edge = new GraphEdge(name, start, node, c, ci);
		graph.addEdge(edge);
		graph.updateEdge(edge);
	    } catch (IllegalOperationException e) {
		graph.getController().setInfo(e.getMessage());
	    } catch (NonUniqueIdException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (GraphElementNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
    }

    @Override
    public void mouseReleasedOnEdge(MouseEvent event, GraphEdge edge) {
    }

    @Override
    public void dragStart(MouseEvent event) {
	if(event.getSource() instanceof Node){
	    ((Node)event.getSource()).startFullDrag();
	}
    }

}
