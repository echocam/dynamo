package uk.ac.cam.echo2016.multinarrative.gui.commands;

import java.util.ArrayList;
import java.util.List;

public interface Command {
    public static final List<Command> history = new ArrayList<Command>();

    public static void storeAndExecute(Command c) throws CommandException {
        c.execute();
        history.add(c);
    }

    public void execute() throws CommandException;

    public void undo() throws CommandException;
}
