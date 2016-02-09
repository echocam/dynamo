package uk.ac.cam.echo2016.multinarrative.gui.graph;

import javafx.scene.layout.Region;


/**
 * 
 * @author jr650
 *
 */
public class GraphNode {

    private Region contents;

    private double x;
    private double y;
    
    private String name;

    public GraphNode(Region node, String name) {
	node.setUserData(this);
	contents = node;
	this.name = name;
    }

    public void setPosition(double x, double y) {
	contents.relocate(x, y);
	this.x = x;
	this.y = y;
    }

    public void translate(double x, double y) {
	this.x += x;
	this.y += y;
	
	contents.relocate(contents.getLayoutX()+x, contents.getLayoutY()+y);	
    }
    
    public double getX(){
	return x;
    }
    
    public double getY(){
	return y;
    }
    
    public Region getContents(){
	return contents;
    }
    
    public String getName(){
	return name;
    }

}
