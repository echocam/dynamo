package uk.ac.cam.echo2016.multinarrative.gui.graph;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

	private DoubleProperty x = new SimpleDoubleProperty();
	private DoubleProperty y = new SimpleDoubleProperty();

	public GraphNode(Region node, StringProperty name, double x, double y) {
		node.setUserData(this);
		contents = node;
		this.name = name;
		node.layoutXProperty().bind(this.x);
		node.layoutYProperty().bind(this.y);
		this.x.set(x);
		this.y.set(y);
	}

	public void setPosition(double x, double y) {
		this.x.set(x);
		this.y.set(y);
	}

	public void translate(double x, double y) {
		this.x.set(this.x.get()+x);
		this.y.set(this.y.get()+y);
	}

	public double getX() {
		return x.get();
	}

	public DoubleProperty xProperty() {
		return x;
	}

	public double getY() {
		return y.get();
	}
	
	public DoubleProperty yProperty() {
		return y;
	}

	public Region getContents() {
		return contents;
	}

	public StringProperty getNameProperty() {
		return name;
	}

	public String getName() {
		return name.get();
	}

}
