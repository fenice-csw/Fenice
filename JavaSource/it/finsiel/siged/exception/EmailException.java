package it.finsiel.siged.exception;

import javax.mail.NoSuchProviderException;

/*
 * @author Almaviva sud.
 */

public class EmailException extends Exception {

	private static final long serialVersionUID = 1L;
	
    public EmailException() {
        super();
    }

    public EmailException(String m) {
        super(m);
    }

    public EmailException(String m,NoSuchProviderException e1) {
    	super(m);
    }
};