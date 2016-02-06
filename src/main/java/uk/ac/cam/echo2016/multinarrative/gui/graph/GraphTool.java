package uk.ac.cam.echo2016.multinarrative.gui.graph;

import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

public interface GraphTool {
    
    void mousePressed(MouseEvent event);

    void mouseReleased(MouseEvent event);
    
    void mouseDragged(MouseEvent event);
    
    void mousePressedOnNode(MouseEvent event, GraphNode node);

    void mousePressedOnEdge(MouseEvent event, GraphEdge edge);
    
    void mouseReleasedOnNode(MouseEvent event, GraphNode node);

    void mouseReleasedOnEdge(MouseEvent event, GraphEdge edge);
    
    void dragStart(MouseEvent event);
}
