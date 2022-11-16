package edu.dimae.uclm.dielectroapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.dimae.uclm.dielectroapp.dao.UserDAO;
import edu.dimae.uclm.dielectroapp.dao.UserEmailDAO;
import edu.dimae.uclm.dielectroapp.model.User;
import edu.dimae.uclm.dielectroapp.model.UserEmail;
import edu.dimae.uclm.dielectroapp.utilities.Cypher;

@Service
public class UserService {
	
	private static final Log LOG = LogFactory.getLog(UserService.class);
	
	@Value("${system.email}")
	private String emailsystem;
	@Value("${system.emailpass}")
	private String emailsystempass;
	@Value("${mail.host}")
	private String mailhost;
	@Value("${mail.port}")
	private String mailport;
	@Value("${mail.auth}")
	private String mailauth;
	@Value("${mail.enable}")
	private String mailenable;
	
	private String idStr = "id";
	private String usernameStr = "username";
	private String tokenPassStr = "tokenPass";
	private String delegacionStr = "delegacion";
	private String roleStr = "role";
	private String emailStr = "email";
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserEmailDAO userEmailDAO;
	
	@Autowired
	private Cypher cypher;
	

	/**
	 * obtiene si el usuario que trata de acceder al sistema existe
	 * @param username usuario que quiere acceder
	 * @param password contrasena del usuario que quiere acceder
	 * @return devuelve true si existe y false si no ha sido encontrado en el sistema
	 */
	public boolean getUserByUsernameAndPassword(String username, String password) {

		User usuariologin;
		usuariologin = this.userDAO.getUserByUsernameAndPassword(username, password);
		return usuariologin != null;

	}
	
	/**
	 * Envia al usuario que trata de acceder un usuario seguro para que acceda al sistema
	 * @param username usuario que quiere acceder
	 * @param password contrasena del usuario que quiere acceder
	 * @return se retorna un usuario seguro con un tokenPass temporal que se corresponderá al usuario
	 */
	public String sendSecureUser(String username, String password) {

		User usuariologin;
		usuariologin = this.userDAO.getUserByUsernameAndPassword(username, password);
		usuariologin.setUsername(this.cypher.encrypt(username));
		usuariologin.setPassword(this.cypher.encrypt(password));
		usuariologin.newTokenPass();
		
		UserEmail usuarioEmail = this.userEmailDAO.findByUsername(username);
		String email = this.cypher.decrypt(usuarioEmail.getEmail());
		usuarioEmail.setUsername(this.cypher.encrypt(username));
		usuarioEmail.setEmail(this.cypher.encrypt(email));
		
		this.userDAO.updateUser(usuariologin);
		this.userEmailDAO.updateUserEmail(usuarioEmail);
		
		JSONObject secureUser = new JSONObject();
		secureUser.put("id", usuariologin.getId());
		secureUser.put(this.usernameStr, username);
		secureUser.put("role", this.cypher.decrypt(usuariologin.getRole()));
		secureUser.put(this.tokenPassStr, usuariologin.getTokenPass());
		secureUser.put(this.delegacionStr, usuariologin.getDelegacion());
		secureUser.put(this.emailStr, this.cypher.decrypt(usuarioEmail.getEmail()));

		return secureUser.toString();
		
	}

	/**
	 * Obtiene si el tokenPass pertenece a algún usuario del sistema en ese momento
	 * @param tokenpass cadena unica de caracteres perteneciente al usuario para la comprobacion de acceso temporal al sistema
	 * @return se devuelve si existe un usuario vinculado al tokenPass en el sistema
	 */
	public boolean getUserByTokenPass(String tokenpass) {

		User usuariologin;
		usuariologin = this.userDAO.getUserByTokenPass(tokenpass);
		return usuariologin != null;

	}
	
	
	/**
	 * Verifica si el usuario del tokenpass es admin
	 * @param tokenpass cadena unica de caracteres perteneciente al usuario para la comprobacion de acceso temporal al sistema
	 * @return devuelve si el usuario es o no es admin
	 */
	public boolean getUserByTokenPassAdmin(String tokenpass) {

		User usuariologin;
		usuariologin = this.userDAO.getUserByTokenPass(tokenpass);
		if (usuariologin != null) {
			
			return this.cypher.decrypt(usuariologin.getRole()).equals("admin");
			
		} else {
			return false;
		}
		
	}
	
	/**
	 * Obtiene la informacion de un usuario a partir de su nombre de usuario
	 * @param username indica el nombre de usuario que posee en el sistema
	 * @return devuelve toda la informacion del usuario
	 */
	public String findByUsername(String username) {
		
			
     	User user = this.userDAO.findByUsername(username);
		UserEmail userEmail = this.userEmailDAO.findByUsername(username);
		
		JSONObject secureUser = new JSONObject();
		
		if(user == null) {
			return null;
		}
		secureUser.put(this.idStr, user.getId());
		secureUser.put(this.usernameStr, username);
		secureUser.put(this.roleStr, this.cypher.decrypt(user.getRole()));
		secureUser.put(this.tokenPassStr, user.getTokenPass());
		secureUser.put(this.delegacionStr, user.getDelegacion());
		secureUser.put(this.emailStr, this.cypher.decrypt(userEmail.getEmail()));

		return secureUser.toString();
			
	
	}
	

	/**
	 * Obtiene la informacion de todos los usuarios del sistema
	 * @return devuelve a todos los usuarios del sistema
	 */
	public String getAllUsers(){
		
		List<User> users = this.userDAO.findAll();
		
		JSONArray array = new JSONArray();
		JSONObject secureUser;
		for (int i = 0; i < users.size(); i++) {
			
			secureUser = new JSONObject();
			secureUser.put(this.idStr, users.get(i).getId());
			String username = this.cypher.decrypt(users.get(i).getUsername());
			secureUser.put(this.usernameStr, username);
			secureUser.put(this.roleStr, this.cypher.decrypt(users.get(i).getRole()));
			secureUser.put(this.tokenPassStr, users.get(i).getTokenPass());
			secureUser.put(this.delegacionStr, users.get(i).getDelegacion());
			UserEmail userMail= this.userEmailDAO.findByUsername(username);
			secureUser.put(this.emailStr, this.cypher.decrypt(userMail.getEmail()));
			
			array.put(secureUser);
		}

		return array.toString();
		
	}
	
	
	/**
	 *  Obtiene los usuarios que no son admin 
	 * @param users listado de los usuarios
	 * @return devuelve aquellos usuarios que no son admin del listado
	 */
	private List<User> obtenerUsersNoAdmins(List<User> users){
		
		List<User> usersnoadmin = new ArrayList<>();
		for(int i=0; i<users.size(); i++) {
			String role = this.cypher.decrypt(users.get(i).getRole());
			if(!role.equals("admin")) {
				usersnoadmin.add(users.get(i));
			}
		}
		return usersnoadmin;
		
	}
	
	
	/**
	 * Elimina un usuario del sistema
	 * @param username nombre de usuario que se quiere eliminar
	 */
	public void deleteUser(String username) {
		
		User user = this.userDAO.findByUsername(username);
		this.userDAO.deleteUser(user.getUsername());
		
	}

	
	/**
	 * obtiene el usuario a partir del nombre de usuario y su correo en el sistema
	 * @param username indica el nombre de usuario en el sistema
	 * @param email indica el email del usuario en el sistema
	 * @return devuelve true si existe y false si no ha sido encontrado en el sistema
	 */
	public boolean getByUsernameAndEmail(String username, String email) {
		
		UserEmail useremail;
		
		useremail = this.userEmailDAO.findByUsernameAndEmail(username, email);
		return useremail != null;
		
	}
	
	/**
	 * Registra a un usuario en el sitema a partir d ela informacion introducida, le llegara una contrasena temporal por correo
	 * @param username nomrbe del usuario en el sistema a ser registrado
	 * @param role rol del usuario en el sistema a ser registrado
	 * @param email email del usuario en el sistema a ser registrado
	 * @param delegacion delegacion del usuario en el sistema a ser registrado
	 */
	public void registrarUser(String username, String role, String email, String delegacion) {

		UUID uuid = UUID. randomUUID();
		String uuidAsString = uuid. toString();
		String temporalPassword = uuidAsString.substring(0, 12);
		User usuario = new User(this.cypher.encrypt(username), this.cypher.encrypt(temporalPassword),
				this.cypher.encrypt(role), delegacion);
		
		UserEmail userEmail = new UserEmail(this.cypher.encrypt(username), this.cypher.encrypt(email));
		this.userEmailDAO.saveUserEmail(userEmail);
	
		this.userDAO.saveUser(usuario);
		
		sendEmail(username, temporalPassword, role, delegacion, email);
			
	}
	
	/**
	 * Actualiza los datos del usuario en el sistema
	 * @param username usuario que se actualizara
	 * @param password contrasena del usuario que se actualizara
	 */
	public void actualizarUsuario(String username, String password) {
	
		User usuario = this.userDAO.findByUsername(username);
		UserEmail userEmail =  this.userEmailDAO.findByUsername(username);
		

		String usernameEncriptado = this.cypher.encrypt(username);
		String passwordEncriptado = this.cypher.encrypt(password);
		String roleEncriptado = usuario.getRole();
		String emailEncriptado = userEmail.getEmail();
		usuario.setUsername(usernameEncriptado);
		usuario.setPassword(passwordEncriptado);
		usuario.setRole(roleEncriptado);
		userEmail.setEmail(emailEncriptado);
		
		this.userDAO.updateUser(usuario);
		this.userEmailDAO.updateUserEmail(userEmail);

	}

	public void recoverPassword(String username, String email) {
			
		UUID uuid = UUID. randomUUID();
		String uuidAsString = uuid. toString();
		String temporalPassword = uuidAsString.substring(0, 12);
		User usuario = this.userDAO.findByUsername(username);
		usuario.setPassword(this.cypher.encrypt(temporalPassword));
		this.userDAO.updateUser(usuario);
		String role = this.cypher.decrypt(usuario.getRole());
		String delegacion =  usuario.getDelegacion();
		
		sendEmail(username, temporalPassword, role, delegacion, email);
		
	}

	
	/**
	 * Ennvia un email con los datos del usuario que ha sido creado o que solicita nueva contrasena
	 * @param username usuario que ha sido creado o solicita nueva contrasena
	 * @param temporalPassword contrasena temporal que sera enviada al usuario
	 * @param role indica el rol del usuario
	 * @param delegacion indica la delegacion del usuario
	 * @param email indica el email del usuario
	 */
	private void sendEmail(String username, String temporalPassword, String role, String delegacion, String email){
		
		try {
			Properties prop = new Properties();
			prop.put("mail.smtp.host", mailhost);
	        prop.put("mail.smtp.port", mailport);
	        prop.put("mail.smtp.auth", mailauth);
	        prop.put("mail.smtp.starttls.enable", mailenable); 
	        
	        Session session = Session.getInstance(prop,
	                new javax.mail.Authenticator() {
	        			@Override
	                    protected PasswordAuthentication getPasswordAuthentication() {
	                        return new PasswordAuthentication(emailsystem, emailsystempass);
	                    }
	                });
	        
	     
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(emailsystem));
	        message.setRecipients(
	                Message.RecipientType.TO,
	                InternetAddress.parse(email)
	        );
	        message.setSubject("Data access for new user");
	        message.setText("Hello, \n\n"
	        		+ "your user information to access DielectroApp is:\n\n"
	        		+ "user: "+username+"\n"
	        		+ "password: "+temporalPassword+"\n"
	        		+ "delegacion: "+delegacion+"\n"
	        		+ "role: "+role+"\n\n"
	        		+"Remember that you can change your password if you want in User Ops menu.\n\n"
	        		+"Thank you.\n\n"
	        		+"Kind regards. \n\n"
	        		+ "https://myesidevopsmetrics.herokuapp.com");
	
	        Transport.send(message);
	
	        String mensaje ="\nEmail enviado al correo "+email;
	        LOG.info(mensaje);
		
		} catch ( MessagingException e) {
			e.toString();
		}
		
	}

}
