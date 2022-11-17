package edu.dimae.uclm.dielectroapp.exceptions;

public class EmailYaExisteException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public EmailYaExisteException(String msg) {
		super("El email ya existe en el sistema.");
	}

}
