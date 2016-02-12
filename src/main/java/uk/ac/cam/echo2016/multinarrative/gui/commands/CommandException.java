package uk.ac.cam.echo2016.multinarrative.gui.commands;

public class CommandException extends Exception {

    private static final long serialVersionUID = -5346075710013301725L;

    public CommandException() {
        super();
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
