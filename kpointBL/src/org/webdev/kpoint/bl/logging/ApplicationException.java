package org.webdev.kpoint.bl.logging;


public class ApplicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2047333339320419881L;

	public ApplicationException() {
		super();
	}

	public ApplicationException(String msg) {
		super(msg);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
