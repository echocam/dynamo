package uk.ac.cam.echo2016.multinarrative.gui.operations;

import java.util.ArrayList;
import java.util.List;

public interface Command {
    public static final List<Command> history = new ArrayList<Command>();

    public static void storeAndExecute(Command c) throws IllegalOperationException {
        c.execute();
        history.add(c);
    }

    public void execute() throws IllegalOperationException;

    public void undo() throws IllegalOperationException;
}
