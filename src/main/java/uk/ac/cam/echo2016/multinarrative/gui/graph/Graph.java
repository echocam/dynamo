package uk.ac.cam.echo2016.multinarrative.gui.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Shape;
import uk.ac.cam.echo2016.multinarrative.gui.FXMLController;
import uk.ac.cam.echo2016.multinarrative.gui.operations.GUIOperations;

/**
 * 
 * @author jr650
 *
 */
public class Graph {

    private Pane pane;
    private GUIOperations operations;

    private InputMonitor input;
    private GraphTool tool;

    private Set<GraphNode> nodes = new HashSet<>();
    private Set<GraphEdge> edges = new HashSet<>();

    private FXMLController controller;

    public Graph(ScrollPane pane, Pane p, GUIOperations operations, FXMLController c) {
        this.pane = p;
        this.operations = operations;
        controller = c;
        input = new InputMonitor(this);
        input.registerHandlerFor(p);
        input.registerHandlerFor(pane);
    }

    public void setTool(GraphTool tool) {
        this.tool = tool;
    }

    public GraphTool getTool() {
        return tool;
    }

    public Set<GraphNode> getNodes() {
        return nodes;
    }

    public Set<GraphEdge> getEdges() {
        return edges;
    }

    public void update(double scale) {
        for (GraphEdge edge : edges) {
            updateEdge(edge);
        }
    }

    public void updateEdge(GraphEdge edge) {
        edge.update(this);
    }

    public void updateNode(GraphNode node) {
        node.update(this);
        for (GraphEdge edge : edges) {
            if (edge.getFrom() == node || edge.getTo() == node) {
                updateEdge(edge);
            }
        }
    }

    public void addNode(GraphNode node) {
        Node contents = node.getContents();
        pane.getChildren().add(contents);
        nodes.add(node);
        input.registerHandlerFor(contents);
    }

    public void removeNode(GraphNode node) {
        Node contents = node.getContents();
        pane.getChildren().remove(contents);
        nodes.remove(node);
        controller.removeNode(node.getName());
        for (GraphEdge edge : new ArrayList<>(edges)) {
            if (edge.getFrom() == node || edge.getTo() == node) {
                removeEdge(edge);
            }
        }
    }

    public void addEdge(GraphEdge edge) {
        CubicCurve n = edge.getNode();
        Shape c = edge.getControl();
        pane.getChildren().add(0, n);
        int i = 1;
        while (pane.getChildren().get(i) instanceof CubicCurve) {
            i++;
        }
        pane.getChildren().add(i, c);
        edge.update(this);
        edges.add(edge);
        input.registerHandlerFor(n);
        input.registerHandlerFor(c);
    }

    public void removeEdge(GraphEdge edge) {
        pane.getChildren().remove(edge.getNode());
        pane.getChildren().remove(edge.getControl());
        edges.remove(edge);
        controller.removeRoute(edge.getName());
    }

    public InputMonitor getInput() {
        return input;
    }

    public FXMLController getController() {
        return controller;
    }

    public GUIOperations getOperations() {
        return operations;
    }

    public Pane getPane() {
        return pane;
    }

    public void renameNode(String from, String to) {
        for (GraphNode node : nodes) {
            if (node.getName().equals(from)) {
                node.getNameProperty().set(to);
                updateNode(node);
            }
        }
    }

    public void renameRoute(String from, String to) {
        for (GraphEdge edge : edges) {
            if (edge.getName().equals(from)) {
                edge.getNameProperty().set(to);
            }
        }
    }

    public void deleteNode(String name) {
        GraphNode find = null;
        for (GraphNode node : nodes) {
            if (node.getName().equals(name)) {
                find = node;
                break;
            }
        }
        if (find != null) {
            removeNode(find);
        }
    }

    public void deleteRoute(String name) {
        GraphEdge find = null;
        for (GraphEdge edge : edges) {
            if (edge.getName().equals(name)) {
                find = edge;
                break;
            }
        }
        if (find != null) {
            removeEdge(find);
        }
    }

}
