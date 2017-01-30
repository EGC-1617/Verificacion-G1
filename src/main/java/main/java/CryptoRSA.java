package main.java;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class CryptoRSA {

		
	/**
	 * Dado un voto, generamos su clave publica y privada
	 * 
	 * @param vote
	 * @return Par de claves publica y privada
	 * @throws NoSuchAlgorithmException
	 */
	public KeyPair generateKeyPair(String vote) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		return keyPair;
	}


}
