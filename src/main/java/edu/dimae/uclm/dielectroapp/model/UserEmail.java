package edu.dimae.uclm.dielectroapp.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entidad emails de usuarios en la base de datos.
 * 
 * @author FcoCrespo
 */
@Entity
@Table(name="USERSEMAIL")
public class UserEmail{
	
	/**
	 * ID.
	 * 
	 * @author FcoCrespo
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	/**
	 * username.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String username;
	/**
	 * email.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String email;
	
	public UserEmail(@NotNull String username, @NotNull String email) {
		super();
		this.username = username;
		this.email = email;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "UserEmail [id=" + id + ", username=" + username + ", email=" + email + "]";
	}
	

	
}