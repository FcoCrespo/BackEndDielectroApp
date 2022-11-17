package edu.dimae.uclm.dielectroapp.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
class TestGestionUsuario {
	
	@Autowired
	private UserService userService;
	
	@Test 
	@Order(1) 
	@DisplayName("Registro de crespo, delegacion 00 y email crespo@prueba.es. La contrasena llegara por correo.")
	void testRegistro() {
		try {
			userService.registrarUser("crespo", "crespo@prueba.es", "admin", "00");
		} catch (Exception e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}
	}
	
	@Test 
	@Order(2) 
	@DisplayName("Registro de crespo, delegacion 00, contrasena 1234, email crespo@prueba.es")
	public void testRegistroDuplicado() {
		try {
			userService.registrarUser("crespo", "crespo@prueba.es", "admin", "00");
			fail("Se esperaba UsuarioYaExisteException");
		} catch (Exception e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}
	}
	
	@Test 
	@Order(3) 
	@DisplayName("Acceso al sistema del usuario de crespo, con contrasena 1234")
	void testAcceso() {
		boolean acceso = userService.getUserByUsernameAndPassword("crespo", "1234");
		assertTrue(acceso);
	}
	
	@Test 
	@Order(4) 
	@DisplayName("Acceso al sistema del usuario de crespo, con contrasena 1235")
	void testAccesoInvalido() {
		boolean acceso = userService.getUserByUsernameAndPassword("crespo", "1235");
		assertFalse(acceso);
	}
	
	@Test 
	@Order(4) 
	@DisplayName("Acceso al sistema del usuario de pepe, no existe, con contrasena 1234")
	void testAccesoInvalidoUser() {
		boolean acceso = userService.getUserByUsernameAndPassword("pepe", "1234");
		assertFalse(acceso);
	}
	

	@Test 
	@Order(5) 
	@DisplayName("Registro de usuario pepe, con mismo email que usuario crespo")
	void testEmailDuplicado() {
		try {
			userService.registrarUser("pepe", "crespo@prueba.es", "admin", "00");
			fail("Se esperaba EmailYaExisteException");
		} catch (Exception e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}
	}
	
	
	@Test 
	@Order(6) 
	@DisplayName("Actualizacion de datos de usuario crespo")
	void testActualizarUser() {
		userService.actualizarUsuario("crespo", "1235");
		boolean acceso = userService.getUserByUsernameAndPassword("crespo", "1235");
		assertTrue(acceso);
		boolean acceso2 = userService.getByUsernameAndEmail("crespo", "crespo2@prueba.com");
		assertTrue(acceso2);
	}
	
	
	@Test 
	@Order(7) 
	@DisplayName("Obtencion de usuario por su tokenpass")
	void testUsuarioToken() {
	
		try {
			String jsonData = userService.sendSecureUser("crespo", "1235");
			JsonNode node = new ObjectMapper().readTree(jsonData);

			SecureUser secureUser  = new SecureUser(node.get("id").textValue(),
					 node.get("username").textValue(),
					 node.get("role").textValue(),
					 node.get("tokenPass").textValue(),
					 node.get("delegacion").textValue()
					);
			boolean userExists = userService.getUserByTokenPass(secureUser.getTokenPass());
			assertTrue(userExists);
		} catch (JsonMappingException e) {	
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		} catch (JsonProcessingException e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}

	}
	
	@Test 
	@Order(8) 
	@DisplayName("Obtener usuario por su nombre de usuario, que es crespo")
	void testUsuarioNombre() {
		
		try {
			String jsonData = userService.findByUsername("crespo");
			JsonNode node = new ObjectMapper().readTree(jsonData);

			SecureUser secureUser  = new SecureUser(node.get("id").textValue(),
					 node.get("username").textValue(),
					 node.get("role").textValue(),
					 node.get("tokenPass").textValue(),
					 node.get("delegacion").textValue()
					);
			boolean userExists = userService.getUserByTokenPass(secureUser.getTokenPass());
			assertTrue(userExists);
		} catch (JsonMappingException e) {	
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		} catch (JsonProcessingException e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}
	}
	
	@Test 
	@Order(9) 
	@DisplayName("Obtencion de todos los usuarios")
	void testTodosUsuarios() {
		try {
			boolean content = false;
			String jsonData = this.userService.getAllUsers();
			if(!jsonData.isEmpty()) {
				content =true;
			}
			assertTrue(content);
			
		} catch (Exception e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}
	}
	
	
	@Test 
	@Order(10) 
	@DisplayName("Eliminar usuario creado crespo")
	void testEliminarUsuario() {
		try {
			this.userService.deleteUser("crespo");		} 
		catch (Exception e) {
			fail("No se esperaba excepción, pero se ha lanzado " + e.getMessage());
		}
		
	}
	
	
}
