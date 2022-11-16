package edu.dimae.uclm.dielectroapp.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.dimae.uclm.dielectroapp.services.UserService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class TestGestionUsuario {
	
	@Autowired
	private UserService userService;
	
	@Test 
	@Order(1) 
	@DisplayName("Registro de crespo, delegacion 00, contrasena 1234, email crespo@prueba.es")
	public void testRegistro() {
		//TODO
	}
	
	@Test 
	@Order(2) 
	@DisplayName("Registro de crespo, delegacion 00, contrasena 1234, email crespo@prueba.es")
	public void testRegistroDuplicado() {
		//TODO
	}
	
	@Test 
	@Order(3) 
	@DisplayName("Acceso al sistema del usuario de crespo, con contrasena 1234")
	public void testAcceso() {
		//TODO
	}
	
	@Test 
	@Order(4) 
	@DisplayName("Acceso al sistema del usuario de crespo, con contrasena 1235")
	public void testAccesoInvalido() {
		//TODO
	}
	
	@Test 
	@Order(4) 
	@DisplayName("Acceso al sistema del usuario de pepe, no existe, con contrasena 1234")
	public void testAccesoInvalidoUser() {
		//TODO
	}
	

	@Test 
	@Order(5) 
	@DisplayName("Registro de usuario pepe, con mismo email que usuario crespo")
	public void testEmailDuplicado() {
		//TODO
	}
	
	
	@Test 
	@Order(6) 
	@DisplayName("Actualizacion de datos de usuario crespo")
	public void testActualizarUser() {
		//TODO
	}
	
	
	@Test 
	@Order(7) 
	@DisplayName("Obtencion de usuario por su tokenpass")
	public void testUsuarioToken() {
		//TODO
	}
	
	@Test 
	@Order(8) 
	@DisplayName("Obtener usuario por su nombre de usuario")
	public void testUsuarioNombre() {
		//TODO
	}
	
	@Test 
	@Order(9) 
	@DisplayName("Obtencion de todos los usuarios")
	public void testTodosUsuarios() {
		//TODO
	}
	
	
	@Test 
	@Order(10) 
	@DisplayName("Eliminar usuario creado crespo")
	public void testEliminarUsuario() {
		//TODO
	}
	
	
}
