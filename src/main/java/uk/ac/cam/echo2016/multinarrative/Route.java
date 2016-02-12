package uk.ac.cam.echo2016.multinarrative;

import java.io.Serializable;

import android.os.BaseBundle;

/**
 * 
 * Acts as a connection between nodes. Represents the paths that characters follow.
 * 
 * @author tr393
 * @author eyx20
 * @author rjm232
 * @author jr650
 * @version 1.0
 *
 */
public class Route implements Serializable, Cloneable { // TODO Documentation
    private static final long serialVersionUID = 1;
    private final String id;
    private StoryNode start;
    private StoryNode end;
    private BaseBundle properties;

    public Route(String id, StoryNode start, StoryNode end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public void setup() {
        start.getExiting().add(this);
        end.getEntering().add(this);
    }

    @Override
    public Route clone() {
        try {
            Route clone = (Route) super.clone();
            clone.properties = BaseBundle.deepcopy(this.properties);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getId() {
        return id;
    }

    public StoryNode getStart() {
        return start;
    }

    public void setStart(StoryNode start) {
        this.start = start;
    }

    public StoryNode getEnd() {
        return end;
    }

    public void setEnd(StoryNode end) {
        this.end = end;
    }

    public void createProperties() {
        if (properties == null)
            properties = new BaseBundle(4);
    }

    public BaseBundle getProperties() {
        return properties;
    }

    public void setProperties(BaseBundle b) {
        properties = b;
    }
}
