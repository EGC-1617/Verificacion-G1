package main.java;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class CryptoRSA {

	
	private static Cipher rsa;
	
	
	/**
	 * Dado un voto, generamos su clave pública y privada
	 * 
	 * @param vote : Voto sin encriptar
	 * @return Par de claves pública y privada
	 * @throws NoSuchAlgorithmException
	 */
	public KeyPair generateKeyPair(String vote) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		return keyPair;
	}
	

	/**
	 * Método que dado un voto y una clave pública, encripta usando RSA
	 * 
	 * @param vote : Voto sin encriptar
	 * @param key : Clave publica
	 * @return Voto encriptado
	 */
	public static String encrypt (String vote, PublicKey key) {

		String cipherVoteString = null;
		byte[] cipherVote = null;

		try {
			// Obtener el tipo de encriptacion que vamos a realizar
			rsa = Cipher.getInstance("RSA");
			
			// Encripta
			rsa.init(Cipher.ENCRYPT_MODE, key);
			cipherVote = rsa.doFinal(vote.getBytes());

			// Convertimos a String el voto encriptado
			cipherVoteString = new String(cipherVote);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cipherVoteString;
	}
	
	
	/**
	 * Método que dado un voto y una clave privada, desencripta usando RSA
	 * 
	 * @param cipherText : Voto encriptado
	 * @param key : Clave privada
	 * @return Voto desencriptado
	 */
	public static String decrypt (String cipherText, PrivateKey key) {

		String vote = null;
		byte[] dectyptedVote = null;

		try {
			// Obtener el tipo de encriptacion que vamos a realizar
			rsa = Cipher.getInstance("RSA");
			
			// Desencripta
			rsa.init(Cipher.DECRYPT_MODE, key);
			dectyptedVote = rsa.doFinal(cipherText.getBytes());

			// Convertimos a String el voto desencriptado
			vote = new String(dectyptedVote);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vote;
	}


}
