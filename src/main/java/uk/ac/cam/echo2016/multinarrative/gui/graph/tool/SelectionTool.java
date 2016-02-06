package uk.ac.cam.echo2016.multinarrative.gui.graph.tool;

import java.util.HashSet;
import java.util.Set;

import javafx.css.PseudoClass;
import javafx.scene.input.MouseEvent;
import uk.ac.cam.echo2016.multinarrative.gui.graph.Graph;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphEdge;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphNode;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphTool;

public class SelectionTool implements GraphTool {

    public static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

    private boolean dragging;

    private GraphNode select;

    private double mouseX;
    private double mouseY;

    private Graph graph;

    private Set<GraphNode> selection = new HashSet<GraphNode>();

    public SelectionTool(Graph graph) {
	this.graph = graph;
    }

    public void resetSelection() {
	for (GraphNode node : selection) {
	    node.getContents().pseudoClassStateChanged(SELECTED, false);
	}
	selection.clear();
    }

    public void select(GraphNode node) {
	node.getContents().pseudoClassStateChanged(SELECTED, true);
	selection.add(node);
    }

    public Set<GraphNode> getSelection() {
	return selection;
    }

    public boolean isSelected(GraphNode node) {
	return selection.contains(node);
    }

    public void deselect(GraphNode node) {
	node.getContents().pseudoClassStateChanged(SELECTED, false);
	selection.remove(node);
    }

    @Override
    public void mousePressed(MouseEvent event) {
	mouseX = event.getSceneX();
	mouseY = event.getSceneY();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
	dragging = false;
	select = null;
	mouseX = Double.NaN;
	mouseY = Double.NaN;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
	if (!dragging) {
	    dragging = true;
	} else {
	    if (!selection.isEmpty()) {
		double movementX = mouseX == mouseX ? event.getSceneX() - mouseX : 0.0;
		double movementY = mouseY == mouseY ? event.getSceneY() - mouseY : 0.0;

		for (GraphNode node : selection) {
		    node.translate(movementX, movementY, graph.getInput().getScale());
		    graph.updateNode(node);
		}

	    }

	}

	mouseX = event.getSceneX();
	mouseY = event.getSceneY();
    }

    @Override
    public void mousePressedOnNode(MouseEvent event, GraphNode node) {
	select = node;
    }

    @Override
    public void mousePressedOnEdge(MouseEvent event, GraphEdge edge) {
    }

    @Override
    public void mouseReleasedOnNode(MouseEvent event, GraphNode node) {
	
	if (select == node) {
		if (isSelected(select)) {
		    deselect(select);
		} else {
		    select(select);
		}
	    } 
	
	mouseReleased(event);
    }

    @Override
    public void mouseReleasedOnEdge(MouseEvent event, GraphEdge edge) {
	mouseReleased(event);
    }

}
