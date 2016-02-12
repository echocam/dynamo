package uk.ac.cam.echo2016.multinarrative.gui.tool;

import java.util.HashSet;
import java.util.Set;

import javafx.css.PseudoClass;
import javafx.scene.input.MouseEvent;
import uk.ac.cam.echo2016.multinarrative.gui.graph.Graph;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphEdge;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphNode;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphTool;
import uk.ac.cam.echo2016.multinarrative.gui.operations.IllegalOperationException;

public class SelectionTool implements GraphTool {

    public static final PseudoClass SELECTED = PseudoClass.getPseudoClass("selected");

    private boolean dragging;

    private GraphEdge press;
    private boolean selectMade;

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

    public void setSelection(String s) {
    	GraphNode node = graph.getNodes().get(s);
    	if(node!=null){
    		resetSelection();
            select(node);
    	}
    }

    @Override
    public void mousePressed(MouseEvent event) {
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (!dragging && press == null && !selectMade) {
            resetSelection();
            graph.getController().deselect();
        }
        dragging = false;
        selectMade = false;
        press = null;
        mouseX = Double.NaN;
        mouseY = Double.NaN;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (!selectMade) {
            resetSelection();
            selectMade = true;
        }
        if (!dragging) {
            dragging = true;
        } else {
            double movementX = mouseX == mouseX ? event.getSceneX() - mouseX : 0.0;
            double movementY = mouseY == mouseY ? event.getSceneY() - mouseY : 0.0;
            if (press != null) {
                press.translate(movementX, movementY);
                graph.updateEdge(press);
            } else if (!selection.isEmpty()) {
                for (GraphNode node : selection) {
                    try {
                        graph.getOperations().translateNode(node.getName(), movementX, movementY);
                        node.translate(movementX, movementY);
                        graph.updateNode(node);
                    } catch (IllegalOperationException e) {
                        graph.getController().setInfo(e.getMessage(), event.getSceneX() + "", event.getSceneY() + "");
                    }
                }
            }
        }

        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
    }

    @Override
    public void mousePressedOnNode(MouseEvent event, GraphNode node) {
        if (event.isControlDown()) {
            if (!isSelected(node)) {
                select(node);
            }
        } else if (event.isAltDown()) {
            if (isSelected(node)) {
                deselect(node);
            }
        } else {
            if (!isSelected(node)) {
                resetSelection();
                select(node);
            }
        }
        selectMade = true;
        mousePressed(event);
        graph.getController().selectNode(node.getName());
    }

    @Override
    public void mousePressedOnEdge(MouseEvent event, GraphEdge edge) {
        press = edge;
        mousePressed(event);
        graph.getController().selectRoute(edge.getName());

    }

    @Override
    public void mouseReleasedOnNode(MouseEvent event, GraphNode node) {
    }

    @Override
    public void mouseReleasedOnEdge(MouseEvent event, GraphEdge edge) {
    }

    @Override
    public void dragStart(MouseEvent event) {
        dragging = true;
    }

}
