package it.finsiel.siged.exception;

/*
 * @author Almaviva sud.
 */

public class ValidationException extends Exception {

	private static final long serialVersionUID = 1L;
	
    public ValidationException() {
        super();
    }

    public ValidationException(String m) {
        super(m);
    }

};