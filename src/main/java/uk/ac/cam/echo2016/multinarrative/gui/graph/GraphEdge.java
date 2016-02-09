package uk.ac.cam.echo2016.multinarrative.gui.graph;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;

public class GraphEdge {

    private static final Stop[] STOPS = { new Stop(0.0, Color.WHITE), new Stop(1.0, Color.rgb(68, 68, 68)) };

    private GraphNode from;
    private GraphNode to;

    private double xOffset;
    private double yOffset;

    private CubicCurve display;
    private Circle control;

    private String name;

    public GraphEdge(String name, GraphNode source, GraphNode dest, CubicCurve curve, Circle midpoint) {
	this.name = name;
	from = source;
	to = dest;
	display = curve;
	control = midpoint;
	control.setUserData(this);
    }

    public CubicCurve getNode() {
	return display;
    }

    public Circle getControl(){
	return control;
    }
    
    public void shift(double x, double y) {
	xOffset += x;
	yOffset += y;
    }

    public void update() {

	double startX = from.getX() + from.getContents().getWidth() / 2;
	double startY = from.getY() + from.getContents().getHeight() / 2;
	double endX = to.getX() + to.getContents().getWidth() / 2;
	double endY = to.getY() + to.getContents().getHeight() / 2;

	display.setStartX(startX);
	display.setStartY(startY);
	display.setEndX(endX);
	display.setEndY(endY);

	double cX = startX + endX;
	double cY = startY + endY;
	cX /= 2.0;
	cY /= 2.0;
	cX += xOffset;
	cY += yOffset;
	
	display.setControlX1(cX);
	display.setControlY1(cY);
	display.setControlX2(cX);
	display.setControlY2(cY);
	
	cX *= 6;
	cY *= 6;
	cX += startX + endX;
	cY += startY + endY;
	cX /=8;
	cY /=8;
	
	control.setLayoutX(cX);
	control.setLayoutY(cY);

	display.setStroke(new LinearGradient(startX, startY, endX, endY, false, CycleMethod.NO_CYCLE, STOPS));
    }

    public GraphNode getFrom() {
	return from;
    }

    public GraphNode getTo() {
	return to;
    }

    public void translate(double xOff, double yOff) {
	xOffset += xOff;
	yOffset += yOff;
    }

    public String getName() {
	return name;
    }

}
