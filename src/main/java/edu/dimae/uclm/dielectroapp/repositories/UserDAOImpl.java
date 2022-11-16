package edu.dimae.uclm.dielectroapp.repositories;

import java.util.List;
import org.springframework.stereotype.Repository;

import edu.dimae.uclm.dielectroapp.dao.UserDAO;
import edu.dimae.uclm.dielectroapp.model.User;

@Repository
public class UserDAOImpl implements UserDAO{

	@Override
	public User getUserByUsernameAndPassword(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUser(User usuariologin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User getUserByTokenPass(String tokenpass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUser(User usuario) {
		// TODO Auto-generated method stub
		
	}

}
