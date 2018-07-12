package it.finsiel.siged.exception;

/*
 * @author Almaviva sud.
 */

public class AuthenticationException extends Exception {


	private static final long serialVersionUID = 1L;

	public AuthenticationException() {
        super();
    }

    public AuthenticationException(String m) {
        super(m);
    }

};