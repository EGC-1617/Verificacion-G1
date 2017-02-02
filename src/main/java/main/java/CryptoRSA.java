package main.java;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



public class CryptoRSA {

	private static Cipher rsa;
	private static KeyPair keyPair;

	/**
	 * Dado un voto, generamos su clave pública y privada
	 * 
	 * @param vote
	 *            : Voto sin encriptar
	 * @return Par de claves pública y privada
	 * @throws NoSuchAlgorithmException
	 */
	public static  KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		return keyPair;
	}

	

	/**
	 *  Método que dado un voto y una clave pública, encripta usando RSA
	 *  
	 * @param textToEncypt
	 *            : Voto sin encriptar
	 * @param publicKey
	 *            : Clave publica
	 * @return Voto encriptado
	 */
	
	
	public static byte[] encrypt(String votationId,byte[] textToEncypt, Key publicKey , Integer token) {
		byte[] cipherText = new byte[0];
		
		try {
			if(Token.checkTokenDb(new Integer(votationId), token))
			// Obtener el tipo de encriptacion que vamos a realizar
			rsa = Cipher.getInstance("RSA");
			
			// Encripta
			rsa.init(Cipher.ENCRYPT_MODE, publicKey);
			cipherText = rsa.doFinal(textToEncypt);

		} catch (Exception e) {
			System.out.println("encrypt exception: " + e.getMessage());
		}

		return cipherText;
	}

	/**
	 * Método que dado un voto y una clave privada, desencripta usando RSA
	 * 
	 * @param cipherText
	 *            : Voto encriptado
	 * @param privateKey
	 *             : Clave privada
	 * @return Voto desencriptado
	 */
	public static byte[] decrypt (String votationId,byte[] cipherText,Key privateKey,Integer token) {
		byte[] text = new byte[0];
		
		try {
			if(Token.checkTokenDb(new Integer(votationId), token))
			// Obtener el tipo de encriptacion que vamos a realizar
			rsa = Cipher.getInstance("RSA");
			     
			
			// Desencripta
			rsa.init(Cipher.DECRYPT_MODE, privateKey);
			text = rsa.doFinal(cipherText);

		} catch (Exception e) {
			System.out.println("decrypt exception: " + e.getMessage());
		}

		return text;
	}
	
	// --------------------------------------------------------------------------------------------
	

	
	/**
	 *  Método que dado un voto y una clave pública, encripta usando RSA
	 *  
	 * @param textToEncypt
	 *            : Voto sin encriptar
	 * @param publicKey
	 *            : Clave publica
	 *
	 * @return Voto encriptado
	 *
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] encryptLocal (String votationId,byte[] textToEncypt,Key publicKey, Integer token) throws NoSuchAlgorithmException {
		byte[] cipherText = new byte[0];

		LocalDataBaseManager dbLocal = new LocalDataBaseManager();
		
		keyPair = generateKeyPair();
		
		String publicKey2 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
		String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
		
		dbLocal.postKeys(votationId, publicKey2, privateKey);
		LocalDataBaseManager.sendGeneratedToken(new Integer(votationId), token);
		
		try {
			if(TokenLocal.checkTokenDb(new Integer(votationId), token))
			// Obtener el tipo de encriptacion que vamos a realizar
			rsa = Cipher.getInstance("RSA");
			
			// Encripta
			rsa.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
			cipherText = rsa.doFinal(textToEncypt);

		} catch (Exception e) {
			System.out.println("encrypt exception: " + e.getMessage());
		}

		return cipherText;
	}

	/**
	 * Método que dado un voto y una clave privada, desencripta usando RSA
	 * 
	 * @param cipherText
	 *            : Voto encriptado
	 * @param privateKey
	 *             : Clave privada
	 * @return Voto desencriptado
	 */
	public static byte[] decryptLocal (String votationId,byte[] cipherText,Key privateKey,Integer token) {
		byte[] text = new byte[0];

		LocalDataBaseManager dbLocal = new LocalDataBaseManager();
		
		try {
			if(TokenLocal.checkTokenDb(new Integer(votationId), token))
			// Obtener el tipo de encriptacion que vamos a realizar
			rsa = Cipher.getInstance("RSA");
			
			byte[] encodedKey     = Base64.getDecoder().decode(dbLocal.getSecretKey(votationId));
			SecretKey privateKey2 = new SecretKeySpec(encodedKey, 0, encodedKey.length, "RSA");
			
			
			// Desencripta
			rsa.init(Cipher.DECRYPT_MODE, privateKey);
			text = rsa.doFinal(cipherText);

		} catch (Exception e) {
			System.out.println("decrypt done: " + e.getMessage());
		}

		return text;
	}

}
