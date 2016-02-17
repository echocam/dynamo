package uk.ac.cam.echo2016.multinarrative.gui.tool;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import uk.ac.cam.echo2016.multinarrative.gui.graph.Graph;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphEdge;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphNode;
import uk.ac.cam.echo2016.multinarrative.gui.graph.GraphTool;
import uk.ac.cam.echo2016.multinarrative.gui.operations.IllegalOperationException;

public class InsertTool implements GraphTool {

    private GraphNode start;
    private GraphEdge pressed;

    boolean dragging = false;

    private Graph graph;

    public InsertTool(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (!dragging) {
            try {
                if (pressed != null) {
                    String name = graph.getOperations().narrativeOperations().getUniqueNodeName();
                    graph.getOperations().doOp(graph.getOperations().generator().addChoiceNode(name, pressed));
                } else if (start == null) {
                    String name = graph.getOperations().narrativeOperations().getUniqueNodeName();
                    graph.getOperations()
                            .doOp(graph.getOperations().generator().addSynchNode(name, event.getX(), event.getY()));
                }
            } catch (IllegalOperationException ioe) {
                graph.getController().setInfo(ioe.getMessage());
            }
        }
        start = null;
        pressed = null;
        dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
    }

    @Override
    public void mousePressedOnNode(MouseEvent event, GraphNode node) {
        start = node;
    }

    @Override
    public void mousePressedOnEdge(MouseEvent event, GraphEdge edge) {
        pressed = edge;
    }

    @Override
    public void mouseReleasedOnNode(MouseEvent event, GraphNode node) {
        if (start != null) {
            String name = graph.getOperations().narrativeOperations().getUniqueRouteName();
            try {
                graph.getOperations()
                        .doOp(graph.getOperations().generator().addRoute(name, start.getName(), node.getName()));
            } catch (IllegalOperationException ioe) {
                graph.getController().setInfo(ioe.getMessage());
            }
        }
    }

    @Override
    public void mouseReleasedOnEdge(MouseEvent event, GraphEdge edge) {
    }

    @Override
    public void dragStart(MouseEvent event) {
        dragging = true;
        if (event.getSource() instanceof Node) {
            ((Node) event.getSource()).startFullDrag();
        }
    }

}
