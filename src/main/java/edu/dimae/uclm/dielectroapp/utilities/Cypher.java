package edu.dimae.uclm.dielectroapp.utilities;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.util.Base64;

/**
*
* @author FcoCrespo
* source = https://stackoverflow.com/questions/46153425/aes-gcm-nopadding-aeadbadtagexception
* OriginalAuthor = President James K. Polk 
* Profile = https://stackoverflow.com/users/238704/president-james-k-polk
* Using: https://docs.oracle.com/javase/8/docs/api/javax/crypto/spec/GCMParameterSpec.html
* 
*/
@Component
public class Cypher {
	
	@Value("${app.key}")
	private String accessKey;
	
	//Longitud de la inicializacion del vector Galois - Counter Mode (GCM) 
	private static final int GCM_IV_LENGTH = 12;
	//Longitud del tag de autenticacion en bits Galois - Counter Mode (GCM) 
    private static final int GCM_TAG_LENGTH = 16;
	
    /**
     * Cifra la cadena de caracteres a partir de la clave del sistema
     * @param privateString clave para ser encriptada por el sistema
     * @return clave encriptada por el sistema
     */
	public String encrypt(String privateString) {
		
		try {
			byte[] iv = new byte[GCM_IV_LENGTH];
	        (new SecureRandom()).nextBytes(iv);
	        
	        String secretKey = accessKey;
	    	MessageDigest md = MessageDigest.getInstance("SHA-512");
	    	byte[] digestOfPassword = md.digest(secretKey.getBytes(StandardCharsets.UTF_8));
	        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
	        SecretKey key = new SecretKeySpec(keyBytes, "AES"); 

	        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
	        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
	        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

	        byte[] ciphertext = cipher.doFinal(privateString.getBytes(StandardCharsets.UTF_8));
	        byte[] encrypted = new byte[iv.length + ciphertext.length];
	        System.arraycopy(iv, 0, encrypted, 0, iv.length);
	        System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);
	        
	        return Base64.getEncoder().encodeToString(encrypted);
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException
				 | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
		
    }

	/**
     * Descifra la cadena de caracteres a partir de la clave del sistema
     * @param encrypted clave para ser desencriptada por el sistema
     * @return clave desencriptada por el sistema
     */
    public String decrypt(String encrypted){
    	
    	try {
	    	String secretKey = accessKey;
	    	MessageDigest md = MessageDigest.getInstance("SHA-512");
	    	byte[] digestOfPassword = md.digest(secretKey.getBytes(StandardCharsets.UTF_8));
	        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
	        
	        SecretKey key = new SecretKeySpec(keyBytes, "AES"); 
	        
	    	byte[] decoded = Base64.getDecoder().decode(encrypted);
	
	        byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);
	
	        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
	        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
	        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
	    		
	        byte[] ciphertext = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);
	         
	        return new String(ciphertext, StandardCharsets.UTF_8);
    	}
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException
				 | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
    }
    
}