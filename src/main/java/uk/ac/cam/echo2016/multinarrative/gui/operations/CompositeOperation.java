package uk.ac.cam.echo2016.multinarrative.gui.operations;

import java.util.ArrayList;

public class CompositeOperation implements Operation {

    private ArrayList<Operation> items;
    
    public CompositeOperation(ArrayList<Operation> items) {
        this.items = items;
    }

    @Override
    public void execute() throws IllegalOperationException {
        for(Operation op: items){
            op.execute();
        }
    }

    @Override
    public void undo() throws IllegalOperationException {
        for(int i = items.size()-1;i>=0;i--){
            items.get(i).undo();
        }
    }

}
