/*
 * Created on 1-feb-2005
 *
 * 
 */
package it.finsiel.siged.exception;

/**
 * @author Almaviva sud
 * 
 */
public class ConversionException extends Exception {

	private static final long serialVersionUID = 1L;

    public ConversionException() {
        super();
    }

    /**
     * @param arg0
     */
    public ConversionException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public ConversionException(Throwable arg0) {
        super(arg0);
    }
}
