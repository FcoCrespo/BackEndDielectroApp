package edu.dimae.uclm.dielectroapp.exceptions;

public class UsuarioYaExisteException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public UsuarioYaExisteException(String msg) {
		super("El usuario ya existe en el sistema.");
	}

}
