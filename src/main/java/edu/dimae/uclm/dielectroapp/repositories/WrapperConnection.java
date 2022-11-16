package edu.dimae.uclm.dielectroapp.repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;

public class WrapperConnection implements AutoCloseable{

	private Pool pool;
    private Connection connection;
    private final Logger log = Logger.getLogger(WrapperConnection.class.getName());
    
    @Value("${spring.datasource.url}")
	private String url;
    @Value("${spring.datasource.username}")
	private String username;
    @Value("${spring.datasource.password}")
	private String password;
    

    public WrapperConnection(Pool pool) {
        this.pool = pool;
        try {
        	this.connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
        	log.info("La conexión con la BBDD ha fallado");
        }
    }

    @Override
    public void close(){
        this.pool.liberame(this);

    }

    public PreparedStatement prepareStatement(String sql) {
        try {
            return this.connection.prepareStatement(sql);
        } catch (SQLException e) {
        	log.info("La conexión con la BBDD ha fallado");
        }
        return null;
    }

}
