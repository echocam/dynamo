package uk.ac.cam.echo2016.multinarrative.gui.operations;

import uk.ac.cam.echo2016.multinarrative.Node;
import uk.ac.cam.echo2016.multinarrative.Route;

import java.util.ArrayList;
import java.util.HashSet;

public class DFSCycleDetect {

    private HashSet<Node> marked;
    private ArrayList<Node> onStack;
    private boolean hasCycle;

    public DFSCycleDetect(Node node) {
        marked = new HashSet<Node>();
        onStack = new ArrayList<Node>();
        findCycle(node);
    }

    public boolean hasCycle() {
        return hasCycle;
    }

    public void findCycle(Node n) {

        marked.add(n);
        onStack.add(n);

        for (Route rt : n.getExiting()) {
            Node next = rt.getEnd();
            if (!marked.contains(next)) {
                findCycle(next);
            } else if (onStack.contains(next)) {
                hasCycle = true;
                return;
            }
        }

        onStack.remove(n);
    }
}