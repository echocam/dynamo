package uk.ac.cam.echo2016.multinarrative.gui.graph;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import uk.ac.cam.echo2016.multinarrative.gui.FXMLController;
import uk.ac.cam.echo2016.multinarrative.gui.GUIOperations;

/**
 * 
 * @author jr650
 *
 */
public class Graph {
   
    private Pane pane;
    private GUIOperations operations;
    
    private InputMonitor input;
    private GraphTool tool;
    
    private Set<GraphNode> nodes = new HashSet<>();
    private Set<GraphEdge> edges = new HashSet<>();
    
    private FXMLController controller;
    
    public Graph(ScrollPane pane, Pane p, GUIOperations operations, FXMLController c){
	this.pane = p;
	this.operations=operations;
	controller = c;
	input = new InputMonitor(this, pane);
	input.registerHandlerFor(p);
    }
    
    public void setTool(GraphTool tool){
	this.tool=tool;
    }
    
    public GraphTool getTool(){
	return tool;
    }
    
    public Set<GraphNode> getNodes(){
	return nodes;
    }

    public Set<GraphEdge> getEdges(){
	return edges;
    }
    
    public void update(double scale){
	for(GraphEdge edge:edges){
	    updateEdge(edge);
	}
    }
    
    public void updateEdge(GraphEdge edge){
	System.out.println("Update edge: "+edge.getName());
	edge.update(input.getScale());
    }
    
    public void updateNode(GraphNode node){
	for(GraphEdge edge: edges){
	    if(edge.getFrom()==node || edge.getTo()==node){
		updateEdge(edge);
	    }
	}
    }
    
    public void addNode(GraphNode node){
	Node contents = node.getContents();
	pane.getChildren().add(contents);
	nodes.add(node);
	input.registerHandlerFor(contents);
    }
    
    public void removeNode(GraphNode node){
	Node contents = node.getContents();
	pane.getChildren().remove(contents);
	nodes.remove(node);
	for(GraphEdge edge: edges){
	    if(edge.getFrom()==node || edge.getTo()==node){
		removeEdge(edge);
	    }
	}
    }
    
    public void addEdge(GraphEdge edge){
	CubicCurve n = edge.getNode();
	Circle c = edge.getControl();
	pane.getChildren().add(0,n);
	int i = 1;
	while(pane.getChildren().get(i) instanceof CubicCurve){
	    i++;
	}
	pane.getChildren().add(i, c);
	edge.update(input.getScale());
	edges.add(edge);
	input.registerHandlerFor(n);
	input.registerHandlerFor(c);
    }
    
    public void removeEdge(GraphEdge edge){
	Node n = edge.getNode();
	pane.getChildren().remove(n);
	edges.remove(edge);
    }
    
    public InputMonitor getInput(){
	return input;
    }

    public FXMLController getController() {
	return controller;
    }
    
    public GUIOperations getOperations() {
	return operations;
    }
    
    public Pane getPane(){
	return pane;
    }

}
