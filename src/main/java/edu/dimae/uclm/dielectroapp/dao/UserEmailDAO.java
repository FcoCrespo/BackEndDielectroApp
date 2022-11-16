package edu.dimae.uclm.dielectroapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.dimae.uclm.dielectroapp.model.UserEmail;

public interface UserEmailDAO extends JpaRepository <UserEmailDAO, String>{

	UserEmail findByUsername(String username);

	void updateUserEmail(UserEmail usuarioEmail);

	UserEmail findByUsernameAndEmail(String username, String email);

	void saveUserEmail(UserEmail userEmail);

}
