package uk.ac.cam.echo2016.multinarrative.gui.operations;

import java.util.ArrayList;

public class CompositeOperation implements Operation {

    private ArrayList<OperationGenerator> gens;
    private ArrayList<Operation> items;

    public CompositeOperation(ArrayList<OperationGenerator> gens) {
        this.gens = gens;
    }

    @Override
    public void execute() throws IllegalOperationException {
        if (items == null) {
            items = new ArrayList<>();
            for (OperationGenerator gen : gens) {
                Operation op = gen.generate();
                items.add(op);
                op.execute();
            }
        } else {
            for (Operation op : items) {
                op.execute();
            }
        }
    }

    @Override
    public void undo() throws IllegalOperationException {
        for (int i = items.size() - 1; i >= 0; i--) {
            items.get(i).undo();
        }
    }

}
