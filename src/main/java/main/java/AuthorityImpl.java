package main.java;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class AuthorityImpl implements Authority{
	
	/**
	 * Esta función obtiene las claves pública y privada de la votación cuyo id es el pasado 
	 * como parámetro. Resaltar que hacemos uso del proyecto Elliptic_SDK, que es una librería
	 * criptográfica el�ptica bajo la licensia GPL v3. Más información en la clase CryptoEngine.java.
	 * Se crean las claves mediante el m�todo generateKeyPair() de la clase CryptoEngine.
	 * La clave pública es de la forma xxxxxxx++++yyyyyyy, siendo el + un separador.
	 * Se cifra en base64 solo la clave pública y se guarda la clave pública y privada en la base de
	 * datos. Finalmente se comprueba que se haya guardado correctamente.
	 * @param id. Corresponde al id de la votación
	 * @param token. Corresponde al token que se comprobará si es el adecuado para seguir la operación.
	 * @return res. Boolean que indica si la operación ha tenido éxito.
	 */
	@Override
	public boolean postKey(String id, Integer token) {
		boolean res;
		BigInteger secretKey;
		String publicKey;
		String encodedPublicKey;
		res = false;
		
		if(Token.checkToken(new Integer(id), token)){
			try{
				
				Token.createToken(new Integer(id));				
				
				CryptoEngine cryptoEngine = new CryptoEngine(id);
				cryptoEngine.generateKeyPair();
		
				secretKey = cryptoEngine.getKeyPair().getSecretKey();
				publicKey = cryptoEngine.getKeyPair().getPublicKey().getX()+"++++"+cryptoEngine.getKeyPair().getPublicKey().getY();
				
				encodedPublicKey = new String(Base64.encodeBase64(publicKey.getBytes()));
				
				RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
				 //Llamamos a la función que se encarga de guardar el par de claves asociadas
				 // a la votación cuya id se especifica como parámetro.

				if (rdbm.postKeys(id, encodedPublicKey, secretKey.toString())){
					res = true;
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			throw new VerificationException("El token no coincide");
		}
		
		return res;
	}
	
		public boolean postKeyLocal(String id, Integer token) {
		boolean res;
		BigInteger secretKey;
		String publicKey;
		String encodedPublicKey;
		res = false;
		
		if(TokenLocal.checkToken(new Integer(id), token)){
			try{
				
				TokenLocal.createToken(new Integer(id));				
				
				CryptoEngine cryptoEngine = new CryptoEngine(id);
				cryptoEngine.generateKeyPair();
		
				secretKey = cryptoEngine.getKeyPair().getSecretKey();
				publicKey = cryptoEngine.getKeyPair().getPublicKey().getX()+"++++"+cryptoEngine.getKeyPair().getPublicKey().getY();
				
				
				//Cambiar metodo de autenticacion
				encodedPublicKey = new String(Base64.encodeBase64(publicKey.getBytes()));
				
				
				//Usar base de datos local
				LocalDataBaseManager rdbm=new LocalDataBaseManager();

				if (rdbm.postKeys(id, encodedPublicKey, secretKey.toString())){
					res = true;
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			throw new VerificationException("El token no coincide");
		}
		
		return res;
	}

	@Override
	public String getPublicKey(String id, Integer token) {
		
		String result = "";
		
		if(Token.checkTokenDb(new Integer(id), token)){
			RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
			//Llamamos a la función que conecta con la base de datos remota y obtiene la clave pública.
			result = rdbm.getPublicKey(id);
		}else{
			throw new VerificationException("El token no coincide en getPublicKey");
		}
		
		return result;
		
	}
	
	public String getPublicKeyLocal(String id, Integer token) {
		
		String result = "";
		
		if(TokenLocal.checkTokenDb(new Integer(id), token)){
			LocalDataBaseManager rdbm=new LocalDataBaseManager();
			//Llamamos a la función que conecta con la base de datos remota y obtiene la clave pública.
			result = rdbm.getPublicKey(id);
		}else{
			throw new VerificationException("El token no coincide en getPublicKey Local");
		}
		
		return result;
		
	}

	@Override
	public String getPrivateKey(String id, Integer token) {
		
		String result = "";
		
		if(Token.checkTokenDb(new Integer(id), token)){
			RemoteDataBaseManager rdbm=new RemoteDataBaseManager();
			//Llamamos a la función que conecta con la base de datos remota y obtiene la clave privada.
			result = rdbm.getSecretKey(id);
		}else{
			throw new VerificationException("El token no coincide en getPrivateKey");
		}		
		
		return result;
	}


	@Override
	public String getPrivateKeyLocal(String id, Integer token) {
		
		String result = "";
		
		if(TokenLocal.checkTokenDb(new Integer(id), token)){
			LocalDataBaseManager rdbm=new LocalDataBaseManager();
			//Llamamos a la función que conecta con la base de datos remota y obtiene la clave privada.
			result = rdbm.getSecretKey(id);
		}else{
			throw new VerificationException("El token no coincide en getPrivateKey Local");
		}		
		
		return result;
	}


	@Override
	public String decrypt(String idVote, String cipherText, Integer token) throws Exception {
		String result = "";
	
		String secretKey;
		String publicKey;

		
		if(Token.checkTokenDb(new Integer(idVote), token)){
			
			secretKey = getPrivateKey(idVote, token);
			System.out.println(secretKey);
			
			publicKey = getPublicKey(idVote, token);
			byte[] keyDecoded2 = Base64.decodeBase64(publicKey.getBytes());
			publicKey = new String(keyDecoded2);
			
			result= Desencriptar(cipherText);

			


		}else{
			throw new VerificationException("El token no coincide en desencriptar");
		}
				
		return result;
	}
	
	@Override
	public String decryptLocal(String idVote, String cipherText, Integer token) throws Exception {
		String result = "";
	
		String secretKey;
		String publicKey;

		
		if(TokenLocal.checkTokenDb(new Integer(idVote), token)){
			
			secretKey = getPrivateKeyLocal(idVote, token);
			System.out.println(secretKey);
			
			publicKey = getPublicKeyLocal(idVote, token);
			byte[] keyDecoded2 = Base64.decodeBase64(publicKey.getBytes());
			publicKey = new String(keyDecoded2);
			
			result= Desencriptar(cipherText);

		}else{
			throw new VerificationException("El token no coincide en desencriptar Local");
		}
				
		return result;
	}

	/**
	 * Esta función corta un voto en claro en partes de longitud 31 para que el método de cifrar
	 * funcione correctamente.
	 * @param votoEnClaro. String que representa el voto en claro para cortar.
	 * @return result. String[] que indica los distintos trozos en los que ha sido cortado el voto en claro.
	 */

	
	/**
	 * Esta función corta un voto cifrado en partes situadas entre el símbolo "|", de esa forma en el
	 * método decrypt descifrará las distintas partes cifradas que han resultado de cifrar los trozos
	 * que devuelve el método cutVote.
	 * @param votoCifrado. String que representa el voto cifrado para cortar.
	 * @return result. String[] que indica los distintos trozos en los que ha sido cortado el voto cifrado.
	 */
	
	
	
	public String encrypt(String idVote, String textToEncypt, Integer token) {
		String result;
		String publicKeyBD = "";
		String aux;
		
		if(Token.checkTokenDb(new Integer(idVote), token)){
			
			//Obtengo la clave pública con getKey y separa esto en x e y (que es la mitad y hacer un new PointGMP). Acordarse de que  
			//en la base de datos se guarda en base64
			publicKeyBD = getPublicKey(idVote, token);

			byte[] keyDecoded = Base64.decodeBase64(publicKeyBD.getBytes());
			publicKeyBD = new String(keyDecoded);
				
			aux=Encriptar(textToEncypt);
			
			result=aux;

		}else{
			throw new VerificationException("El token no coincide en encriptar");
		}
				
		return result;
	}

	public String encryptLocal(String idVote, String textToEncypt, Integer token) {
		String result;
		String publicKeyBD = "";
		String aux;
		
		if(TokenLocal.checkTokenDb(new Integer(idVote), token)){
			
			//Obtengo la clave pública con getKey y separa esto en x e y (que es la mitad y hacer un new PointGMP). Acordarse de que  
			//en la base de datos se guarda en base64
			publicKeyBD = getPublicKeyLocal(idVote, token);

			byte[] keyDecoded = Base64.decodeBase64(publicKeyBD.getBytes());
			publicKeyBD = new String(keyDecoded);
				
			aux=Encriptar(textToEncypt);
			
			result=aux;

		}else{
			throw new VerificationException("El token no coincide en encriptar Local");
		}
				
		return result;
	}

	
	public static String Encriptar(String texto){
		
		String secretKey= "qualityinfoSolutions"; //llave para encriptar datos
		String base64EncryptedString = "";
		
		try{
			
			MessageDigest md=MessageDigest.getInstance("MD5");
			byte[] digestOfPassowrd = md.digest(secretKey.getBytes("utf-8"));
			byte[] keyBytes = Arrays.copyOf(digestOfPassowrd, 24);
			
			SecretKey key = new SecretKeySpec(keyBytes, "DESede");
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			byte[] plainTextBytes = texto.getBytes("utf-8");
			byte[] buf = cipher.doFinal(plainTextBytes);
			byte[] base64Bytes = Base64.encodeBase64(buf);
			base64EncryptedString = new String(base64Bytes);
		}catch (Exception ex){
		}
	return base64EncryptedString;
	}
	
	
	
	public static String Desencriptar(String textoEncriptado) throws Exception{
		
		String secretKey = "qualityinfoSolutions"; 
		String base64EncryptedString = "";
		
		try{
			byte[] message = Base64.decodeBase64(textoEncriptado.getBytes("utf-8"));
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
			byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
			SecretKey key = new SecretKeySpec(keyBytes, "DESede");
			
			Cipher decipher = Cipher.getInstance("DESede");
			decipher.init(Cipher.DECRYPT_MODE, key);
			
			byte[] plainText = decipher.doFinal(message);
			
			base64EncryptedString = new String(plainText, "UTF-8");
		}catch (Exception ex){
		}
		return base64EncryptedString;
	}

	
	

}
