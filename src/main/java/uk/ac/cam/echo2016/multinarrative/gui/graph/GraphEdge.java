package uk.ac.cam.echo2016.multinarrative.gui.graph;

import javafx.scene.Node;
import javafx.scene.shape.CubicCurve;

public class GraphEdge {

    private GraphNode from;
    private GraphNode to;
    
    private double xOffset;
    private double yOffset;
    
    private CubicCurve display;
    
    private String name;
    
    public GraphEdge(String name, GraphNode source, GraphNode dest) {
	this.name = name;
	from = source;
	to = dest;
	display = new CubicCurve();
    }
    
    public Node getNode(){
	return display;
    }
    
    public void shift(double x, double y){
	xOffset+=x;
	yOffset+=y;
    }
    
    public void update(double scale){
	display.setStartX(from.getX(scale));
	display.setStartY(from.getY(scale));
	display.setStartX(to.getX(scale));
	display.setStartY(to.getY(scale));
	
	double cX = from.getX(scale)+to.getX(scale);
	double cY = from.getY(scale)+to.getY(scale);
	cX/=2.0;
	cY/=2.0;
	cX+=xOffset*scale;
	cY+=yOffset*scale;
	
	display.setControlX1(cX);
	display.setControlY1(cY);
	display.setControlX2(cX);
	display.setControlY2(cY);
    }
    
    public GraphNode getFrom(){
	return from;
    }
    
    public GraphNode getTo(){
	return to;
    }
    
    public void translate(double xOff, double yOff){
	xOffset+=xOff;
	yOffset+=yOff;
    }
    
    public String getName(){
	return name;
    }

}
