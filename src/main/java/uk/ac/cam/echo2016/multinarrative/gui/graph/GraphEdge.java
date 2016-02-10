package uk.ac.cam.echo2016.multinarrative.gui.graph;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
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

	private DoubleProperty xOffset = new SimpleDoubleProperty();
	private DoubleProperty yOffset = new SimpleDoubleProperty();

	private CubicCurve display;
	private Circle control;

	private StringProperty name;

	public GraphEdge(StringProperty name, GraphNode source, GraphNode dest, CubicCurve curve, Circle midpoint) {
		this.name = name;
		from = source;
		to = dest;
		display = curve;
		control = midpoint;
		control.setUserData(this);
		build();
	}

	public CubicCurve getNode() {
		return display;
	}

	public Circle getControl() {
		return control;
	}

	public void shift(double x, double y) {
		xOffset.set(xOffset.get() + x);
		yOffset.set(yOffset.get() + y);
	}

	public void build() {
		DoubleBinding p_startX = from.xProperty().add(from.getContents().widthProperty().divide(2));
		DoubleBinding p_startY = from.yProperty().add(from.getContents().heightProperty().divide(2));

		display.startXProperty().bind(p_startX);
		display.startYProperty().bind(p_startY);
		
		DoubleBinding p_endX = to.xProperty().add(to.getContents().widthProperty().divide(2));
		DoubleBinding p_endY = to.yProperty().add(to.getContents().heightProperty().divide(2));

		display.endXProperty().bind(p_endX);
		display.endYProperty().bind(p_endY);

		DoubleBinding p_controlX = p_startX.add(p_endX).divide(2).add(xOffset);
		DoubleBinding p_controlY = p_startY.add(p_endY).divide(2).add(yOffset);

		display.controlX1Property().bind(p_controlX);
		display.controlY1Property().bind(p_controlY);
		display.controlX2Property().bind(p_controlX);
		display.controlY2Property().bind(p_controlY);

		DoubleBinding p_centreX = p_controlX.multiply(6).add(p_endX).add(p_startX).divide(8);
		DoubleBinding p_centreY = p_controlY.multiply(6).add(p_endY).add(p_startY).divide(8);

		control.centerXProperty().bind(p_centreX);
		control.centerYProperty().bind(p_centreY);
	}

	public void update() {
		double startX = from.getX() + from.getContents().getWidth() / 2;
		double startY = from.getY() + from.getContents().getHeight() / 2;
		double endX = to.getX() + to.getContents().getWidth() / 2;
		double endY = to.getY() + to.getContents().getHeight() / 2;

		display.setStroke(new LinearGradient(startX, startY, endX, endY, false, CycleMethod.NO_CYCLE, STOPS));
	}

	public GraphNode getFrom() {
		return from;
	}

	public GraphNode getTo() {
		return to;
	}

	public void setFrom(GraphNode from) {
		this.from = from;
		build();
	}

	public void setTo(GraphNode to) {
		this.to = to;
		build();
	}

	public void translate(double xOff, double yOff) {
		xOffset.set(xOffset.get() + xOff);
		yOffset.set(yOffset.get() + yOff);
	}

	public void zeroOffset(){
		xOffset.set(0);
		yOffset.set(0);
	}
	
	public StringProperty getNameProperty() {
		return name;
	}

	public String getName() {
		return name.get();
	}

}
