package uk.ac.cam.echo2016.multinarrative;

import uk.ac.cam.echo2016.multinarrative.gui.commands.CommandException;

/**
 * 
 * @author tr393
 * @version 1.0
 */
public class NonUniqueIdException extends CommandException {

    private static final long serialVersionUID = -5346075710013301725L;

    public NonUniqueIdException() {
        super();
    }

    public NonUniqueIdException(String message) {
        super(message);
    }
}
