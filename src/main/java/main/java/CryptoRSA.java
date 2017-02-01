package main.java;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

public class CryptoRSA {

	private static Cipher rsa;

	/**
	 * Dado un voto, generamos su clave pública y privada
	 * 
	 * @param vote
	 *            : Voto sin encriptar
	 * @return Par de claves pública y privada
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
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
	public static byte[] encrypt(byte[] textToEncypt, Key publicKey) {
		byte[] cipherText = new byte[0];

		try {
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
	public static byte[] decrypt (byte[] cipherText, Key privateKey) {
		byte[] text = new byte[0];

		try {
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

}
