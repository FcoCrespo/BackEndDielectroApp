package edu.dimae.uclm.dielectroapp.controllers;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.dimae.uclm.dielectroapp.services.UserService;


/**
 * @author FcoCrespo "https://myesidevopsmetrics.herokuapp.com"
 */

@RestController
@RequestMapping("/users")
public class UserController {

	private static final Log LOG = LogFactory.getLog(UserController.class);

	@Autowired
	private UserService userService;
	
	
	/**
	 * Peticion GET que obtiene el usuario seguro con tokenpass a partir de usuario y contrasena correspondiente a su usuario
	 * @param username credencial de usuario para accerder al sistema
	 * @param password credencial de contrasena para acceder al sistema
	 * @return usuario seguro con tokenpass valido para realizar operaciones en el sistemab
	 */
	@GetMapping
	public ResponseEntity<String> getUserPassword(@RequestParam("username") final String username,
			@RequestParam("password") final String password) {

			final boolean existe = this.userService.getUserByUsernameAndPassword(username, password);

			if (existe) {
				LOG.info("Usuario encontrado");
				return ResponseEntity.ok(this.userService.sendSecureUser(username, password));
			} else {
				LOG.info("Usuario no encontrado");
				return ResponseEntity.badRequest().build();
			}
			
	}
	
	/**
	 * Peticion GET para recuperar la contrasena por correo a partir del usuario y el email
	 * @param username se correponde al usuario del sistema en caso de que exista
	 * @param email se correponde al email enlazado al usuario del sistema en caso de que exista
	 * @return resultado de la operación de recuperar la constrasena
	 */
	@GetMapping(value = "/recoverpassword")
	public ResponseEntity<String> recoverPassword(@RequestParam("username") final String username,
			@RequestParam("email") final String email) {
		

			final boolean existe = this.userService.getByUsernameAndEmail(username, email);
			if (existe) {
				LOG.info("Recover password");
				this.userService.recoverPassword(username, email);
				return ResponseEntity.ok("Password recovered.");
			} else {
				LOG.info("No existe un usuario con ese usuario y email en el sistema.");
				return ResponseEntity.badRequest().body("No existe un usuario con ese usuario y email en el sistema.");
			}

		
	}
	
	/**
	 * Peticion GET que obtiene todos los usuarios del sistema
	 * @param tokenpass valor del token de sesion del usuario
	 * @return informacion correspondiente a todos los usuarios del sistema
	 */
	@GetMapping(value = "/all")
	public ResponseEntity<String> allUsers(@RequestParam("tokenpass") final String tokenpass){

			final boolean existe = this.userService.getUserByTokenPass(tokenpass);
			if (existe) {
				LOG.info("Get all Users");
				return ResponseEntity.ok(this.userService.getAllUsers());
			} else {
				LOG.info("Error en la peticion de obtener todos los usuarios");
				return ResponseEntity.badRequest().body("El usuario no esta iniciado en el sistema.");
			}

	}
	
	/**
	 * Peticion GET que obtiene el usuario cuyo username sea igual al pedido
	 * @param tokenpass valor del token de sesion del usuario
	 * @return informacion correspondiente al usuario con el username
	 */
	@GetMapping(value = "/getuser")
	public ResponseEntity<String> getUser(@RequestParam("tokenpass") final String tokenpass,
			@RequestParam("username") final String username){

			final boolean existe = this.userService.getUserByTokenPass(tokenpass);
			if (existe) {
				LOG.info("Get user");
				return ResponseEntity.ok(this.userService.findByUsername(username));
			} else {
				LOG.info("El usuario no tiene iniciada sus sesión para obtener un usuario concreto.");
				return ResponseEntity.badRequest().body("El usuario no tiene iniciada sus sesión.");
			}


	}
	
	/**
	 * Peticion DELETE que obtiene el usuario cuyo username sea igual al pedido y lo elimina
	 * @param tokenpass valor del token de sesion del usuario
	 * @return resultado de la operacion de borrar a un usuario
	 */
	@DeleteMapping(value = "/deleteuser")
	public ResponseEntity<String> deleteUser(@RequestParam("username") final String username,
			@RequestParam("tokenpass") final String tokenpass) {

			final boolean existe = this.userService.getUserByTokenPassAdmin(tokenpass);
			if (existe) {
				LOG.info("Delete user " + username);
				this.userService.deleteUser(username);
				return ResponseEntity.ok("Usuario eliminado correctamente.");
			} else {
				LOG.info("El usuario no ha iniciado su sesion para poder borrar a un usuario");
				return ResponseEntity.badRequest().body("El usuario no ha iniciado su sesion para poder borrar a un usuario");
			}

	}
	
	/**
	 * Peticion POST que registra a un nuevo usuario en el sistema
	 * @param tokenpass valor del token de sesion del usuario
	 * @return resultado de la operacion de registrar a un usuario
	 */
	@PostMapping
	public ResponseEntity<String> registrarUsuario(@RequestBody final String message,
			@RequestParam("tokenpass") final String tokenpass) {
		

			
			final JSONObject jso = new JSONObject(message);
			

			final boolean tokenpassCorrect = this.userService.getUserByTokenPassAdmin(tokenpass);

			if (tokenpassCorrect) {
				final String username = jso.getString("username");

				String user = this.userService.findByUsername(username);
				
				if (user==null) {
					LOG.info("Registrando usuario...");

					final String email = jso.getString("email");
					final String role = jso.getString("role");
					final String userGithub = jso.getString("userGithub");

					this.userService.registrarUser(username, role, email, userGithub);

					LOG.info("Usuario registrado.");
					return ResponseEntity.ok("Usuario registrado correctamente.");

				} else {
					LOG.info("Error: El usuario ya está registrado.");
					return ResponseEntity.badRequest().body("Error: El usuario ya está registrado.");
				}
			} else {
				LOG.info("El usuario no ha iniciado sesión para poder registrar a otro usuario.");
				return ResponseEntity.badRequest().body("El usuario no ha iniciado sesión para poder registrar a otro usuario.");
			}
			

	}
	
	/**
	 * Peticion PUT para modificar la informacion referente a un usuario del sistema
	 * @param mensajerecibido valores de los campos que actualizaran a los de la informacion del usuario
	 * @param username valor de usuario del sistema que va a ser actualizado
	 * @param tokenpass valor del token de sesion del usuario
	 * @return resultado de la operacion de actualizar el usuario en el sistema
	 */
	@PutMapping(value = "/{username}")
	public ResponseEntity<String> updateUsuario(@RequestBody final String mensajerecibido,
			@PathVariable final String username, @RequestParam("tokenpass") final String tokenpass) {
		

			final boolean existe = this.userService.getUserByTokenPass(tokenpass);
			if (existe) {
					final JSONObject jso = new JSONObject(mensajerecibido);
					
					JSONObject json = new JSONObject(this.userService.findByUsername(username));
					String id = json.getString("id");
					if (id==null) {
						LOG.info("Error: El usuario no existe.");
						return ResponseEntity.badRequest().body("El usuario no existe.");
					} else {
						
						LOG.info("Actualizando usuario...");
	
						final String password = jso.getString("password");
	
						this.userService.actualizarUsuario(username, password);
	
						LOG.info("[SERVER] Usuario actualizado.");
						return ResponseEntity.ok("Usuario actualizado correctamente.");			
	
					}
			} else {
				LOG.info("No se ha encontrado ningún usuario con esos datos para actualizar.");
				return ResponseEntity.badRequest().body("El usuario no existe.");
			}
			
	}

}
