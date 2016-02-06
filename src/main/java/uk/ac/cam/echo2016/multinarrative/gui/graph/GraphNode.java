package uk.ac.cam.echo2016.multinarrative.gui.graph;

import javafx.scene.Node;


/**
 * 
 * @author jr650
 *
 */
public class GraphNode {

    private Node contents;

    private double x;
    private double y;
    
    private String name;

    public GraphNode(Node node, String name) {
	node.setUserData(this);
	contents = node;
	this.name = name;
    }

    public void setPosition(double x, double y) {
	contents.relocate(x, y);
	this.x = x;
	this.y = y;
    }

    public void translate(double x, double y, double scale) {
	this.x += x/scale;
	this.y += y/scale;
	
	contents.relocate(contents.getLayoutX()+x, contents.getLayoutY()+y);	
    }
    
    public void scale(double scale){
	contents.relocate(x*scale, y*scale);
	contents.setScaleX(scale);
	contents.setScaleY(scale);
    }
    
    public double getX(double scale){
	return x*scale;
    }
    
    public double getY(double scale){
	return y*scale;
    }
    
    public Node getContents(){
	return contents;
    }
    
    public String getName(){
	return name;
    }

}
