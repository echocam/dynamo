package uk.ac.cam.echo2016.multinarrative.gui.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Shape;
import uk.ac.cam.echo2016.multinarrative.gui.FXMLController;
import uk.ac.cam.echo2016.multinarrative.gui.OperationsManager;

/**
 * 
 * @author jr650
 *
 */
public class Graph {

    private Pane pane;
    private OperationsManager operations;

    private InputMonitor input;
    private GraphTool tool;

    private Map<String, GraphNode> nodes = new HashMap<>();
    private Map<String, GraphEdge> edges = new HashMap<>();

    private FXMLController controller;

    public Graph(ScrollPane pane, Pane p, OperationsManager operations, FXMLController c) {
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

    public Map<String, GraphNode> getNodes() {
        return nodes;
    }

    public Map<String, GraphEdge> getEdges() {
        return edges;
    }

    public void update(double scale) {
        for (GraphEdge edge : edges.values()) {
            updateEdge(edge);
        }
    }

    public void updateEdge(GraphEdge edge) {
        edge.update(this);
    }

    public void updateNode(GraphNode node) {
        node.update(this);
        for (GraphEdge edge : edges.values()) {
            if (edge.getFrom() == node || edge.getTo() == node) {
                updateEdge(edge);
            }
        }
    }

    public void addNode(GraphNode node) {
        Node contents = node.getContents();
        pane.getChildren().add(contents);
        nodes.put(node.getName(), node);
        input.registerHandlerFor(contents);
    }

    public void removeNode(GraphNode node) {
        Node contents = node.getContents();
        pane.getChildren().remove(contents);
        nodes.remove(node);
        for (GraphEdge edge : new ArrayList<>(edges.values())) {
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
        edges.put(edge.getName(), edge);
        input.registerHandlerFor(n);
        input.registerHandlerFor(c);
    }

    public void removeEdge(GraphEdge edge) {
        pane.getChildren().remove(edge.getNode());
        pane.getChildren().remove(edge.getControl());
        edges.remove(edge);
    }

    public InputMonitor getInput() {
        return input;
    }

    public FXMLController getController() {
        return controller;
    }

    public OperationsManager getOperations() {
        return operations;
    }

    public Pane getPane() {
        return pane;
    }

    public void renameNode(String from, String to) {
        GraphNode node = nodes.remove(from);
        if (node != null) {
            node.getNameProperty().set(to);
            nodes.put(to, node);
            updateNode(node);
        }
    }

    public void renameRoute(String from, String to) {
        GraphEdge edge = edges.remove(from);
        if (edge != null) {
            edge.getNameProperty().set(to);
            edges.put(to, edge);
        }
    }

    public void deleteNode(String name) {
        GraphNode node = nodes.get(name);
        if (node != null)
            removeNode(node);
    }

    public void deleteRoute(String name) {
        GraphEdge edge = edges.get(name);
        if (edge != null)
            removeEdge(edge);
    }

    public void clear() {
        nodes.clear();
        edges.clear();
    }

}
