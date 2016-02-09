package uk.ac.cam.echo2016.multinarrative.gui.graph;

import javafx.beans.property.StringProperty;
import javafx.scene.layout.Region;


/**
 * 
 * @author jr650
 *
 */
public class GraphNode {

    private Region contents;
    
    private StringProperty name;

    private double x;
    private double y;

    public GraphNode(Region node, StringProperty name) {
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
    
    public StringProperty getNameProperty(){
	return name;
    }
    
    public String getName(){
        return name.get();
    }

}
