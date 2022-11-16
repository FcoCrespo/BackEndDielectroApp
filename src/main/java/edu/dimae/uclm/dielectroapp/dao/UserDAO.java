package edu.dimae.uclm.dielectroapp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.dimae.uclm.dielectroapp.model.User;

@Repository
public interface UserDAO {

	User getUserByUsernameAndPassword(String username, String password);

	void updateUser(User usuariologin);

	User getUserByTokenPass(String tokenpass);

	User findByUsername(String username);

	List<User> findAll();

	void deleteUser(String username);

	void saveUser(User usuario);

}
