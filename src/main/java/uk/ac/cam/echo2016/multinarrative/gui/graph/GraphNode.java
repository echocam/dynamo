package uk.ac.cam.echo2016.multinarrative.gui.graph;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import uk.ac.cam.echo2016.multinarrative.gui.FXUtil;

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

	public GraphNode(Region node, StringProperty name, double x, double y, Graph g) {
		node.setUserData(this);
		contents = node;
		this.name = name;
		node.layoutXProperty().bind(this.x);
		node.layoutYProperty().bind(this.y);
		this.x.set(x);
		this.y.set(y);
		update(g);
	}

	public void setPosition(double x, double y) {
		this.x.set(x);
		this.y.set(y);
	}

	public void translate(double x, double y) {
		this.x.set(this.x.get() + x);
		this.y.set(this.y.get() + y);
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

	public void update(Graph g) {
		ArrayList<Color> c = g.getOperations().getNodeColor(name.get());
		if (c.isEmpty()) {
			contents.setStyle("-fx-background-color: #333333;");
		}else if (c.size() == 1){
			contents.setStyle("-fx-background-color: "+FXUtil.colorToHex(c.get(0))+";");
		}else {
			String s = "-fx-background-color: linear-gradient(to right";
			for(Color col:c){
				s+=", " + FXUtil.colorToHex(col);
			}
			s+=");";
			contents.setStyle(s);
		}
	}

}
