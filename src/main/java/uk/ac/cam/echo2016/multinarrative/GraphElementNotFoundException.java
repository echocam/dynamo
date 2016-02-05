package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author Rjmcf
 *
 */

public class GraphElementNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

    public GraphElementNotFoundException(String message){
        super(message);
    }

    public GraphElementNotFoundException(Throwable cause){
        super(cause);
    }

    public GraphElementNotFoundException(String message, Throwable cause){
        super(message, cause);
    }

}

