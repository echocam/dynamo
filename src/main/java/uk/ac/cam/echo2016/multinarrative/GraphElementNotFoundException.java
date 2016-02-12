package uk.ac.cam.echo2016.multinarrative;

import uk.ac.cam.echo2016.multinarrative.gui.commands.CommandException;

/**
 * This is thrown if an identifier for an expected {@code Route} or {@code Node}
 * is not found in the {@code MultiNarrative} being searched.
 * 
 * @author rjm232
 * @author tr393
 *
 */

public class GraphElementNotFoundException extends CommandException {

    private static final long serialVersionUID = 1L;

    public GraphElementNotFoundException() {
        super();
    }

    public GraphElementNotFoundException(String message) {
        super(message);
    }

    public GraphElementNotFoundException(Throwable cause) {
        super(cause);
    }

    public GraphElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
