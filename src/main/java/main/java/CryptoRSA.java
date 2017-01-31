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
	
	
	



	public static String encrypt (String vote, PublicKey key) {

		String cipherVoteString = null;
		byte[] cipherVote = null;

		try {
			rsa = Cipher.getInstance("RSA");
			rsa.init(Cipher.ENCRYPT_MODE, key);
			
			cipherVote = rsa.doFinal(vote.getBytes());

			cipherVoteString = new String(cipherVote);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cipherVoteString;
	}
	
	public static String decrypt (String cipherText, PrivateKey key) {

		String vote = null;
		byte[] dectyptedVote = null;

		try {

			rsa = Cipher.getInstance("RSA");
			rsa.init(Cipher.DECRYPT_MODE, key);

			dectyptedVote = rsa.doFinal(cipherText.getBytes());

			vote = new String(dectyptedVote);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vote;
	}


}
