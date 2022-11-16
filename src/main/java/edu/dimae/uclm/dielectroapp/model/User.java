package edu.dimae.uclm.dielectroapp.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Entidad usuario en la base de datos.
 * 
 * @author FcoCrespo
 */
@Entity
@Table(name="USERS")
public class User implements Comparable<User> {
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
	 * Password.
	 * 
	 * @author FcoCrespo
	 */
	@NotNull
	private String password;
	/**
	 * Role.
	 * 
	 * @author FcoCrespo
	 */
	private String role;

	/**
	 * tokenPass.
	 * 
	 * @author FcoCrespo
	 */
	private String tokenPass;

	/**
	 * userGithub.
	 * 
	 * @author FcoCrespo
	 */
	private String delegacion;


	/**
	 * Constructor de Usuario.
	 * 
	 * @author FcoCrespo
	 */
	public User(@NotNull final String username, @NotNull final String password, final String role, final String delegacion) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.delegacion = delegacion;
	}

	@Override
	public int compareTo(User o) {
		return this.getUsername().compareTo(o.getUsername());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}

		User other = (User) obj;
		return this.getUsername() == other.getUsername();

	}

	@Override
	public int hashCode() {
		return this.hashCode();
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getDelegacion() {
		return delegacion;
	}

	public void setDelegacion(String delegacion) {
		this.delegacion = delegacion;
	}
	
	public String getTokenPass() {
		return tokenPass;
	}

	public void setTokenPass(String tokenPass) {
		this.tokenPass = tokenPass;
	}

	public void newTokenPass() {
		this.tokenPass = UUID.randomUUID().toString();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", role="
				+ role + ", delegacion=" + delegacion + ", tokenPass=" + tokenPass + "]";
	}

	

	
}
