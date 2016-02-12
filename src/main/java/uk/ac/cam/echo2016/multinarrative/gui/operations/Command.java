package uk.ac.cam.echo2016.multinarrative.gui.operations;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 
 * The Command class is used to implement most functions in GUIOperations. The reason for this is that anything that
 * affects the graph, must be 'undoable', and this provides a clean interface for enabling that functionality.
 * 
 * @author tr395
 *
 */
public interface Command {
    public static final Deque<Command> undoHistory = new ArrayDeque<Command>(64); // TODO(tr395): Make this configurable
                                                                                  // undo limit!
    public static final Deque<Command> redoHistory = new ArrayDeque<Command>(16);

    /**
     * Executes a command and stores it on the undoHistory Stack.
     * 
     * If an exception is thrown by the command, it won't be added to the history.
     * 
     * Furthermore the redoHistory is flushed.
     * 
     * @param c
     *            Command that you wish to execute
     * @throws IllegalOperationException
     */
    public static void storeAndExecute(Command c) throws IllegalOperationException {
        c.execute();
        if (undoHistory.size() > 63) // TODO(tr395): make this configurable undo limit!
            undoHistory.removeLast();
        undoHistory.addFirst(c);
        redoHistory.clear();
    }

    /**
     * Undo the last command.
     */
    public static void undoLastCommand() {
        Command c = undoHistory.removeFirst();
        c.undo();
        if (redoHistory.size() > 15)
            redoHistory.removeLast();
        redoHistory.addFirst(c);
    }

    /**
     * Redo the last undo. The exception should never really be thrown as only successful commands should be able to
     * reach this point.
     * 
     * @throws IllegalOperationException
     */
    public static void redoLastUndo() {
        Command c = redoHistory.removeFirst();
        try {
            c.execute();
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e);
        }
        undoHistory.addFirst(c);
    }

    public void execute() throws IllegalOperationException;

    public void undo();
}
