package it.finsiel.siged.exception;

/*
 * @author Almaviva sud.
 */

public class CannotDeleteException extends Exception {

	private static final long serialVersionUID = 1L;

	public CannotDeleteException() {
        super();
    }

    public CannotDeleteException(String m) {
        super(m);
    }

};