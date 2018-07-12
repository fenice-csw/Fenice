package it.finsiel.siged.exception;

/*
 * @author Almaviva sud.
 */

public class MessageParsingException extends Exception {

	private static final long serialVersionUID = 1L;
	
    public MessageParsingException() {
        super();
    }

    public MessageParsingException(String m) {
        super(m);
    }

};