package uk.ac.cam.echo2016.multinarrative.gui.graph;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
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
    
    public Graph(ScrollPane pane, Pane p, GUIOperations operations){
	this.pane = p;
	this.operations=operations;
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
	Node n = edge.getNode();
	pane.getChildren().add(n);
	edge.update(input.getScale());
	edges.add(edge);
	input.registerHandlerFor(n);
    }
    
    public void removeEdge(GraphEdge edge){
	Node n = edge.getNode();
	pane.getChildren().remove(n);
	edges.remove(edge);
    }
    
    public InputMonitor getInput(){
	return input;
    }

    /**
     * @return the operations
     */
    public GUIOperations getOperations() {
	return operations;
    }

}
