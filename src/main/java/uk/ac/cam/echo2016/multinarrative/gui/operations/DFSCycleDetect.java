package uk.ac.cam.echo2016.multinarrative.gui.operations;

import uk.ac.cam.echo2016.multinarrative.StoryNode;
import uk.ac.cam.echo2016.multinarrative.Route;

import java.util.ArrayList;
import java.util.HashSet;

public class DFSCycleDetect {

    private HashSet<StoryNode> marked;
    private ArrayList<StoryNode> onStack;
    private boolean hasCycle;

    public DFSCycleDetect(StoryNode node) {
        marked = new HashSet<StoryNode>();
        onStack = new ArrayList<StoryNode>();
        findCycle(node);
    }

    public boolean hasCycle() {
        return hasCycle;
    }

    public void findCycle(StoryNode n) {

        marked.add(n);
        onStack.add(n);

        for (Route rt : n.getExiting()) {
            StoryNode next = rt.getEnd();
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