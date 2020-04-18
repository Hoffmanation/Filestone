package com.filestone.handler;

/**
 * Custom Exception class will serve as an error message return to the client
 * in case a CRUD operation failed.
 * 
 * @author Hoffman
 *
 */
public class FilestoneMediaFileException extends Exception {

	private static final long serialVersionUID = -2159158830914661737L;

	public FilestoneMediaFileException(String message) {
		super(message);
	}

}
