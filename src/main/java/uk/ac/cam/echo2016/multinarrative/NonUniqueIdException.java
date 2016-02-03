package uk.ac.cam.echo2016.multinarrative;

/**
 * 
 * @author tr393
 *
 */
public class NonUniqueIdException extends Exception {

	private static final long serialVersionUID = -5346075710013301725L; // Made eclipse happy :)
	
	public NonUniqueIdException() {
		super();
	}
	public NonUniqueIdException(String message) {
		super(message);
	}
}
