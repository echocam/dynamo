package uk.ac.cam.echo2016.multinarrative.gui.operations;

import java.util.ArrayDeque;
import java.util.Deque;

public class UndoableOperationSequence {

    public static final int UNDO_LIMIT = 64; 
    public static final int REDO_LIMIT = 16; 

    private int undoLimit;
    private int redoLimit;
    
    private Deque<Operation> undoHistory;
    private Deque<Operation> redoHistory;

    public UndoableOperationSequence() {
        this(UNDO_LIMIT,REDO_LIMIT);
    }
    
    public UndoableOperationSequence(int undoLimit, int redoLimit) {
        undoHistory =  new ArrayDeque<Operation>(undoLimit);
        redoHistory = new ArrayDeque<Operation>(redoLimit);
        this.undoLimit = undoLimit;
        this.redoLimit = redoLimit;
    }
    
    /**
     * Executes a command and stores it on the undoHistory Stack.
     * 
     * If an exception is thrown by the command, it won't be added to the history.
     * 
     * Furthermore the redoHistory is flushed.
     * 
     * @param c
     *            Operation that you wish to execute
     * @throws IllegalOperationException
     */
    public void storeAndExecute(Operation c) throws IllegalOperationException {
        c.execute();
        if (undoHistory.size() >= undoLimit)
            undoHistory.removeLast();
        undoHistory.addFirst(c);
        redoHistory.clear();
    }
    
    /**
     * Undo the last command.
     */
    public void undoLastOperation() throws IllegalOperationException {
        Operation c = undoHistory.removeFirst();
        c.undo();
        if (redoHistory.size() >= redoLimit)
            redoHistory.removeLast();
        redoHistory.addFirst(c);
    }
    
    /**
     * Redo the last undo. The exception should never really be thrown as only successful commands should be able to
     * reach this point.
     * 
     * @throws IllegalOperationException
     */
    public void redoLastUndo() {
        Operation c = redoHistory.removeFirst();
        try {
            c.execute();
        } catch (IllegalOperationException e) {
            throw new RuntimeException(e);
        }
        undoHistory.addFirst(c);
    }

}
